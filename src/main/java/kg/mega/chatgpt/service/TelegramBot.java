package kg.mega.chatgpt.service;

import kg.mega.chatgpt.bot.Commands;
import kg.mega.chatgpt.config.BotConfig;
import kg.mega.chatgpt.entity.ChatUser;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@PropertySource("application.yaml")
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private Map<Long,List<kg.mega.chatgpt.entity.chat.Message>> chatMessages;

    private final ChatGPT chatGPT;

    private Map<ChatUser, String> actions;

    private final String botName="@MegacomChatGPTbot";

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        chatGPT = ChatGPT.builder()
                .apiKey(botConfig.getApitoken())
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();
        actions=new HashMap<>();
        chatMessages=new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()&&update.getMessage().hasText()){
            Message message=update.getMessage();


            String messageText=message.getText();
            long chatId=message.getChatId();
            String username=message.getFrom().getUserName();

            System.out.println(chatId);
            System.out.println(username);

            if(messageText.equals(Commands.COMMAND_START)||messageText.equals(Commands.COMMAND_START+botName)){
                startCommandReceived(chatId,message.getChat().getFirstName());
            }
            else if (messageText.equals(Commands.COMMAND_ASK)||messageText.equals(Commands.COMMAND_ASK+botName)) {
                askCommandReceived(chatId,username);
            }
            else if (messageText.equals(Commands.COMMAND_CLEAR)||messageText.equals(Commands.COMMAND_CLEAR+botName)) {
                clearCommandReceived(chatId);
            }
            else {
                ChatUser chatUser=new ChatUser(chatId,username);

                if(actions.containsKey(chatUser)){
                    String action=actions.get(chatUser);
                    if (action.equals(Commands.COMMAND_ASK)) {
                        askGPT(messageText, chatId);
                    }
                    actions.remove(chatUser);
                }
            }
        }
    }

    private void clearCommandReceived(long chatId) {
        if(!chatMessages.containsKey(chatId)||chatMessages.get(chatId)==null){
            List<kg.mega.chatgpt.entity.chat.Message> messages=new ArrayList<>();
            chatMessages.put(chatId,messages);
        }
        chatMessages.get(chatId).clear();
        sendMessage(chatId,"История была успшено очищена");
    }

    private void askGPT(String messageText, long chatId) {
        if(!chatMessages.containsKey(chatId)||chatMessages.get(chatId)==null){
            List<kg.mega.chatgpt.entity.chat.Message> messages=new ArrayList<>();
            chatMessages.put(chatId,messages);
        }
        String res = chatGPT.chat(messageText,chatMessages.get(chatId),botConfig.getLimit());
        sendMessage(chatId,res);
    }

    private void askCommandReceived(long chatId, String username) {
        ChatUser chatUser=new ChatUser(chatId,username);

        actions.put(chatUser,Commands.COMMAND_ASK);

        sendMessage(chatId,"Задай вопрос и я его передам ChatGPT");
    }

    private void startCommandReceived(long chatId, String firstName) {
        StringBuilder answer=new StringBuilder("Привет, "+firstName+", меня зовут Mega ChatGPT, приятно познакомится! Напиши свой вопрос и я передам его ChatGPT");

        List<String> descriptions=Commands.descriptions();
        List<String> commands=Commands.commands();

        for(int i=0;i<descriptions.size();i++){
            answer.append("\n").append(commands.get(i)).append(" - ").append(descriptions.get(i));
        }

        if(!chatMessages.containsKey(chatId)){
            List<kg.mega.chatgpt.entity.chat.Message> messages=new ArrayList<>();
            chatMessages.put(chatId,messages);
        }
        sendMessage(chatId,answer.toString());
    }

    private void sendMessage(long chatId,String textToSend){
        SendMessage message=new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    public String getBotToken(){
        return botConfig.getToken();
    }

}
