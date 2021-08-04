package springboot.services.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.models.DTO.PostDTO;
import springboot.models.Post;
import springboot.models.User;

import java.util.List;

public interface PostService {

    List<Post> listAllPosts();
    void deleteById(String id);
    List<Post> findByTopic(String topic);
    List<Post> listByEmployer(String id, List<Post> list);
    void update(String id,PostDTO post);
    void closeById(String id);
    Post findById(String id);
    Post save(PostDTO post, User user);
    Page<Post> getPaginatedPosts (Pageable pageable);
    Page<Post> getPaginatedPostsByJobType(String jobType,Pageable pageable);
    Page<Post> getPaginatedPostsofUser (User user,Pageable pageable);
}
