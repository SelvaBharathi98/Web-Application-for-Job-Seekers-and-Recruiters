package springboot.services;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.models.Post;
import springboot.models.Role;
import springboot.models.User;
import springboot.repositories.UserRepository;
import springboot.models.DTO.UserRegistrationDTO;
import springboot.services.base.UserService;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByUsername(String username){
        for (User u : userRepository.findAll()) {
            if(u.getUserName().equals(username)|| u.getEmail().equals(username))return u;
        }
        return null;
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(Long.parseLong(id)).get();
    }

    public List<User> findByUserNameOrEmail(String username) {

        List<User> result = userRepository.findAll().stream()
                .filter(x -> x.getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
        return result;

    }

    @Override
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Post> listPostsOfUser(String username) {
         return findByUsername(username).getPostList();
    }

    @Override
    public List<Post> listPostsOfUserPerPage(String email, List<Post> pages) {
        List<Post> result = new ArrayList<>();
        for (Post p: pages) {
            if (p.getUser().getEmail().equals(email))
                result.add(p);
        }
        return result;
    }

    public User save(UserRegistrationDTO registration){
        User user = new User();
        user.setCompanyName(registration.getCompanyName());
        user.setUserName(registration.getUserName());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setGender(registration.getGender());
        user.setPhoneNo(registration.getPhoneNo());
        user.setQualification(registration.getQualification());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role(registration.getRole())));
        user.setDeleted(false);
        user.setImgName(registration.getImgName());
        return userRepository.save(user);
    }

    @Override
    public boolean containsRole(User user, String role) {
        for (Role r: user.getRoles()) {
            if(r.getName().equalsIgnoreCase(role)){
                return true;
            }
        }
        return  false;
    }

    @Override
    public void update(String id, User user, String filename) {
        Optional<User> opt = userRepository.findById(Long.parseLong(id));
        if(opt.isPresent())
        {
            User u = opt.get();
            u.setUserName(user.getUserName());
            u.setCompanyName(user.getCompanyName());
            u.setEmail(user.getEmail());
            u.setQualification(user.getQualification());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setGender(user.getGender());
            u.setPhoneNo(user.getPhoneNo());
            u.setPassword(passwordEncoder.encode(user.getPassword()));
            u.setImgName(filename);
            userRepository.save(u);
        }
    }

    @Override
    public void delete(User user) {
        user.setDeleted(true);
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}