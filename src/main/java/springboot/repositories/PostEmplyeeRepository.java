package springboot.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.models.PostEmployee;
import springboot.models.User;

import java.util.List;

@Repository
public interface PostEmplyeeRepository extends JpaRepository<PostEmployee, Long> {
    PostEmployee findById(int id);
    Page<PostEmployee> findAllByUser (User user, Pageable pageable);
}
