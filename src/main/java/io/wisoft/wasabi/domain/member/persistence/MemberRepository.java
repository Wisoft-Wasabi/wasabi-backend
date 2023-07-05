package io.wisoft.wasabi.domain.member.persistence;

import io.wisoft.wasabi.domain.member.persistence.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
