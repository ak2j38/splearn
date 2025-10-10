package org.park.tobyspring.splearn.domain.shared;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void equailty() {
    Email email1 = new Email("1234@splearn.com");
    Email email2 = new Email("1234@splearn.com");

    Assertions.assertThat(email1).isEqualTo(email2);
  }
}
