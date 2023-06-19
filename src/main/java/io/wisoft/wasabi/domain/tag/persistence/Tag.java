package io.wisoft.wasabi.domain.tag.persistence;

import io.wisoft.wasabi.domain.usage.persistence.Usage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Tag {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag")
    private Set<Usage> usages = new HashSet<>();

    public static Tag createTag(final String name) {
        final Tag tag = new Tag();
        tag.name = name;
        return tag;
    }
}
