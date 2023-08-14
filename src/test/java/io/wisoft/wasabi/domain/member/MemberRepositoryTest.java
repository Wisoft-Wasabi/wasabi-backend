package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("이메일로 회원 유무 확인")
    class ExistsByEmail {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일로 회원의 존재 여부를 확인할 수 있다.")
        void exists_by_email_success(final Member member) throws Exception {

            //given
            memberRepository.save(member);

            //when
            final boolean result = memberRepository.existsByEmail(member.getEmail());

            //then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("이메일로 회원 찾기")
    class FindMemberByEmail {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일로 회원 정보를 조회할 수 있다.")
        void find_by_email_success(final Member member) throws Exception {

            //given
            memberRepository.save(member);

            //when
            final Member findMember = memberRepository.findMemberByEmail(member.getEmail())
                    .orElseThrow();

            //then
            assertThat(findMember.getId()).isEqualTo(member.getId());
        }
    }
}