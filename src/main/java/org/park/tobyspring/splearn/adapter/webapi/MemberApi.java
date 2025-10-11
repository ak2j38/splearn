package org.park.tobyspring.splearn.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.park.tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import org.park.tobyspring.splearn.application.member.provided.MemberRegister;
import org.park.tobyspring.splearn.domain.member.Member;
import org.park.tobyspring.splearn.domain.member.MemberRegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApi {

  private final MemberRegister memberRegister;

  @PostMapping("/api/members")
  public MemberRegisterResponse register(@Valid @RequestBody MemberRegisterRequest request) {
    Member member = memberRegister.register(request);

    return MemberRegisterResponse.of(member);
  }
}
