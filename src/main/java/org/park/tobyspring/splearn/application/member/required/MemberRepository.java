package org.park.tobyspring.splearn.application.member.required;

import java.util.Optional;
import org.park.tobyspring.splearn.domain.member.Member;
import org.park.tobyspring.splearn.domain.shared.Email;
import org.springframework.data.repository.Repository;

/**
 * 회원 정보를 저장하거나 조회한다
 */
public interface MemberRepository extends Repository<Member, Long> {

  Member save(Member member);

  Optional<Member> findByEmail(Email email);

  Optional<Member> findById(Long memberId);
}
