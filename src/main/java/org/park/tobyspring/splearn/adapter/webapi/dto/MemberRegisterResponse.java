package org.park.tobyspring.splearn.adapter.webapi.dto;

import org.park.tobyspring.splearn.domain.member.Member;

public record MemberRegisterResponse(
    Long memberId,
    String emailAddress
) {

  public static MemberRegisterResponse of(Member member) {
    return new MemberRegisterResponse(member.getId(), member.getEmail().address());
  }
}
