package com.icarus.batch;

import com.icarus.batch.board.repository.BoardRepository;
import com.icarus.batch.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
@Component
public class LocalCommandLineRunner implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @PersistenceContext(unitName = "member")
    private EntityManager entityManager;

    @Transactional(transactionManager = "memberTransactionManager")
    @Override
    public void run(String... args) throws Exception {
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1001, 'test@test.com', 'test1', 'test1', 'FACEBOOK', 'ACTIVE', 'VIP', '2016-03-01T00:00:00', '2018-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1002, 'test@test.com', 'test2', 'test2', 'FACEBOOK', 'ACTIVE', 'VIP', '2016-03-01T00:00:00', '2018-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1003, 'test@test.com', 'test3', 'test3', 'FACEBOOK', 'ACTIVE', 'VIP', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1004, 'test@test.com', 'test4', 'test4', 'FACEBOOK', 'ACTIVE', 'GOLD', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1005, 'test@test.com', 'test5', 'test5', 'FACEBOOK', 'ACTIVE', 'GOLD', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1006, 'test@test.com', 'test6', 'test6', 'FACEBOOK', 'ACTIVE', 'GOLD', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1007, 'test@test.com', 'test7', 'test7', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1008, 'test@test.com', 'test8', 'test8', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1009, 'test@test.com', 'test9', 'test9', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1010, 'test@test.com', 'test10', 'test10', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1011, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1012, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1013, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1014, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1015, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1016, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1017, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1018, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1019, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1020, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
        /*entityManager.createNativeQuery("insert into ex_member.member (idx, email, name, password, socialType, status, grade, createdDate, updatedDate) values (1021, 'test@test.com', 'test11', 'test11', 'FACEBOOK', 'ACTIVE', 'FAMILY', '2016-03-01T00:00:00', '2016-03-01T00:00:00')").executeUpdate();*/
    }
}
