package io.wisoft.wasabi.customization.composite;

import autoparams.customization.CompositeCustomizer;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;

public class BoardCompositeCustomizer extends CompositeCustomizer {

    public BoardCompositeCustomizer() {
        super(
                new NotSaveMemberCustomization(),
                new NotSaveBoardCustomization()
        );
    }

}
