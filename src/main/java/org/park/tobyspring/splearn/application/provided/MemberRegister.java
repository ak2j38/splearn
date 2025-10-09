package org.park.tobyspring.splearn.application.provided;

import jakarta.validation.Valid;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberRegisterRequest;

/**
 * 회원의 등록과 관련된 기능을 제공한다
 */
public interface MemberRegister {

  Member register(@Valid MemberRegisterRequest registerRequest);

  Member activate(Long memberId);
}
