package springboot.services.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.models.DTO.PostEmployeeDTO;
import springboot.models.PostEmployee;
import springboot.models.User;

import java.util.List;

public interface PostEmployeeService {

    List<PostEmployee> listAllPosts();
    void deleteById(String id);
    List<PostEmployee> listByEmployee(String id);
    void update(String id, PostEmployeeDTO post);
    void closeById(String id);
    void deleteAllByUser(String id,  List<PostEmployee> list);
    PostEmployee findById(String id);
    PostEmployee findByUser(User user);
    PostEmployee save(PostEmployeeDTO post, User user);
    Page<PostEmployee> getPaginatedPosts (Pageable pageable);
    Page<PostEmployee> getPaginatedPostsofUser (User user,Pageable pageable);
}
