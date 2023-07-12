package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.auth.Role;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글 조회에 성공한다.")
        public void 게시글_조회() throws Exception {

            //given
            final Member member = Member.createMember(
                    new MemberSignupRequestDto(
                            "게시글조회성공@email.com",
                            "pass12",
                            "pass12",
                            "name",
                            "phoneNumber",
                            Role.GENERAL
                    )
            );
            memberRepository.save(member);

            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            boardRepository.save(board);


            //when
            final Board findBoard = boardRepository.findById(board.getId()).get();

            //then
            Assertions.assertThat(findBoard.getTitle()).isEqualTo("title");
            Assertions.assertThat(findBoard.getContent()).isEqualTo("content");
        }
    }
}