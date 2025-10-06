package org.park.tobyspring.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  void createMember() {
    Member member = new Member("woojin@splearn.app", "woojin", "hashedPassword");

    Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

}
