package kg.mega.chatgpt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"chatId","username"})
public class ChatUser {
    private long chatId;

    private String username;
}
