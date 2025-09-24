package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.reposirory.MemberDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository; // 주입받는 클래스 이름을 MemberRepository로 수정

    @Test
    @DisplayName("새로운 회원을 저장하고 ID로 조회하면 성공해야 한다.")
    void saveAndFindMemberTest() {
        // given (준비)
        MemberDomain newMember = new MemberDomain();
        newMember.setLoginId("testuser");
        newMember.setPassword("password123");

        // when (실행)
        memberRepository.save(newMember); // 사용하는 클래스 이름을 memberRepository로 수정
        Optional<MemberDomain> foundMemberOptional = memberRepository.findByLoginId("testuser"); // 사용하는 클래스 이름을 memberRepository로 수정

        // then (검증)
        assertThat(foundMemberOptional).isPresent();

        MemberDomain foundMember = foundMemberOptional.get();
        assertThat(foundMember.getLoginId()).isEqualTo(newMember.getLoginId());
        assertThat(foundMember.getPassword()).isEqualTo(newMember.getPassword());
    }
}