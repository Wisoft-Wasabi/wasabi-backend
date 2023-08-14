package io.wisoft.wasabi.customization;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import io.wisoft.wasabi.customization.container.MemberStaticContainer;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.Part;
import io.wisoft.wasabi.domain.member.Role;

import java.util.Random;

public class NotSaveMemberCustomization implements Customizer {
    @Override
    public ObjectGenerator customize(final ObjectGenerator generator) {
        return ((query, context) -> query.getType().equals(Member.class)
                ? new ObjectContainer(factory(context))
                : generator.generate(query, context));
    }

    private Member factory(final ObjectGenerationContext context) {
        final Random random = new Random();
        final String email = new StringBuilder()
                .append("test")
                .append(random.nextInt(1_000_000))
                .append("@gmail.com")
                .toString();
        final Member member = new Member(
                email,
                "test1234",
                "test",
                "010-1111-1111",
                false,
                Role.GENERAL,
                "",
                Part.UNDEFINED,
                "",
                ""
        );
        MemberStaticContainer.put(context.hashCode(), member);

        return member;
    }
}
