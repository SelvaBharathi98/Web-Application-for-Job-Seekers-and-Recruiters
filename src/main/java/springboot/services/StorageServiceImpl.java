package springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springboot.config.StorageProperties;
import springboot.models.File;
import springboot.models.StorageException;
import springboot.models.User;
import springboot.repositories.FileRepository;
import springboot.services.base.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private Path rootLocation;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
          throw new StorageException("Fail to load all",e);
        }
    }

    @Override
    public Stream<Path> loadByFileName(String filename){

        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation) && path.getFileName().toString().toLowerCase().equalsIgnoreCase(filename))
                    .map(this.rootLocation::relativize);
        }catch (IOException e){
            throw new StorageException("Fail to load all",e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException("Could not read file ");

            }
        }
        catch (MalformedURLException e) {
            throw new StorageException("Could not read file", e);
        }
    }

    @Override
    public void deleteAll() {
        //FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String findFilenameByUserCand(User user, Integer cand) {
        List<File> list = fileRepository.findAll();
        if(cand == null) return null;
        for(File f : list){
            if(f.getUser().getUsername().equalsIgnoreCase(user.getUserName()) && f.getCand_id() == cand){
                return f.getName();
            }
        }
        return null;
    }

    @Override
    public File update(String name,String type, User user, Integer cand){
        File file = fileRepository.findByName(name);
        if(file != null) {
            file.setType(type);
            file.setUser(user);
            file.setCand_id(cand);
            fileRepository.save(file);
        }
        return file;

    }

    @Override
    public void deleteByUser(User user){
        for(File f: fileRepository.findAll()){
            if(f.getUser().getUsername().equalsIgnoreCase(user.getUserName()))
                fileRepository.deleteById(f.getId());
        }
    }

    @Override
    public File save(String name, String type, User user, Integer cand) {

        File file = new File();
        file.setName(name);
        file.setType(type);
        file.setUser(user);
        file.setCand_id(cand);
        fileRepository.save(file);
        return file;
    }
}
