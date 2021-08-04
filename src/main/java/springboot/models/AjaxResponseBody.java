package springboot.models;
import java.util.List;

public class AjaxResponseBody {

    String msg;
    List<Post> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Post> getResult() {
        return result;
    }

    public void setResult(List<Post> result) {
        this.result = result;
    }
}