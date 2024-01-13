package io.wisoft.wasabi.customization;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import io.wisoft.wasabi.domain.tag.persistence.Tag;

public class NotSaveTagCustomization implements Customizer {

    @Override
    public ObjectGenerator customize(final ObjectGenerator generator) {
        return ((query, context) -> query.getType().equals(Tag.class)
                ? new ObjectContainer(factory())
                : generator.generate(query, context));
    }

    static Tag factory() {

        return new Tag("tag");
    }
}
