package io.wisoft.wasabi.customization.container;

import io.wisoft.wasabi.domain.member.persistence.Member;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemberStaticContainer {

    public static final Map<Integer, Member> MEMBER_MAP = new ConcurrentHashMap<>();

    public static void put(final Integer key, final Member member) {
        if (!MEMBER_MAP.containsKey(key)) {
            MEMBER_MAP.put(key, member);
        }
    }

    public static Optional<Member> get(final Integer key) {
        return Optional.ofNullable(MEMBER_MAP.get(key));
    }

}
