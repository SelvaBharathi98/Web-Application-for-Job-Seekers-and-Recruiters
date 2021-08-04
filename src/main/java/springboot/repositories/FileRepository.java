package springboot.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.models.File;


@Repository
public interface FileRepository extends JpaRepository<File, Long>{
    public File findByName(String name);
}