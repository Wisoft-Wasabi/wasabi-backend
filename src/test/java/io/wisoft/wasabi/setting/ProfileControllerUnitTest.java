package io.wisoft.wasabi.setting;

import io.wisoft.wasabi.global.ProfileController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfileControllerUnitTest {

    @Test
    public void dev_profile이_조회된다() {
        // given
        final String expected = "dev";
        final MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expected);
        env.addActiveProfile("dev");
        env.addActiveProfile("prod");

        final ProfileController controller = new ProfileController(env);

        // when
        final String profile = controller.profile();

        // then
        assertThat(profile).isEqualTo(expected);
    }

    @Test
    public void dev_profile이_없으면_첫번째가_조회된다() {
        // given
        final String expected = "dev";
        final MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expected);
        env.addActiveProfile("prod");

        final ProfileController controller = new ProfileController(env);

        // when
        final String profile = controller.profile();

        // then
        assertThat(profile).isEqualTo(expected);
    }

    @Test
    public void active_profile이_없으면_default가_조회된다() {
        //given
        final String expected = "default";
        final MockEnvironment env = new MockEnvironment();
        final ProfileController controller = new ProfileController(env);

        // when
        final String profile = controller.profile();

        // then
        assertThat(profile).isEqualTo(expected);
    }
}
