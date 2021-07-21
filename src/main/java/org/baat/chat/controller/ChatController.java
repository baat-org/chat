package org.baat.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.baat.chat.application.ChatApplication;
import org.baat.chat.service.ChatService;
import org.baat.core.transfer.chat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
public class ChatController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private RabbitTemplate wsChatMessagePublisher;

    @Autowired
    private ChatService chatService;

    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void storeAndSendChatMessage(@Valid @NotNull @RequestBody final ChatMessage chatMessage) throws JsonProcessingException {
        if (chatMessage.getRecipientChannelId() == null && chatMessage.getRecipientUserId() == null) {
            throw new IllegalArgumentException("Either of recipient channel Id or user Id must be passed");
        }

        chatService.storeChatMessage(chatMessage);
        wsChatMessagePublisher.convertAndSend(ChatApplication.CHAT_WS_EXCHANGE_NAME, "", createRawChatWSMessage(chatMessage));
    }

    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<ChatMessage> getChats(@RequestParam(name = "channelId", required = false) @Positive final Long channelId,
                                      @RequestParam(name = "firstUserId", required = false) @Positive final Long firstUserId,
                                      @RequestParam(name = "secondUserId", required = false) @Positive final Long secondUserId) {
        return chatService.getChats(channelId, firstUserId, secondUserId);
    }


    private static String createRawChatWSMessage(final ChatMessage chatMessage) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(chatMessage);
    }
}