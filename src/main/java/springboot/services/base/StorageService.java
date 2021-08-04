package springboot.services.base;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import springboot.models.File;
import springboot.models.User;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();
    void store(MultipartFile file);
    Stream<Path> loadAll();
    Path load(String filename);
    Resource loadAsResource(String filename);
    void deleteAll();
    void deleteByUser(User user);
    String findFilenameByUserCand(User user, Integer cand);
    Stream<Path> loadByFileName(String filename);
    File save(String name, String type, User user, Integer cand);
    File update(String name, String type, User user, Integer cand);
}