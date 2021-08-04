package springboot.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.models.DTO.PostDTO;
import springboot.models.DTO.PostEmployeeDTO;
import springboot.models.PostEmployee;
import springboot.models.User;
import springboot.services.base.*;

import javax.validation.Valid;

@Component
@Controller
public class PostEmplController {
    @Autowired
    private PostEmployeeService postEmployeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;


    @ModelAttribute("postdto")
    public PostEmployeeDTO userRegistrationDto() {
        return new PostEmployeeDTO();
    }

    @GetMapping("/create/postEmp")
    public String showCreatePostForm(Model model, Authentication authentication) {
        PostEmployee p =  postEmployeeService.findByUser(userService.findByUsername(authentication.getName()));
        if(p != null){
            model.addAttribute("postdto", p);
            model.addAttribute("post_id", p.getId());
        }

        return "postEmp";
    }

    @GetMapping("delete/post/emp/{id}")
    public String deletePostEmp(@PathVariable("id") String id){
        postEmployeeService.deleteById(id);
        return "redirect:/index_user";
    }

    @PostMapping("/create/post/emp")
    public String savePostEmp(@ModelAttribute("postdto") @Valid PostEmployeeDTO postEmployeeDTO, BindingResult result,
                              Authentication authentication, @RequestParam("file") MultipartFile file, Model model){
        if(result.hasErrors()){
            return "postEmp";
        }

        storageService.store(file);
        postEmployeeDTO.setImgName(file.getOriginalFilename());
        User user = userService.findByUsername(authentication.getName());
        postEmployeeDTO.setUser(user);
        String user_id = user.getId().toString();
        postEmployeeService.deleteAllByUser(user_id, postEmployeeService.listByEmployee(user_id));
        postEmployeeService.save(postEmployeeDTO, user);

        model.addAttribute("post", postEmployeeService.findByUser(user));
        return "empView";
    }

    @GetMapping("/create/view")
    public String showCreated(Model model, Authentication authentication) {

        if(postEmployeeService.listAllPosts() != null)
            model.addAttribute("posts", postEmployeeService.listAllPosts());

        return "postEmpList";
    }
}
