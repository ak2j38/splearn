package org.park.tobyspring.splearn.adapter.integration;

import org.park.tobyspring.splearn.application.required.EmailSender;
import org.park.tobyspring.splearn.domain.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {

  @Override
  public void send(Email email, String subject, String body) {
    System.out.println("DummyEmailSender.send to " + email);
  }
}
