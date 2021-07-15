package org.baat.chat.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.baat.chat.repository")
@EntityScan("org.baat.chat.repository.entity")
@ComponentScan("org.baat")
public class ChatApplication {
    public final static String CHAT_WS_EXCHANGE_NAME = "chat-ws-message-exchange";

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
