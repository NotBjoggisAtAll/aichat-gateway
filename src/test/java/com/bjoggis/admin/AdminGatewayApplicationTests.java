package com.bjoggis.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

@SpringBootTest
class AdminGatewayApplicationTests {

  @MockBean
  ReactiveClientRegistrationRepository clientRegistrationRepository;

  @Test
  void contextLoads() {
  }

}
