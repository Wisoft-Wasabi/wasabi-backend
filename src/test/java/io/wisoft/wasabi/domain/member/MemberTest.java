package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MemberTest {

    @Nested
    @DisplayName("회원 개인 정보 수정")
    class UpdateInfo {

        @DisplayName("회원이 자신의 개인 정보를 수정시 정상적으로 수정되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void update_info(final Member member) {

            // given

            // when
            member.update(
                    "name",
                    "phoneNumber",
                    "referenceUrl",
                    Part.BACKEND,
                    "organization",
                    "motto"
            );

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(member.getUpdatedAt()).isAfter(member.getCreatedAt());
                softAssertions.assertThat(member.getPart()).isEqualTo(Part.BACKEND);
            });
        }
    }

    @Nested
    @DisplayName("회원 승인")
    class Activation {

        @DisplayName("관리자가 회원을 승인하면 activation 상태가 true로 바뀐다.")
        @ParameterizedTest
        @AutoSource
        void activation(final Member member) {

            // given

            // when
            member.activate();

            // then
            assertThat(member.isActivation()).isTrue();
        }
    }
}