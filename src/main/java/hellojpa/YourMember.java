package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "MBR")
public class YourMember {

    @Id
    private Long id;

    @Column(name = "name", updatable = false)   // updatable 이 false 면 해당 컬럼은 강제로 update 치지 않는 이상 update 되지 않는다.
    private String username;

    /*
        유니크 키를 주고 싶은 경우
        @Column(unique = true) 해도 되긴 하는데 그러면 유니크 키 이름이 랜덤한 문자열로 지정되기 때문에
        해당 유니크 키에서 에러 발생 시 한눈에 식별이 어렵다는 단점이 존재한다.
        따라서 @Column 어노테이션 옆에 유니크 키를 명시하는 방식보다,
        @Table(uniqueConstraints = ) 방식이 더 선호된다.
     */
    private Integer age;

    @Enumerated(EnumType.ORDINAL)
    private RoleType roleType;

    /*
        Enumerated 의 디폴트는 ORDINAL 인데 사용하지 않는 것을 권장한다
        왜냐하면 운영 도중에 EnumType 에 새로운 타입이 추가된 경우,
        기존의 숫자값과 뒤죽박죽 되어버리는 참사가 발생한다..
        그러니 STRING 타입을 권장!
     */

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)   // @Temporal 은 날짜타입 매핑
    private Date lastModifiedDate;

    /*
        @Temporal
        DATE 는 date 타입과 매핑되는 날짜만 표기되는 타입 (2013-10-11)
        TIME 은 time 타입과 매핑되는 시간만 표기되는 타입 (10:32:15)
        TIMESTAMP 는 timestamp 타입과 매핑되는 날짜, 시간이 모두 표기되는 타입 (2013-01-11 23:12:10)

        날짜타입 매핑은 위와 같이 @Temporal 을 사용해도 되지만
        필드 타입을 LocalDate, LocalDateTime 으로 하는 경우 @Temporal 이 생략 가능하다.
        (예시: 아래 createDate2,3)
     */

    private LocalDate createDate2;
    private LocalDateTime createDate3;
    @Lob
    private String description;  // varchar 를 넘는 큰 데이터를 쓸때는 Lob으로 지정 (DB의 BLOB, CLOB으로 매핑)

    /*
        @Lob 은 부가적으로 지정해줄 수 있는 속성은 없다.
        매핑하는 필드 타입이 문자면 CLOB, 나머지는 BLOB 으로 매핑된다.

        CLOB : String, char[], java.sql.CLOB
        BLOB : byte[], java.sql.BLOB
     */

    @Transient
    private int temp;       // @Transient 는 DB 컬럼으로 매핑하고 싶지 않을 때 사용, DB에 저장 및 조회 되지 않으며 주로 메모리 상에서 임시로 어떤 값을 보관하고 싶을 때.

    public YourMember() {

    }
}
