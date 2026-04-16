package com.example.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MessageController.class)
@ExtendWith(MockitoExtension.class)
class MessageControllerTests {

	@MockitoBean
	JmsClient jmsClient;

	@Mock
	JmsClient.OperationSpec operationSpec;

	@Autowired
	MockMvcTester mvc;

	@BeforeEach
	void setUp() {
		given(this.jmsClient.destination("sample.queue")).willReturn(this.operationSpec);
	}

	@Test
	void givenValidRequestThenShouldReturnOk() {
		// given
		var request = post("/message")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
							"message": "test"
						}
						""");
		// when
		var result = this.mvc.perform(request);
		// then
		assertThat(result).hasStatusOk();
	}

}
