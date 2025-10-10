package org.park.tobyspring.splearn.application.member;

import lombok.RequiredArgsConstructor;
import org.park.tobyspring.splearn.application.member.provided.MemberFinder;
import org.park.tobyspring.splearn.application.member.required.MemberRepository;
import org.park.tobyspring.splearn.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

  private final MemberRepository memberRepository;

  @Override
  public Member find(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(
        () -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + memberId)
    );
  }
}
