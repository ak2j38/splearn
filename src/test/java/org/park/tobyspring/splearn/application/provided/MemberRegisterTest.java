package org.park.tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.SplearnTestConfiguration;
import org.park.tobyspring.splearn.domain.DuplicateEmailException;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberFixture;
import org.park.tobyspring.splearn.domain.MemberRegisterRequest;
import org.park.tobyspring.splearn.domain.MemberStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(
    MemberRegister memberRegister,
    EntityManager entityManager
) {

  @Test
  void register() {
    Member member = memberRegister.register(MemberFixture.createRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void duplicateEmailFail() {
    memberRegister.register(MemberFixture.createRequest());

    assertThatThrownBy(() -> memberRegister.register(MemberFixture.createRequest()))
        .isInstanceOf(DuplicateEmailException.class);
  }

  @Test
  void activate() {
    Member member = memberRegister.register(MemberFixture.createRequest());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.activate(member.getId());
    entityManager.flush();

    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTVIE);
  }

  @Test
  void memberRegisterRequestFail() {
    extracted(new MemberRegisterRequest("woojin@splearn.app", "woojin", "123456"));
    extracted(new MemberRegisterRequest("woojin@splearn.app", "woojinaaaaaaaaaaaaaaaaa", "123456"));
    extracted(new MemberRegisterRequest("woojinsplearn.app", "woojin", "123456789"));
  }

  private void extracted(MemberRegisterRequest request) {
    assertThatThrownBy(() -> memberRegister.register(request))
        .isInstanceOf(ConstraintViolationException.class);
  }
}
