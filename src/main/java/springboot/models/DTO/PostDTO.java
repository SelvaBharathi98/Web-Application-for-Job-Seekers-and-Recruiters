package springboot.models.DTO;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

public class PostDTO {
    @NotEmpty
    private String topic;

    @NotEmpty
    private String description;

    @NotEmpty
    private String location;


    private String experience;


    private String salary;

   
    private String duration;

    @NotEmpty
    private String jobType;

    private String imgName;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
   public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary=salary;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration=duration;
    }
    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}
