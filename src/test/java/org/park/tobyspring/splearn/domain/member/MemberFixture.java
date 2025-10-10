package org.park.tobyspring.splearn.domain.member;

public class MemberFixture {

  public static MemberRegisterRequest createRequestWithEmail(String email) {
    return new MemberRegisterRequest(email, "woojin", "hashedPassword");
  }

  public static MemberRegisterRequest createRequest() {
    return createRequestWithEmail("woojin@splearn.app");
  }

  public static PasswordEncoder createPasswordEncoder() {
    return new PasswordEncoder() {
      @Override
      public String encode(String password) {
        return password.toUpperCase();
      }

      @Override
      public boolean matches(String password, String passwordHash) {
        return encode(password).equals(passwordHash);
      }
    };
  }
}
