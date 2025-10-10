package org.park.tobyspring.splearn.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.park.tobyspring.splearn.domain.AbtractEntity;
import org.springframework.util.Assert;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbtractEntity {

  @Embedded
  private Profile profile;

  @Column(columnDefinition = "TEXT")
  private String introduction;

  @Column(nullable = false)
  private LocalDateTime registeredAt;

  private LocalDateTime activatedAt;

  private LocalDateTime deactivatedAt;

  public static MemberDetail create() {
    MemberDetail detail = new MemberDetail();
    detail.registeredAt = LocalDateTime.now();

    return detail;
  }

  public void setActivatedAt() {
    Assert.isTrue(activatedAt == null, "이미 활성화된 회원입니다.");
    this.activatedAt = LocalDateTime.now();
  }

  public void deactivate() {
    Assert.isTrue(deactivatedAt == null, "이미 비활성화된 회원입니다.");
    this.deactivatedAt = LocalDateTime.now();
  }

  public void updateProfile(MemberInfoUpdateRquest request) {
    this.profile = new Profile(request.profileAddress());
    this.introduction = Objects.requireNonNull(request.introduction());
  }
}
