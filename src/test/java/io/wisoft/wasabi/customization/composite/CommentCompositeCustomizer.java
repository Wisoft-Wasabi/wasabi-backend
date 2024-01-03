package io.wisoft.wasabi.customization.composite;

import autoparams.customization.CompositeCustomizer;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.customization.NotSaveCommentCustomization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;

public class CommentCompositeCustomizer extends CompositeCustomizer {

    public CommentCompositeCustomizer() {
        super(
                new NotSaveMemberCustomization(),
                new NotSaveBoardCustomization(),
                new NotSaveCommentCustomization()
        );
    }
}
