package io.wisoft.wasabi.domain.member;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(final String email);

    boolean existsByEmail(final String email);

    // Query 를 안짜는 방식
    Slice<Member> findMembersByActivationIsFalse(final Pageable pageable);

    @Query("SELECT member FROM Member member WHERE member.activation = false")
    Slice<Member> findMemberByUnactivated(final Pageable pageable);

    @Modifying
    @Query("UPDATE Member m SET m.activation = :newActivation WHERE m.id = :memberId")
    void updateActivationStatus(final Long memberId, final boolean newActivation);


}
