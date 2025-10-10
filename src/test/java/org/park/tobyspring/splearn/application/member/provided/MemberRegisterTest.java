package org.park.tobyspring.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.SplearnTestConfiguration;
import org.park.tobyspring.splearn.domain.member.DuplicateEmailException;
import org.park.tobyspring.splearn.domain.member.Member;
import org.park.tobyspring.splearn.domain.member.MemberFixture;
import org.park.tobyspring.splearn.domain.member.MemberRegisterRequest;
import org.park.tobyspring.splearn.domain.member.MemberStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(
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
    checkValidation(new MemberRegisterRequest("woojin@splearn.app", "woojin", "123456"));
    checkValidation(new MemberRegisterRequest("woojin@splearn.app", "woojinaaaaaaaaaaaaaaaaa", "123456"));
    checkValidation(new MemberRegisterRequest("woojinsplearn.app", "woojin", "123456789"));
  }

  private void checkValidation(MemberRegisterRequest request) {
    assertThatThrownBy(() -> memberRegister.register(request))
        .isInstanceOf(ConstraintViolationException.class);
  }
}
