package springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springboot.models.Candidacy;
import springboot.models.DTO.NotificationDTO;
import springboot.models.File;
import springboot.models.Post;
import springboot.models.User;
import springboot.models.enums.State;
import springboot.services.NotificationService;
import springboot.services.base.CandidacyService;
import springboot.services.base.StorageService;
import springboot.services.base.UserService;
import springboot.services.base.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
public class JobApplicationController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CandidacyService candidacyService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private NotificationService notificationService;


    // logic of the employee
    @GetMapping("/index_user")
    public String index_user(Model model, Authentication authentication){
        PageRequest pageable = PageRequest.of( 0, 12);
        Page<Post> postPage = postService.getPaginatedPosts(pageable);

        int totalPages = postPage.getTotalPages();
        if(totalPages >= 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("activePostList", true);
        model.addAttribute("postList", postPage.getContent());

        model.addAttribute("user", userService.findByUsername(authentication.getName()));
        model.addAttribute("can_serv", candidacyService);
        return "index_user";
    }

    @GetMapping("/sort/{category}")
    public String index_sort(@PathVariable("category") String category, Model model, Authentication authentication){

        PageRequest pageable = PageRequest.of( 0, 4);
        Page<Post> postPage = null;
        if(category.equalsIgnoreCase("fulltime"))
            postPage = postService.getPaginatedPostsByJobType("Full time", pageable);
        else if(category.equalsIgnoreCase("parttime"))
            postPage = postService.getPaginatedPostsByJobType("Part time", pageable);

        int totalPages = postPage.getTotalPages();

        if(totalPages >= 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("activePostList", true);
        model.addAttribute("postList", postPage.getContent());

        model.addAttribute("user", userService.findByUsername(authentication.getName()));
        model.addAttribute("can_serv", candidacyService);

        return "index_user";
    }

    @GetMapping("/post/view/{id}")
    public String viewPost(@PathVariable("id") String id, Model model, Authentication authentication) {

       Post p = postService.findById(id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User user = null;
        if (principal instanceof UserDetails) {
            user = userService.findByUsername(((UserDetails)principal).getUsername());
        }

        Candidacy cand = null;
        model.addAttribute("cand_id", -1);
        for(Candidacy c : p.getCandidacyList()){
           if(c.getUser().getUserName().equals(user.getUserName())){
               cand = c;
               cand.setId(c.getId());
               model.addAttribute("cand_id", cand.getId());
               break;
           }
        }

        if (cand == null) {
            cand = new Candidacy();
            cand.setId(null);
            cand.setPost(p);
        }

        String filename = storageService.findFilenameByUserCand(userService.findByUsername(authentication.getName()), cand.getId());

        if(filename != null) {
            model.addAttribute("files", storageService.loadByFileName(filename)
                    .map(
                            path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                    "serveFile", path.getFileName().toString()).build().toString())
                    .collect(Collectors.toList()));
        }else model.addAttribute("files", null);

        model.addAttribute("candidacy", cand);
       return "postApply";
    }

    @PostMapping("apply/{post_id}/{cand_id}")
    public String applySave(@PathVariable("post_id") String post_id,
                            @PathVariable("cand_id") String cand_id,
                            @ModelAttribute("candidacy") Candidacy candidacy,
                            @RequestParam("file") MultipartFile file, Authentication authentication){

        storageService.store(file);

        Candidacy c = candidacyService.findById(Integer.parseInt(cand_id));

        if(c != null){
            c.setComment(candidacy.getComment());
            candidacyService.updateCand(c);
            storageService.deleteByUser(userService.findByUsername(authentication.getName()));
            storageService.save(StringUtils.cleanPath(file.getOriginalFilename()),
                    StringUtils.cleanPath(file.getContentType()),
                    userService.findByUsername(authentication.getName()),Integer.parseInt(cand_id));
        }else{
            User user = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                user = userService.findByUsername(((UserDetails)principal).getUsername());
            }
            candidacy.setUser(user);
            Post p = postService.findById(post_id);
            candidacy.setPost(p);


            Candidacy cand_ = candidacyService.save(candidacy);

            storageService.deleteByUser(userService.findByUsername(authentication.getName()));
            storageService.save(StringUtils.cleanPath(file.getOriginalFilename()),
                    StringUtils.cleanPath(file.getContentType()), userService.findByUsername(authentication.getName()),cand_.getId());
        }

            return "redirect:/index_user";
    }

    @GetMapping("close/{cand_id}")
    public String candClose(@PathVariable("cand_id") String cand_id){
        if(cand_id != null)
        candidacyService.deleteById(candidacyService.findById(Integer.parseInt(cand_id)));
        return "redirect:/index_user";
    }

    // employer
    @GetMapping("reject/{cand_id}")
    public String appReject(@PathVariable("cand_id") String cand_id){
        candidacyService.deleteById(candidacyService.findById(Integer.parseInt(cand_id)));
        return "redirect:/index";
    }


    @GetMapping("approve/{cand_id}")
    public String appApprove(@PathVariable("cand_id") String cand_id){

        for (Candidacy c : candidacyService.listAllCand()) {
            if(c.getId() != Integer.parseInt(cand_id))
                candidacyService.deleteById(c);
        }

        Candidacy c = candidacyService.findById(Integer.parseInt(cand_id));
        c.setState(State.approved);
        candidacyService.updateCand(c);

        NotificationDTO n = new NotificationDTO();
        n.setSubject("Approved for a JOB");
        notificationService.sendApprovedNotification(n);

        return "redirect:/index";
    }


    @ModelAttribute("posts")
    public List<Post> posts() {
        return postService.listAllPosts();
    }

    @RequestMapping(value = "user/page/{page}")
    public String listArticlesPageByPage(@PathVariable("page") int page, Model model, Authentication authentication) {

        PageRequest pageable = PageRequest.of( page - 1, 12);

        Page<Post> postPage = postService.getPaginatedPosts(pageable);
        int totalPages = postPage.getTotalPages();
        if(totalPages >= 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("activePostList", true);
        model.addAttribute("postList", postPage.getContent());
        return "index_user";
    }


}
