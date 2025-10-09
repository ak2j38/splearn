package org.park.tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.park.tobyspring.splearn.application.provided.MemberFinder;
import org.park.tobyspring.splearn.application.provided.MemberRegister;
import org.park.tobyspring.splearn.application.required.EmailSender;
import org.park.tobyspring.splearn.application.required.MemberRepository;
import org.park.tobyspring.splearn.domain.DuplicateEmailException;
import org.park.tobyspring.splearn.domain.Email;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberRegisterRequest;
import org.park.tobyspring.splearn.domain.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {

  private final MemberFinder memberFinder;
  private final MemberRepository memberRepository;
  private final EmailSender emailSender;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Member register(MemberRegisterRequest registerRequest) {
    checkDuplicateEmail(registerRequest);

    Member member = Member.register(registerRequest, passwordEncoder);

    memberRepository.save(member);

    sendWelcomeEmail(member);

    return member;
  }

  @Override
  public Member activate(Long memberId) {
    Member member = memberFinder.find(memberId);

    member.activate();

    return memberRepository.save(member);
  }

  private void sendWelcomeEmail(Member member) {
    emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
  }

  private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
    if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
      throw new DuplicateEmailException("동일한 이메일이 이미 존재합니다.");
    }
  }
}
