package kg.mega.chatgpt.bot;

import java.util.List;

public class Commands {
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_ASK="/ask";
    public static final String COMMAND_CLEAR="/clear";

    public static final String DESCRIPTION_ASK="Обращение к GPT";
    public static final String DESCRIPTION_CLEAR="Очистка истории GPT";

    public static List<String> commands(){
        return List.of(COMMAND_ASK,COMMAND_CLEAR,COMMAND_START);
    }
    public static List<String> descriptions(){
        return List.of(DESCRIPTION_ASK,DESCRIPTION_CLEAR);
    }
}
