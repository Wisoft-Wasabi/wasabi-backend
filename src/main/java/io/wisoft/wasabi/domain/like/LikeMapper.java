package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.member.Member;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {
    public Like registerLikeRequestToEntity(final Member member, final Board board) {

        return new Like(member, board);
    }

    RegisterLikeResponse entityToRegisterLikeResponse(final Like like) {

        return new RegisterLikeResponse(like.getId());
    }
}
