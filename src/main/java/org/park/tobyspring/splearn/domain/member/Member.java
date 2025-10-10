package org.park.tobyspring.splearn.domain.member;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.park.tobyspring.splearn.domain.AbtractEntity;
import org.park.tobyspring.splearn.domain.shared.Email;
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

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private MemberDetail detail;


  public static Member register(MemberRegisterRequest request, PasswordEncoder passwordEncoder) {
    Member member = new Member();

    member.email = new Email(request.email());
    member.nickname = requireNonNull(request.nickname());
    member.passwordHash = requireNonNull(passwordEncoder.encode(request.password()));
    member.status = MemberStatus.PENDING;

    member.detail = MemberDetail.create();

    return member;
  }

  public void activate() {
    Assert.state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

    this.status = MemberStatus.ACTVIE;
    this.detail.setActivatedAt();
  }

  public void deactivate() {
    Assert.state(status == MemberStatus.ACTVIE, "ACTVIE 상태가 아닙니다.");

    this.status = MemberStatus.DEACTIVATED;
    this.detail.deactivate();
  }

  public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(password, this.passwordHash);
  }

  public void changeNickname(String nickname) {
    this.nickname = requireNonNull(nickname);
  }

  public void updateInfo(MemberInfoUpdateRquest request) {
    this.nickname = Objects.requireNonNull(request.nickname());
    this.detail.updateProfile(request);
  }

  public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
    this.passwordHash = passwordEncoder.encode(requireNonNull(newPassword));
  }

  public boolean isActive() {
    return this.status == MemberStatus.ACTVIE;
  }
}
