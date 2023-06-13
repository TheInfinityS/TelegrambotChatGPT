package kg.mega.chatgpt.service;

import kg.mega.chatgpt.config.BotConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("application.yaml")
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final ChatGPT chatGPT;

    public TelegramBot(BotConfig botConfig) {
        System.out.println(botConfig.getApitoken());
        this.botConfig = botConfig;
        chatGPT = ChatGPT.builder()
                .apiKey(botConfig.getApitoken())
                .messages(new ArrayList<>())
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()&&update.getMessage().hasText()){
            Message message=update.getMessage();

            String messageText=message.getText();
            long chatId=message.getChatId();

            if(messageText.equals("/start")){

                startCommandReceived(chatId,message.getChat().getFirstName());
            }
            else {

                String res = chatGPT.chat(messageText);
                sendMessage(chatId,res);
            }
        }
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer="Привет, "+firstName+", меня зовут Mega ChatGPT, приятно познакомится! Напиши свой вопрос и я передам его ChatGPT";
        sendMessage(chatId,answer);
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
