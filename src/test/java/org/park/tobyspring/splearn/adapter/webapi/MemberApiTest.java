package org.park.tobyspring.splearn.adapter.webapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.application.member.provided.MemberRegister;
import org.park.tobyspring.splearn.domain.member.Member;
import org.park.tobyspring.splearn.domain.member.MemberFixture;
import org.park.tobyspring.splearn.domain.member.MemberRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(MemberApi.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberApiTest {

  @MockitoBean
  private MemberRegister memberRegister;

  @Autowired
  MockMvcTester mockMvcTester;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void register() throws JsonProcessingException {
    Member member = MemberFixture.createMember(1L);
    when(memberRegister.register(any())).thenReturn(member);

    MemberRegisterRequest request = MemberFixture.createRequest();
    String requestJson = objectMapper.writeValueAsString(request);

    assertThat(mockMvcTester.post()
        .uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
        .with(csrf())
    ).hasStatusOk()
        .bodyJson()
        .extractingPath("$.memberId").isEqualTo(1);

    verify(memberRegister).register(request);
  }

  @Test
  void registerFail() throws JsonProcessingException {
    MemberRegisterRequest request = MemberFixture.createRequestWithEmail("invalid email");
    String requestJson = objectMapper.writeValueAsString(request);

    assertThat(mockMvcTester.post()
        .uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
        .with(csrf())
    ).hasStatus(HttpStatus.BAD_REQUEST);
  }
}
