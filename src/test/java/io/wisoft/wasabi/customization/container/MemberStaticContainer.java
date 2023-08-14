package io.wisoft.wasabi.customization.container;

import io.wisoft.wasabi.domain.member.Member;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberStaticContainer {

    public static final Map<Integer, Member> MEMBER_MAP = new ConcurrentHashMap<>();

    public static void put(final Integer key, final Member member) {
        MEMBER_MAP.put(key, member);
    }

    public static Member get(final Integer hashCode) {
        return MEMBER_MAP.get(hashCode);
    }

}
