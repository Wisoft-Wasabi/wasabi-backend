package io.wisoft.wasabi.domain.tag;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveTagCustomization;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TagRepository tagRepository;

    @Nested
    @DisplayName("태그 생성")
    class CreateTag {

        @DisplayName("게시글 작성시 태그가 정상적으로 저장된다.")
        @ParameterizedTest
        @AutoSource
        void create_tag(final Tag tag) {

            // given

            // when
            final var savedTag = tagRepository.save(tag);

            // then
            assertThat(savedTag).isEqualTo(tag);
        }
    }

    @Nested
    @DisplayName("태그 조회")
    class ReadTag {

        @DisplayName("태그 이름으로 조회시 이름에 맞는 태그가 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveTagCustomization.class)
        void read_tag(final Tag tag) {

            // given
            em.persist(tag);

            // when
            final var savedTag = tagRepository.findByName("tag").get();

            // then
            assertThat(savedTag).isEqualTo(tag);
        }
    }
}