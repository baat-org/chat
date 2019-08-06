package org.baat.chat.controller;

import org.baat.core.transfer.chat.ChatMessage;
import org.baat.core.transfer.chat.ChatWSMessage;
import org.baat.core.transfer.user.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.baat.chat.application.ChatApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Set;

@RestController
public class ChatController {

	@Autowired
	private RabbitTemplate wsChatMessagePublisher;

	@Value("${user_service_uri}")
	private String userServiceURI;

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

		if (!validUserToken(chatMessage.getSenderUserToken())) {
			throw new IllegalAccessException("Invalid sender user token passed");
		}

		final String rawChatWSMessage = createRawChatWSMessage(chatMessage);

		if (rawChatWSMessage != null) {
			wsChatMessagePublisher.convertAndSend( ChatApplication.CHAT_WS_EXCHANGE_NAME, "", rawChatWSMessage);
		}
	}

	private String createRawChatWSMessage(final ChatMessage chatMessage) throws JsonProcessingException {
		//TODO handle channelId as well
		//TODO store message for the receiver (even if no token)
		final Set<String> recipientUserTokens = findValidUserTokens(chatMessage.getRecipientUserId());
		if (CollectionUtils.isEmpty(recipientUserTokens)) {
			return null;
		}

		final UserInfo senderUserInfo = findUserForToken(chatMessage.getSenderUserToken());
		if (senderUserInfo == null) {
			throw new IllegalStateException("Invalid sender user token passed");
		}

		final ChatWSMessage chatWSMessage = new ChatWSMessage(senderUserInfo.getId(),
				chatMessage.getRecipientChannelId(), recipientUserTokens, chatMessage.getTextMessage());

		final ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(chatWSMessage);
	}

	@SuppressWarnings("unchecked")
	private Set<String> findValidUserTokens(final Long recipientUserId) {
		return new RestTemplate().getForObject(
				URI.create(userServiceURI + "/userTokens/" + recipientUserId), Set.class);
	}

	private UserInfo findUserForToken(final String userToken) {
		return new RestTemplate().getForObject(
				URI.create(userServiceURI + "/userForToken/" + userToken), UserInfo.class);
	}

	private boolean validUserToken(final String userToken) {
		try {
			return BooleanUtils.isTrue(new RestTemplate().getForObject(
					URI.create(userServiceURI + "/validateUserToken/" + userToken), Boolean.class));
		} catch (Exception exception) {
			//TODO error logging
			return false;
		}
	}
}