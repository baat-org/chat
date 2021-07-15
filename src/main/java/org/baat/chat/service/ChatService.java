package org.baat.chat.service;


import org.baat.chat.repository.ChatRepository;
import org.baat.core.transfer.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Service
@Validated
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    public ChatMessage createChatMessage(@Valid final ChatMessage chatMessage) {
    }

    public List<ChatMessage> getChats(@Positive final Long channelId, @Positive final Long firstUserId, @Positive final Long secondUserId) {
        //TODO Paginate

        if (channelId != null) {

        } else if (firstUserId != null && secondUserId != null) {

        } else {
            throw new IllegalArgumentException("Incompatible parameters passed");
        }
        return null;
    }
}
