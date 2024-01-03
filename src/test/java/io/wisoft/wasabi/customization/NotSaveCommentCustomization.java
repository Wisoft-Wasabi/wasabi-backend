package io.wisoft.wasabi.customization;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import io.wisoft.wasabi.customization.container.BoardStaticContainer;
import io.wisoft.wasabi.customization.container.MemberStaticContainer;
import io.wisoft.wasabi.domain.comment.Comment;

public class NotSaveCommentCustomization implements Customizer {

    @Override
    public ObjectGenerator customize(final ObjectGenerator generator) {
        return ((query, context) -> query.getType().equals(Comment.class)
                ? new ObjectContainer(factory(context))
                : generator.generate(query, context));
    }

    static Comment factory(final ObjectGenerationContext context) {

        return new Comment(
                "content",
                MemberStaticContainer.get(context.hashCode())
                        .orElse(NotSaveMemberCustomization.factory(context)),

                BoardStaticContainer.get(context.hashCode())
                        .orElse(NotSaveBoardCustomization.factory(context))
        );
    }
}
