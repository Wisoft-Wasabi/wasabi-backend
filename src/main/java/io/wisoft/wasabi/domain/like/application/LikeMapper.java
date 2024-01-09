package io.wisoft.wasabi.domain.like.application;

import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.like.persistence.Like;
import io.wisoft.wasabi.domain.member.persistence.Member;

public class LikeMapper {
    static Like registerLikeRequestToEntity(final Member member, final Board board) {

        return new Like(member, board);
    }

    static RegisterLikeResponse entityToRegisterLikeResponse(final Like like) {

        return new RegisterLikeResponse(like.getId());
    }
}
