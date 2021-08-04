package springboot.controllers;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.models.File;
import springboot.models.User;
import springboot.services.base.StorageService;
import springboot.services.base.UserService;
import springboot.services.base.PostService;

import javax.websocket.server.PathParam;

@Controller
public class AdminController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;


    @Autowired
    private StorageService storageService;

    @GetMapping("/adminPage")
    public  String admin(Model model)
    {
        model.addAttribute("users", userService.listAllUsers());
        return "admin";
    }

    @GetMapping("/userSettings/{user_id}")
    public String userUpdateAdmin(Model model, @PathVariable("user_id") String user_id) {

        model.addAttribute("user", userService.findById(user_id));
        model.addAttribute("user_id", user_id);
        model.addAttribute("role", userService.containsRole(userService.findById(user_id), "ROLE_ADMIN"));

        return "userSettings";
    }

    @RequestMapping("/delete/{user_id}")
    public String deleteUserAdmin(@PathVariable("user_id") String id) {
        userService.delete(userService.findById(id));
        return "redirect:/adminPage";
    }

    @GetMapping("/listUsers")
    public String adminList(){return "listUsers";}

}
