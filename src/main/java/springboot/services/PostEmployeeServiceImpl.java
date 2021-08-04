package springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.models.DTO.PostEmployeeDTO;
import springboot.models.PostEmployee;
import springboot.models.User;
import springboot.repositories.PostEmplyeeRepository;
import springboot.services.base.PostEmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostEmployeeServiceImpl implements PostEmployeeService {

    @Autowired
    private PostEmplyeeRepository postEmplyeeRepository;

    public PostEmployeeServiceImpl(){}


    @Override
    public List<PostEmployee> listAllPosts() {
        return postEmplyeeRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        postEmplyeeRepository.deleteById(Long.parseLong(id));
    }

    @Override
    public List<PostEmployee> listByEmployee(String id){
        List<PostEmployee> userPosts = new ArrayList<>();
        for (PostEmployee p : postEmplyeeRepository.findAll()){
            if(p.getUser().getId().toString().equals(id))
            {
                userPosts.add(p);
            }
        }
        return userPosts;
    }

    @Override
    public void update(String id, PostEmployeeDTO post) {
        Optional<PostEmployee> opt = postEmplyeeRepository.findById(Long.parseLong(id));
        if(opt.isPresent())
        {
            PostEmployee p = opt.get();

            p.setStartDate(post.getStartDate());
            p.setEndDate(post.getEndDate());
            p.setUserFullName(post.getUserFullName());
            p.setUser(p.getUser());
            p.setLocation(post.getLocation());
            p.setEducation(post.getEducation());
            p.setCategory(post.getCategory());
            p.setDescription(post.getDescription());
            p.setImgName(post.getImgName());
        }
        postEmplyeeRepository.save(opt.get());
    }

    @Override
    public void closeById(String id) {
        postEmplyeeRepository.deleteById(Long.parseLong(id));
    }

    @Override
    public PostEmployee findById(String id){
        return  postEmplyeeRepository.findById(Integer.parseInt(id));
    }

    @Override
    public PostEmployee findByUser(User user) {

        List<PostEmployee> list = postEmplyeeRepository.findAll();
        for (PostEmployee p : list){
            if(p.getUser().getId().toString().equals(user.getId().toString()))
            {
               return p;
            }
        }
        return null;
    }

    @Override
    public void deleteAllByUser(String id, List<PostEmployee> list){
        for (int i = 0; i < list.size();i++) {
            deleteById(list.get(i).getId().toString());
        }
    }

    @Override
    public PostEmployee save(PostEmployeeDTO post, User user) {
        PostEmployee p = new PostEmployee();

        p.setStartDate(post.getStartDate());
        p.setEndDate(post.getEndDate());
        p.setUserFullName(post.getUserFullName());
        p.setUser(user);
        p.setLocation(post.getLocation());
        p.setEducation(post.getEducation());
        p.setCategory(post.getCategory());
        p.setDescription(post.getDescription());
        p.setPrice(Double.parseDouble(post.getPrice()));
        p.setImgName(post.getImgName());
        p.setActive(true);

        return postEmplyeeRepository.save(p);
    }

    @Override
    public Page<PostEmployee> getPaginatedPosts(Pageable pageable) {
        return postEmplyeeRepository.findAll(pageable);
    }

    @Override
    public Page<PostEmployee> getPaginatedPostsofUser(User user, Pageable pageable){
       return postEmplyeeRepository.findAllByUser(user, pageable);
    }

}
