package io.wisoft.wasabi.domain.member.application;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

        @DisplayName("이메일로 회원의 존재 여부를 확인할 수 있다.")
        @ParameterizedTest
        @AutoSource
        void exists_by_email_success(final Member member) {

            //given
            memberRepository.save(member);

            //when
            final var result = memberRepository.existsByEmail(member.getEmail());

            //then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("이메일로 회원 찾기")
    class FindMemberByEmail {

        @DisplayName("이메일로 회원 정보를 조회할 수 있다.")
        @ParameterizedTest
        @AutoSource
        void find_by_email_success(final Member member) {

            //given
            final var expected = memberRepository.save(member);

            //when
            final var result = memberRepository.findMemberByEmail(member.getEmail())
                    .orElseThrow();

            //then
            assertThat(result.getId()).isEqualTo(expected.getId());
        }
    }

    @Nested
    @DisplayName("회원 아이디 리스트로 회원 가입 요청 삭제")
    class DeleteAllByMemberIds {

        @DisplayName("회원 아이디 리스트로 회원 가입 요청(활성화가 안된 회원 정보)를 삭제할 수 있다.")
        @ParameterizedTest
        @AutoSource
        void delete_all_by_member_ids(final List<Member> members) {

            // given
            memberRepository.saveAll(members);
            final List<Long> ids =
                members.stream()
                    .map(Member::getId)
                    .toList();

            // when
            final var result = memberRepository.deleteAllByMemberIds(ids);

            // then
            assertThat(result).isEqualTo(members.size());
        }
    }
}