package org.park.tobyspring.splearn.adapter.webapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Test;
import org.park.tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import org.park.tobyspring.splearn.application.member.provided.MemberRegister;
import org.park.tobyspring.splearn.application.member.required.MemberRepository;
import org.park.tobyspring.splearn.domain.member.Member;
import org.park.tobyspring.splearn.domain.member.MemberFixture;
import org.park.tobyspring.splearn.domain.member.MemberRegisterRequest;
import org.park.tobyspring.splearn.domain.member.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class MemberApiTest {

  @Autowired
  MockMvcTester mockMvcTester;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  MemberRegister memberRegister;

  @Test
  void register() throws JsonProcessingException, UnsupportedEncodingException {
    MemberRegisterRequest request = MemberFixture.createRequest();
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result = mockMvcTester.post()
        .uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
        .with(csrf())
        .exchange();

    assertThat(result).hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.memberId", value -> assertThat(value).isNotNull())
        .hasPathSatisfying("$.email", value -> assertThat(value).isEqualTo(request.email()));

    MemberRegisterResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);
    Member member = memberRepository.findById(response.memberId()).orElseThrow();

    assertThat(member.getEmail().address()).isEqualTo(request.email());
    assertThat(member.getNickname()).isEqualTo(request.nickname());
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void duplicateEmail() throws JsonProcessingException {
    memberRegister.register(MemberFixture.createRequest());

    MemberRegisterRequest request = MemberFixture.createRequest();
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result = mockMvcTester.post()
        .uri("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
        .with(csrf())
        .exchange();

    assertThat(result)
        .apply(print())
        .hasStatus(HttpStatus.CONFLICT);
  }
}
