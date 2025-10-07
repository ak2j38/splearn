package org.park.tobyspring.splearn.domain;

import static java.util.Objects.requireNonNull;

import java.util.regex.Pattern;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class Member {

  private Email email;
  private String nickname;
  private String passwordHash;
  private MemberStatus status;

  private Member() {
  }

  public static Member create(MemberCreateRequest request, PasswordEncoder passwordEncoder) {
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
