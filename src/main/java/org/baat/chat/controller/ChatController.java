package org.baat.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.baat.chat.application.ChatApplication;
import org.baat.core.transfer.chat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private RabbitTemplate wsChatMessagePublisher;

    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void handleMessage(@RequestBody final ChatMessage chatMessage) throws IllegalAccessException, JsonProcessingException {
        if (chatMessage == null) {
            throw new IllegalArgumentException("chatMessage must be passed");
        }
        if (StringUtils.isEmpty(chatMessage.getSenderUserToken())) {
            throw new IllegalArgumentException("sender User Token must be passed");
        }
        if (StringUtils.isEmpty(chatMessage.getTextMessage())) {
            throw new IllegalArgumentException("text message must be passed");
        }
        if (chatMessage.getRecipientChannelId() == null && chatMessage.getRecipientUserId() == null) {
            throw new IllegalArgumentException("recipient channel Id or user Id must be passed");
        }

        LOGGER.info("Got a message: {}", chatMessage.getTextMessage());

        final String rawChatWSMessage = createRawChatWSMessage(chatMessage);

        if (rawChatWSMessage != null) {
            wsChatMessagePublisher.convertAndSend(ChatApplication.CHAT_WS_EXCHANGE_NAME, "", rawChatWSMessage);
        }
    }

    private String createRawChatWSMessage(final ChatMessage chatMessage) throws JsonProcessingException {
        //TODO handle channelId as well
        //TODO store message for the receiver (even if no token)
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(chatMessage);
    }
}