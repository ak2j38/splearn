package org.park.tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  void createMember() {
    Member member = new Member("woojin@splearn.app", "woojin", "hashedPassword");

    Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void constructorNullCheck() {
    assertThatThrownBy(() -> new Member(null, "woojin", "hashedPassword"))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void activate() {
    Member member = new Member("woojin@splearn.com", "woojin", "hashedPassword");

    member.activate();

    Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTVIE);
  }

  @Test
  void activateFail() {
    Member member = new Member("woojin@splearn.com", "woojin", "hashedPassword");

    member.activate();

    assertThatThrownBy(() -> member.activate())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("PENDING 상태가 아닙니다.");
  }

  @Test
  void deactivate() {
    Member member = new Member("woojin@splearn.com", "woojin", "hashedPassword");

    member.activate();
    member.deactivate();

    Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
  }

  @Test
  void deactivateFail() {
    Member member = new Member("woojin@splearn.com", "woojin", "hashedPassword");

    assertThatThrownBy(member::deactivate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("ACTVIE 상태가 아닙니다.");

    member.activate();
    member.deactivate();

    assertThatThrownBy(member::deactivate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("ACTVIE 상태가 아닙니다.");
  }
}
