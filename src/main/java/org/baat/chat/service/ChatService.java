package org.baat.chat.service;


import org.baat.chat.repository.ChatMessageRepository;
import org.baat.chat.repository.entity.ChatMessageEntity;
import org.baat.core.transfer.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ChatService {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    public ChatMessage storeChatMessage(@Valid @NotNull final ChatMessage chatMessage) {
        if (chatMessage.getId() != null) {
            throw new IllegalArgumentException("Cannot pass chat message id when creating chat message");
        }

        final ChatMessageEntity chatMessageEntity = chatMessageRepository.save(new ChatMessageEntity(null,
                chatMessage.getSenderUserId(), chatMessage.getRecipientChannelId(),
                chatMessage.getRecipientUserId(), chatMessage.getTextMessage(),
                LocalDateTime.now(ZoneOffset.UTC)));
        return new ChatMessage(chatMessageEntity.getId(), chatMessageEntity.getSenderUserId(),
                chatMessageEntity.getRecipientChannelId(), chatMessageEntity.getRecipientUserId(),
                chatMessageEntity.getTextMessage(), chatMessageEntity.getUpdatedAt());
    }

    public List<ChatMessage> getChats(@Positive final Long channelId, @Positive final Long firstUserId, @Positive final Long secondUserId) {
        if (channelId != null) {
            return chatMessageRepository.findByRecipientChannelIdOrderByUpdatedAtAsc(channelId)
                    .stream()
                    .map(chatMessageEntity -> new ChatMessage(chatMessageEntity.getId(),
                            chatMessageEntity.getSenderUserId(), chatMessageEntity.getRecipientChannelId(),
                            chatMessageEntity.getRecipientUserId(), chatMessageEntity.getTextMessage(),
                            chatMessageEntity.getUpdatedAt())).collect(Collectors.toList());
        } else if (firstUserId != null && secondUserId != null) {
            return chatMessageRepository.findAllDirectMessages(firstUserId, secondUserId)
                    .stream()
                    .map(chatMessageEntity -> new ChatMessage(chatMessageEntity.getId(),
                            chatMessageEntity.getSenderUserId(), chatMessageEntity.getRecipientChannelId(),
                            chatMessageEntity.getRecipientUserId(), chatMessageEntity.getTextMessage(),
                            chatMessageEntity.getUpdatedAt())).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Incompatible parameters passed");
        }
    }
}
