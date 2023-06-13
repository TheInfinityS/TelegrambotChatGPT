package kg.mega.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(of = {"role","content","name"})
public class Message {
    private String role;
    private String content;
    private String name;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public static Message ofUser(String content) {

        return new Message(Role.USER.getValue(), content);
    }

    public static Message ofSystem(String content) {

        return new Message(Role.SYSTEM.getValue(), content);
    }

    public static Message ofAssistant(String content) {

        return new Message(Role.ASSISTANT.getValue(), content);
    }
}
