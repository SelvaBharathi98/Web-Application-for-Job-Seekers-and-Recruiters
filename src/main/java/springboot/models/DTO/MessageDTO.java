package springboot.models.DTO;

import javax.validation.constraints.NotEmpty;

public class MessageDTO {
    @NotEmpty
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
