package org.park.tobyspring.splearn.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Profile(
    @Column(length = 20) String address
) {

  private static final Pattern PROFILE_ADDRESS_PATTERN = Pattern.compile("[a-z0-9]+");

  public Profile {
    if (!PROFILE_ADDRESS_PATTERN.matcher(address).matches()) {
      throw new IllegalArgumentException("프로필 주소는 영문 소문자와 숫자만 가능합니다.");
    }

    if (address.length() > 15) {
      throw new IllegalArgumentException("프로필 주소는 15자를 넘을 수 없습니다.");
    }
  }

  public String url() {
    return "@" + address;
  }
}
