package springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import springboot.models.DTO.NotificationDTO;
import springboot.models.DTO.UserRegistrationDTO;
import springboot.models.User;
import springboot.services.NotificationService;
import springboot.services.base.StorageService;
import springboot.services.base.UserService;

import javax.validation.Valid;

@Controller
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute("user")
    public UserRegistrationDTO userRegistrationDto() {
        return new UserRegistrationDTO();
    }

    @GetMapping("registration")
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @GetMapping("registration1")
    public String showRegistration1Form(Model model) {
        return "registration1";
    }

    @PostMapping("registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDTO userDto,
                                      BindingResult result, @RequestParam("file") MultipartFile file) {

        User existing = userService.findByUsername(userDto.getUserName());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
            return "registration";
        }

        if (result.hasErrors()) {
            return "registration";
        }
        notificationService.sendRegistrationNotification(new NotificationDTO());
        storageService.store(file);
        userDto.setImgName(file.getOriginalFilename());
        userService.save(userDto);
        return "redirect:/login?success";
    }
    @PostMapping("registration1")
    public String registerUserAccount1(@ModelAttribute("user") @Valid UserRegistrationDTO userDto,
                                      BindingResult result, @RequestParam("file") MultipartFile file) {

        User existing = userService.findByUsername(userDto.getUserName());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
            return "registration1";
        }

        if (result.hasErrors()) {
            return "registration1";
        }
        notificationService.sendRegistrationNotification(new NotificationDTO());
        storageService.store(file);
        userDto.setImgName(file.getOriginalFilename());
        userService.save(userDto);
        return "redirect:/login?success";
    }

}




