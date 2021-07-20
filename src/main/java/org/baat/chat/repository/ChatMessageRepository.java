package org.baat.chat.repository;

import org.baat.chat.repository.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByRecipientChannelIdOrderByUpdatedAtAsc(Long channelId);

    @Query("SELECT c FROM ChatMessageEntity c WHERE (u.senderUserId = ?1 and u.name = ?2) or (u.senderUserId = ?2 and u.name = ?1)")
    List<ChatMessageEntity> findAllDirectMessages(Long firstUserId, Long secondUserId);
}
