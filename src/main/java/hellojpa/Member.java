package hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {

    // @Entity 는 기본 생성자가 필수
    // 파라미터가 없는 public 또는 protected 생성자
    public Member() {

    }
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
