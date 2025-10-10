package org.park.tobyspring.splearn.application.member.required;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.park.tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;
import static org.park.tobyspring.splearn.domain.member.MemberFixture.createRequest;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.domain.member.Member;
import org.park.tobyspring.splearn.domain.member.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

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
    entityManager.clear();

    Member found = memberRepository.findById(member.getId()).orElseThrow();
    assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
    assertThat(found.getDetail().getRegisteredAt()).isNotNull();
  }

  @Test
  void duplicateEmailFail() {
    Member member1 = Member.register(createRequest(), createPasswordEncoder());
    memberRepository.save(member1);

    Member member2 = Member.register(createRequest(), createPasswordEncoder());
    assertThatThrownBy(() -> memberRepository.save(member2))
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
