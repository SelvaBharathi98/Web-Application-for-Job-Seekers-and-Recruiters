package springboot.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.models.User;


@Repository
public interface UserRepository extends JpaRepository < User, Long > {
    User findByUsername(String username);
}