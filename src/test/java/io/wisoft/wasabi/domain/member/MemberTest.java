package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MemberTest {

    @Nested
    @DisplayName("회원 개인 정보 수정")
    class UpdateInfo {

        @ParameterizedTest
        @AutoSource
        @DisplayName("회원이 자신의 개인 정보를 수정시 정상적으로 수정되어야 한다.")
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
            assertSoftly(softly -> {
                softly.assertThat(member.getUpdatedAt()).isAfter(member.getCreatedAt());
                softly.assertThat(member.getPart()).isEqualTo(Part.BACKEND);
            });
        }
    }
}