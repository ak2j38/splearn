package org.park.tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.SplearnTestConfiguration;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberFixture;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(
    MemberFinder memberFinder,
    MemberRegister memberRegister,
    EntityManager entityManager
) {

  @Test
  void find() {
    Member member = memberRegister.register(MemberFixture.createRequest());
    entityManager.flush();
    entityManager.clear();

    Member found = memberFinder.find(member.getId());

    assertThat(member.getId()).isEqualTo(found.getId());
  }

  @Test
  void findFail() {
    assertThatThrownBy(() -> memberFinder.find(100L))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
