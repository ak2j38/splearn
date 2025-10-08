package org.park.tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.park.tobyspring.splearn.application.MemberService;
import org.park.tobyspring.splearn.application.required.EmailSender;
import org.park.tobyspring.splearn.application.required.MemberRepository;
import org.park.tobyspring.splearn.domain.Email;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberFixture;
import org.park.tobyspring.splearn.domain.MemberStatus;
import org.springframework.test.util.ReflectionTestUtils;

class MemberRegisterManualTest {

  @Test
  void registerTestStub() {
    MemberRegister register = new MemberService(
        new MemberRepositoryStub(),
        new EmailSenderStub(),
        MemberFixture.createPasswordEncoder()
    );

    Member member = register.register(MemberFixture.createRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void registerTestMock() {
    EmailSenderMock emailSenderMock = new EmailSenderMock();
    MemberRegister register = new MemberService(
        new MemberRepositoryStub(),
        emailSenderMock,
        MemberFixture.createPasswordEncoder()
    );

    Member member = register.register(MemberFixture.createRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    assertThat(emailSenderMock.tos).hasSize(1);
    assertThat(emailSenderMock.tos.getFirst()).isEqualTo(member.getEmail());
  }

  @Test
  void registerTestMockito() {
    EmailSenderMock emailSenderMock = Mockito.mock(EmailSenderMock.class);
    MemberRegister register = new MemberService(
        new MemberRepositoryStub(),
        emailSenderMock,
        MemberFixture.createPasswordEncoder()
    );

    Member member = register.register(MemberFixture.createRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
  }

  static class MemberRepositoryStub implements MemberRepository {

    @Override
    public Member save(Member member) {
      ReflectionTestUtils.setField(member, "id", 1L);
      return member;
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
      return Optional.empty();
    }
  }

  static class MemberRepositoryMock implements MemberRepository {

    @Override
    public Member save(Member member) {
      ReflectionTestUtils.setField(member, "id", 1L);
      return member;
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
      return Optional.empty();
    }
  }

  static class EmailSenderStub implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
    }
  }

  static class EmailSenderMock implements EmailSender {

    List<Email> tos = new ArrayList<>();

    @Override
    public void send(Email email, String subject, String body) {
      tos.add(email);
    }
  }
}
