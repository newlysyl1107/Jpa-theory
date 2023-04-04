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
            member.setName("이사람을 찾아라");

            entityManager.persist(member);

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
