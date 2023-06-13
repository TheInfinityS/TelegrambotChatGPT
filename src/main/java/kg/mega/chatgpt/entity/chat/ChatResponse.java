package kg.mega.chatgpt.entity.chat;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(of = {"id","object","created","model","choices","usage"})
public class ChatResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choise> choices;
    private Usage usage;
}
