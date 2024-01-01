package io.wisoft.wasabi.domain.tag.application;

import io.wisoft.wasabi.domain.tag.persistence.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(final String name);
}
