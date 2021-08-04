package springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import springboot.models.DTO.PostDTO;
import springboot.models.Candidacy;
import springboot.models.Post;
import springboot.models.User;
import springboot.services.base.CandidacyService;
import springboot.services.base.StorageService;
import springboot.services.base.UserService;
import springboot.services.base.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CandidacyService candidacyService;

    @Autowired
    private StorageService storageService;

    @ModelAttribute("post")
    public PostDTO userRegistrationDto() {
        return new PostDTO();
    }

    @GetMapping("/posts")
    public String showCreatePostForm() {
        return "posts";
    }

    @PostMapping("/posts")
    public String savePost(@ModelAttribute("post") @Valid PostDTO postDTO, BindingResult result, Authentication authentication, @RequestParam("file") MultipartFile file){
        if(result.hasErrors()){
            return "posts";
        }
        storageService.store(file);
        postDTO.setImgName(file.getOriginalFilename());
        User user = userService.findByUsername(authentication.getName());
        postService.save(postDTO, user);
        return "redirect:/index";
    }


    @GetMapping("post/close/{id}")
    public  String closePost(@PathVariable String id, HttpServletRequest request){
        postService.closeById(id);
        return "redirect:/index";
    }

    @GetMapping("edit/{id}")
    public String editPost(@PathVariable String id, Model model)
    {
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("id", id);
        return "editPost";
    }

    @PostMapping("edit/update/{id}")
    public  String updatePost(@ModelAttribute("post") @Valid PostDTO postDTO, BindingResult result, @PathVariable String id,  @RequestParam("file") MultipartFile file){
        if(result.hasErrors()){
            return "editPost";
        }
        storageService.store(file);
        postDTO.setImgName(file.getOriginalFilename());
        postService.update(id, postDTO);
        return "redirect:/index";
    }

    @GetMapping("list/{id}")
    public String listCandidacy(@PathVariable String id, Model model){
        model.addAttribute("cands", postService.findById(id).getCandidacyList());
        model.addAttribute("post_id", id);
        model.addAttribute("can_serv", candidacyService);
        return "listApplications";
    }

    @GetMapping("app/{post_id}/{cand_id}/{user_id}")
    public String applicationInfo(@PathVariable String post_id, @PathVariable String cand_id,  @PathVariable String user_id,
                                     Model model, Authentication authentication){

        model.addAttribute("user", userService.findByUsername(authentication.getName()));
        model.addAttribute("post_id", post_id);
        model.addAttribute("candidacy", candidacyService.findById(Integer.parseInt(cand_id)));

        String filename = storageService.findFilenameByUserCand(userService.findById(user_id), Integer.parseInt(cand_id));

        if(filename != null) {
            model.addAttribute("files", storageService.loadByFileName(filename)
                    .map(
                            path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                    "serveFile", path.getFileName().toString()).build().toString())
                    .collect(Collectors.toList()));
        }else model.addAttribute("files", null);

        return "applicationInfo";
    }
    @ModelAttribute("users")
    public List<User> users() {
        return userService.listAllUsers();
    }

    @ModelAttribute("posts")
    public List<Post> posts() {
        return postService.listAllPosts();
    }

    @ModelAttribute("applicants")
    public  List<Candidacy> applicants(){return candidacyService.listAllCand();}
}