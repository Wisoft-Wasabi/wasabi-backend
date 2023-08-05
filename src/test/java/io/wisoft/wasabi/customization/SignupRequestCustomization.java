package io.wisoft.wasabi.customization;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.member.Part;

import java.util.Random;

public class SignupRequestCustomization implements Customizer {
    @Override
    public ObjectGenerator customize(final ObjectGenerator generator) {
        return ((query, context) -> query.getType().equals(SignupRequest.class)
                ? new ObjectContainer(factory(context))
                : generator.generate(query, context));
    }

    private SignupRequest factory(final ObjectGenerationContext context) {
        final Random random = new Random();
        final String email = new StringBuilder()
                .append("test")
                .append(random.nextInt(1_000_000))
                .append("@gmail.com")
                .toString();

        final String password =  new StringBuilder()
                .append("pass")
                .append(random.nextInt(1_000_000))
                .append("123")
                .toString();

        final String name = new StringBuilder()
                .append("name")
                .append(random.nextInt(1_000_000))
                .toString();

        return new SignupRequest(
                email,
                password,
                password,
                name,
                "phoneNumber",
                "referenceUrl",
                Part.BACKEND,
                "wisoft",
                "호롱이 먹여 살리기"
        );
    }
}
