package org.park.tobyspring.splearn.domain;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;

@Entity
@Table(name = "MEMBER", uniqueConstraints = @UniqueConstraint(name = "UK_MEMBER_EMAIL_ADDRESS", columnNames = "email_address"))
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class Member extends AbtractEntity {

  @Embedded
  @NaturalId
  private Email email;

  @Column(length = 100, nullable = false)
  private String nickname;

  @Column(length = 200, nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(length = 50, nullable = false)
  private MemberStatus status;


  public static Member register(MemberRegisterRequest request, PasswordEncoder passwordEncoder) {
    Member member = new Member();

    member.email = new Email(request.email());
    member.nickname = requireNonNull(request.nickname());
    member.passwordHash = requireNonNull(passwordEncoder.encode(request.password()));
    member.status = MemberStatus.PENDING;

    return member;
  }

  public void activate() {
    Assert.state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

    this.status = MemberStatus.ACTVIE;
  }

  public void deactivate() {
    Assert.state(status == MemberStatus.ACTVIE, "ACTVIE 상태가 아닙니다.");

    this.status = MemberStatus.DEACTIVATED;
  }

  public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(password, this.passwordHash);
  }

  public void changeNickname(String nickname) {
    this.nickname = requireNonNull(nickname);
  }

  public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
    this.passwordHash = passwordEncoder.encode(requireNonNull(newPassword));
  }

  public boolean isActive() {
    return this.status == MemberStatus.ACTVIE;
  }
}
