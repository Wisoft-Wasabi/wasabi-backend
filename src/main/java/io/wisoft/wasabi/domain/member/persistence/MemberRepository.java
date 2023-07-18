package io.wisoft.wasabi.domain.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(final String email);
    boolean existsByEmail(final String email);
}
