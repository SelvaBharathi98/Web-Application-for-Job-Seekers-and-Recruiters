package springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.models.DTO.PostDTO;
import springboot.models.Post;
import springboot.models.User;
import springboot.repositories.PostRepository;
import springboot.services.base.PostService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("postServiceImpl")
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;


    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public PostServiceImpl(){}

    private List<Post> list;

    @Override
    public List<Post> listAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        postRepository.deleteById(Long.parseLong(id));
    }


    @Override
    public List<Post> findByTopic(String topic) {
        List<Post> result = null;
        result = postRepository.findAll().stream()
                .filter(x -> x.getTopic().equalsIgnoreCase(topic) && x.isActive())
                .collect(Collectors.toList());
        return result;
    }


    @Override
    public Post findById(String id) {

        List<Post> list = postRepository.findAll();

        for (Post p : list){
            if(p.getId() == Long.parseLong(id))
            {
                return p;
            }
        }

        return null;

    }

    @Override
    public List<Post> listByEmployer(String id, List<Post> list) {
        List<Post> userPosts = null;
         for (Post p : list){
             if(p.getUser().getId().toString().equals(id))
             {
                userPosts.add(p);
             }
         }
        return userPosts;
    }

    @Override
    public void update(String id, PostDTO post) {
     Optional<Post> opt = postRepository.findById(Long.parseLong(id));
     if(opt.isPresent())
     {
         Post p = opt.get();
         p.setDescription(post.getDescription());
         p.setTopic(post.getTopic());
         p.setLocation(post.getLocation());
         p.setJobType(post.getJobType());
         p.setExperience(post.getExperience());
         p.setSalary(post.getSalary());
         p.setDuration(post.getDuration());
         p.setImgName(post.getImgName());
     }
     postRepository.save(opt.get());
    }

    @Override
    public void closeById(String id) {
        postRepository.deleteById(Long.parseLong(id));
    }

    @Override
    public Post save(PostDTO post, User user) {
        Post p = new Post();
        p.setTopic(post.getTopic());
        p.setDescription(post.getDescription());
        p.setExperience(post.getExperience());
        p.setSalary(post.getSalary());
        p.setDuration(post.getDuration());
        p.setJobType(post.getJobType());
        p.setLocation(post.getLocation());
        p.setActive(true);
        p.setImgName(post.getImgName());
        p.setUser(user);
        return  postRepository.save(p);
    }

    @Override
    public Page<Post> getPaginatedPosts(Pageable pageable) {

        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getPaginatedPostsByJobType(String jobType,Pageable pageable) {
        return postRepository.findAllByJobType(jobType, pageable);
    }

    @Override
    public Page<Post> getPaginatedPostsofUser(User user, Pageable pageable) {
        return postRepository.findAllByUser(user, pageable);
    }

}
