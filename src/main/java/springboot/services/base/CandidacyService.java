package springboot.services.base;

import springboot.models.Candidacy;
import springboot.models.User;
import springboot.models.enums.State;

import java.util.List;

public interface CandidacyService {
    List<Candidacy> listAllCand();
    void deleteById(Candidacy c);
    List<User> listApplicants(String id);
    Candidacy save(Candidacy cand);
    Candidacy findById(Integer id);
    int getLastCandId(Candidacy cand);
    Candidacy updateCand(Candidacy candidacy);
    boolean getState(String post_id, String user_id);
    int getApprovedCount(String user_id);
    boolean StateToBool(State st);
}
