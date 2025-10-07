package org.park.tobyspring.splearn.domain;

import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class Member {

  private String email;
  private String nickname;
  private String passwordHash;
  private MemberStatus status;

  private Member(String email, String nickname, String passwordHash) {
    this.email = Objects.requireNonNull(email);
    this.nickname = Objects.requireNonNull(nickname);
    this.passwordHash = Objects.requireNonNull(passwordHash);
    this.status = MemberStatus.PENDING;
  }

  public static Member create(String email, String nickname, String passwordHash, PasswordEncoder passwordEncoder) {
    return new Member(email, nickname, passwordEncoder.encode(passwordHash));
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
    this.nickname = Objects.requireNonNull(nickname);
  }

  public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
    this.passwordHash = passwordEncoder.encode(newPassword);
  }
}
