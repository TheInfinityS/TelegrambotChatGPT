package kg.mega.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = {"index","delta","message","finishReason"})
public class Choise {

    private long index;

    @JsonProperty("delta")
    private Message delta;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;

}
