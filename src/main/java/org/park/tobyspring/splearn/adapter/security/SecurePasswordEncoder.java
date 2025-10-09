package org.park.tobyspring.splearn.adapter.security;

import org.park.tobyspring.splearn.domain.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurePasswordEncoder implements PasswordEncoder {

  private final BCryptPasswordEncoder bcrpytPasswordEncoder = new BCryptPasswordEncoder();

  @Override
  public String encode(String password) {
    return bcrpytPasswordEncoder.encode(password);
  }

  @Override
  public boolean matches(String password, String passwordHash) {
    return bcrpytPasswordEncoder.matches(password, passwordHash);
  }
}
