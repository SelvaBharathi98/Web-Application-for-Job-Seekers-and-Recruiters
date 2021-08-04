package springboot.models;
import javax.persistence.*;


@Entity
@Table(name="file")
public class File {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn
    private User user;

    @Column(name = "cand_id")
    private int cand_id;

    public File(){}

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCand_id() {
        return cand_id;
    }

    public void setCand_id(int cand_id) {
        this.cand_id = cand_id;
    }

    public void setType(String type) {
        this.type = type;
    }
}