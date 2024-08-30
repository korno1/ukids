package com.modernfamily.ukids.domain.chatMessage.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, EXIT
    }

    private MessageType type;
    private Long roomId;
    private Long senderId;
    private String sender;
    private String message;
    private LocalDateTime createTime = LocalDateTime.now();

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type='" + type + '\'' +
                ", roomId=" + roomId +
                ", senderId=" + senderId + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", time=" + createTime + '\'' +
                '}';
    }

}
