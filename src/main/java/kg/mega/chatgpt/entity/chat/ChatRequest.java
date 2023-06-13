package kg.mega.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest implements Serializable {
    @NonNull
    @Builder.Default
    private String model = Model.GPT_3_5_TURBO.getName();

    @NonNull
    private List<Message> messages;

    @Builder.Default
    private double temperature = 0.9;

    @JsonProperty("top_p")
    @Builder.Default
    private double topP = 0.9;

    @Builder.Default
    private Integer n = 1;


    @Builder.Default
    private boolean stream = false;

    private List<String> stop;

    @JsonProperty("max_tokens")
    private Integer maxTokens;


    @JsonProperty("presence_penalty")
    private double presencePenalty;


    @JsonProperty("frequency_penalty")
    private double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map logitBias;

    private String user;
}
