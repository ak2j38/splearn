package org.park.tobyspring.splearn;

import org.park.tobyspring.splearn.application.member.required.EmailSender;
import org.park.tobyspring.splearn.domain.member.MemberFixture;
import org.park.tobyspring.splearn.domain.member.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {

  @Bean
  public EmailSender emailSender() {
    return (email, subject, body) ->
        System.out.println("Sending email to: " + email);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return MemberFixture.createPasswordEncoder();
  }
}
