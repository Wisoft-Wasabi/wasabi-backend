package io.wisoft.wasabi.domain.member.persistence;

import io.wisoft.wasabi.domain.member.persistence.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(final String email);
}
