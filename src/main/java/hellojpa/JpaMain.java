package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        // 엔티티 매니저 팩토리 생성
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");

        // 엔티티 매니저 생성
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 트랜잭션 시작
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {

            // 생성
            Member member = new Member();
            member.setId(2L);
            member.setName("이사람을 찾아라");     // 여기까지 비영속 상태

            entityManager.persist(member);  // 이제부터 영속 상태 (entity manager에 의해 관리가 되는 상태)
            // 이 때 DB에 저장되는 것 같아보이지만 사실은 그게 아님
            // 트랜잭션이 커밋되는 순간 DB에 쌓이게 됨

            Member targetMember = entityManager.find(Member.class, 2L);
            System.out.println("targetMember id = " + targetMember.getId());
            System.out.println("targetMember name = " + targetMember.getName());

            // 위의 targetMember 를 find 할 때는 SELECT 쿼리가 나가지 않는다.
            // 왜냐하면 entityManager.persist 하는 순간 1차 캐시에 저장이 되었고,
            // 똑같은 pk 값을 조회하고자 하였으므로 DB 가 아닌 1차 캐시에 있는 것을 가져왔기 때문에 쿼리가 나가지 않는다.

            /*

            find 순서
            1. 1차캐시에서 우선 조회한다
            2. 1차캐시에 없는 상황이면 DB에 가서 조회한다
            3. DB에서 가져온 값을 1차캐시에 저장한다
            4. 원하는 값 반환

             */

            // 아래의 두 find 를 실행 시
            // 여태까지의 영속성 컨텍스트에 pk가 3L인 대상은 1차캐시에 없었으므로
            // SELECT 문을 실행한 뒤
            // 1차 캐시에 저장한다.
            // 따라서 아래 findMember1,2 는 SELECT 문이 총 1번만이 실행된다.
            Member findMember1 = entityManager.find(Member.class, 3L);
            Member findMember2 = entityManager.find(Member.class, 3L);

            // 영속성 엔티티의 동일성 보장
            System.out.println(findMember1 == findMember2);
            // true 가 나온다.
            // 1차캐시가 존재하기 때문에 가능한 일
            // 1차 캐시로 반복 가능한 읽기 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공

            // 특정 레코드 1건 조회
            Member findMember = entityManager.find(Member.class, 2L);
            System.out.println("find member = " + findMember.getId());
            System.out.println("find member = " + findMember.getName());

            // 특정 조건을 만족하는 모든 레코드 조회
            List<Member> resultList = entityManager.createQuery("SELECT m FROM Member as m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            for (Member resultMember : resultList) {
                System.out.println("member = " + member.getName());
            }

            // 수정
            findMember.setName("바뀐 이름입니다");

            // 삭제
            entityManager.remove(findMember);


            // 쓰기지연
            // entityManager.persist(객체); 하는 즉시 insert SQL 을 치는게 아니라
            // SQL 쓰기지연 저장소에 차곡차곡 쌓아뒀다가
            // transaction.commit(); 하는 순간 쌓아둔 SQL 들을 날린다. (insert, udpate, delete)
            Member newMember1 = new Member(150L, "A");
            Member newMember2 = new Member(160L, "B");

            // 변경감지 (Dirty checking)
            // find 대상을 찾은 뒤
            // setter 를 통해 원하는 값으로 변경한 다음에
            // entityManager.persist 를 써야하는가 ? -> X
            // 그냥 값을 변경한 뒤 commit(); 만 해주면 됨!

            // 트랜잭션 저장
            entityTransaction.commit();

        } catch(Exception e) {
            // 에러 발생 시 트랜잭션 롤백
            entityTransaction.rollback();

        } finally {
            // 엔티티 매니저 종료
            entityManager.close();
        }

        // 모든 트랜잭션 종료 시 엔티티 매니저 팩토리 종료
        entityManagerFactory.close();
    }
}
