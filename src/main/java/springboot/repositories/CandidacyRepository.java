package springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.models.Candidacy;

@Repository
public interface CandidacyRepository extends JpaRepository<Candidacy, Long> {
    public Candidacy findById(int id);

}