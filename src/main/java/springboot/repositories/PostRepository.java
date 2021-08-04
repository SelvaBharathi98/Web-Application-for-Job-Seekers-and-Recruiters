package springboot.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import springboot.models.Post;
import springboot.models.User;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    Post findById(int id);
    Page<Post> findAllByUser (User user, Pageable pageable);
    Page<Post> findAllByJobType(String jobtype, Pageable pageable);
}