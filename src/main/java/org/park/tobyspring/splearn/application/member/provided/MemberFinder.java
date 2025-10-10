package org.park.tobyspring.splearn.application.member.provided;

import org.park.tobyspring.splearn.domain.member.Member;

/**
 * 회원을 조회한다
 */
public interface MemberFinder {

  Member find(Long memberId);
}
