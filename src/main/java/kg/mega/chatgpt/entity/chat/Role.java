package kg.mega.chatgpt.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    ;
    private String value;
}
