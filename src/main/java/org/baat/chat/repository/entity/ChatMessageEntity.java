package org.baat.chat.repository.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "chat_message")
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderUserId;
    private Long recipientChannelId;
    private Long recipientUserId;
    private String textMessage;
    private LocalDateTime updatedAt;

    public ChatMessageEntity(Long id, Long senderUserId, Long recipientChannelId, Long recipientUserId, String textMessage, LocalDateTime updatedAt) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.recipientChannelId = recipientChannelId;
        this.recipientUserId = recipientUserId;
        this.textMessage = textMessage;
        this.updatedAt = updatedAt;
    }

    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "sender_user_id")
    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    @Column(name = "recipient_channel_id")
    public Long getRecipientChannelId() {
        return recipientChannelId;
    }

    public void setRecipientChannelId(Long recipientChannelId) {
        this.recipientChannelId = recipientChannelId;
    }

    @Column(name = "recipient_user_id")
    public Long getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(Long recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    @Column(name = "text_message", columnDefinition = "TEXT")
    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    @Column(name = "updated_at")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageEntity that = (ChatMessageEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(senderUserId, that.senderUserId) && Objects.equals(recipientChannelId, that.recipientChannelId) && Objects.equals(recipientUserId, that.recipientUserId) && Objects.equals(textMessage, that.textMessage) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderUserId, recipientChannelId, recipientUserId, textMessage, updatedAt);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChatMessageEntity{");
        sb.append("id=").append(id);
        sb.append(", senderUserId=").append(senderUserId);
        sb.append(", recipientChannelId=").append(recipientChannelId);
        sb.append(", recipientUserId=").append(recipientUserId);
        sb.append(", textMessage='").append(textMessage).append('\'');
        sb.append(", updatedAt=").append(updatedAt);
        sb.append('}');
        return sb.toString();
    }
}
