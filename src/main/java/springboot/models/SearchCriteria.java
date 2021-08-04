package springboot.models;

import javax.validation.constraints.NotBlank;

public class SearchCriteria {

    @NotBlank(message = "post topic can't be empty!")
    String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}