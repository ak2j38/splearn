package org.park.tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.park.tobyspring.splearn.application.provided.MemberRegister;
import org.park.tobyspring.splearn.application.required.EmailSender;
import org.park.tobyspring.splearn.application.required.MemberRepository;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberRegisterRequest;
import org.park.tobyspring.splearn.domain.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

  private final MemberRepository memberRepository;
  private final EmailSender emailSender;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Member register(MemberRegisterRequest registerRequest) {
    // check

    Member member = Member.register(registerRequest, passwordEncoder);

    memberRepository.save(member);

    emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");

    return member;
  }
}
