package kg.mega.chatgpt.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Model {
    GPT_3_5_TURBO("gpt-3.5-turbo"),

    GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),

    GPT_4("gpt-4"),

    GPT_4_0314("gpt-4-0314"),

    GPT_4_32K("gpt-4-32k"),

    GPT_4_32K_0314("gpt-4-32k-0314"),
    ;
    private String name;
}
