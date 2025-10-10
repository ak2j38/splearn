package org.park.tobyspring.splearn.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.park.tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;
import static org.park.tobyspring.splearn.domain.member.MemberFixture.createRequest;
import static org.park.tobyspring.splearn.domain.member.MemberFixture.createRequestWithEmail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

  Member member;
  PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    this.passwordEncoder = createPasswordEncoder();
    member = Member.register(createRequest(), passwordEncoder);
  }


  @Test
  void registerMember() {
    Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    assertThat(member.getDetail().getRegisteredAt()).isNotNull();
  }

  @Test
  void constructorNullCheck() {
    assertThatThrownBy(() -> Member.register(createRequestWithEmail(null), passwordEncoder))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void activate() {
    member.activate();

    Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTVIE);
  }

  @Test
  void activateFail() {
    member.activate();

    assertThatThrownBy(() -> member.activate())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("PENDING 상태가 아닙니다.");
  }

  @Test
  void deactivate() {
    member.activate();
    member.deactivate();

    Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
  }

  @Test
  void deactivateFail() {
    assertThatThrownBy(member::deactivate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("ACTVIE 상태가 아닙니다.");

    member.activate();
    member.deactivate();

    assertThatThrownBy(member::deactivate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("ACTVIE 상태가 아닙니다.");
  }

  @Test
  void verifyPassword() {
    Assertions.assertThat(member.verifyPassword("hashedPassword", passwordEncoder)).isTrue();
    Assertions.assertThat(member.verifyPassword("wrongPassword", passwordEncoder)).isFalse();
  }

  @Test
  void changeNickname() {
    assertThat(member.getNickname()).isEqualTo("woojin");

    member.changeNickname("newNickname");

    assertThat(member.getNickname()).isEqualTo("newNickname");
  }

  @Test
  void changePassword() {
    member.changePassword("verySecret", passwordEncoder);

    Assertions.assertThat(member.verifyPassword("verySecret", passwordEncoder)).isTrue();
  }

  @Test
  void isActive() {
    assertThat(member.isActive()).isFalse();

    member.activate();

    assertThat(member.isActive()).isTrue();

    member.deactivate();

    assertThat(member.isActive()).isFalse();
  }

  @Test
  void invalidEmail() {
    assertThatThrownBy(
        () -> Member.register(createRequestWithEmail("invalidEmail"), passwordEncoder)
    ).isInstanceOf(IllegalArgumentException.class);
  }
}
