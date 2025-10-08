package org.park.tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.SplearnTestConfiguration;
import org.park.tobyspring.splearn.domain.DuplicateEmailException;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberFixture;
import org.park.tobyspring.splearn.domain.MemberStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(
    MemberRegister memberRegister
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
}
