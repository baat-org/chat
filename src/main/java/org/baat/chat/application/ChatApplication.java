package org.baat.chat.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.baat")
public class ChatApplication {
	public final static String CHAT_WS_EXCHANGE_NAME = "chat-ws-message-exchange";

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}
}
