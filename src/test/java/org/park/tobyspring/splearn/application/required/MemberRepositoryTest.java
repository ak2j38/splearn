package org.park.tobyspring.splearn.application.required;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.park.tobyspring.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.park.tobyspring.splearn.domain.MemberFixture.createRequest;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.domain.Member;
import org.park.tobyspring.splearn.domain.MemberRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  @Test
  void registerMember() {
    Member member = Member.register(createRequest(), createPasswordEncoder());

    assertThat(member.getId()).isNull();

    memberRepository.save(member);

    assertThat(member.getId()).isNotNull();

    entityManager.flush();
  }
}
