package org.park.tobyspring.splearn.domain.member;

public record MemberInfoUpdateRquest(
    String nickname,
    String profileAddress,
    String introduction
) {

}
