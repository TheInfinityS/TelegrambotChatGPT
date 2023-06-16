package kg.mega.chatgpt.service;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import io.reactivex.Single;
import kg.mega.chatgpt.entity.chat.ChatRequest;
import kg.mega.chatgpt.entity.chat.ChatResponse;
import kg.mega.chatgpt.exception.ChatException;
import lombok.*;
import kg.mega.chatgpt.api.Api;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import kg.mega.chatgpt.entity.chat.Message;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPT {
    private String apiKey;
    @Builder.Default
    private String apiHost = Api.DEFAULT_API_HOST;
    private Api apiClient;
    private OkHttpClient okHttpClient;

    @Builder.Default
    private long timeout = 300;


    public ChatGPT init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(chain -> {
            Request original = chain.request();
            String key = apiKey;

            Request request = original.newBuilder()
                    .header(Header.AUTHORIZATION.getValue(), "Bearer " + key)
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).addInterceptor(chain -> {
            Request original = chain.request();
            Response response = chain.proceed(original);
            System.out.println(response);
            if (!response.isSuccessful()) {
                String errorMsg = response.body().string();

//                BaseResponse baseResponse = JSON.parseObject(errorMsg, BaseResponse.class);
//                if (Objects.nonNull(baseResponse.getError())) {
//                    log.error(baseResponse.getError().getMessage());
//                    throw new ChatException(baseResponse.getError().getMessage());
//                }
                throw new ChatException("error");
            }
            return response;
        });

        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);

        OkHttpClient httpClient = client.build();
        this.okHttpClient = httpClient;


        this.apiClient = new Retrofit.Builder()
                .baseUrl(this.apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(Api.class);

        return this;
    }
    
    public ChatResponse chatCompletion(ChatRequest chatRequest) {
        Single<ChatResponse> chatCompletionResponse =
                this.apiClient.chatCompletion(chatRequest);
        return chatCompletionResponse.blockingGet();
    }


    public ChatResponse chatCompletion(List<Message> messages) {
        ChatRequest chatCompletion = ChatRequest.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }


    public String chat(String message,List<Message> messages,long limittoken) {
        Message userMessage=Message.ofUser(message);
        messages.add(userMessage);
        ChatRequest chatCompletion = ChatRequest.builder()
                .messages(messages)
                .build();
        System.out.println(chatCompletion);
        ChatResponse response = this.chatCompletion(chatCompletion);
        Message assistantMessage=response.getChoices().get(0).getMessage();
        messages.add(assistantMessage);
        if(response.getUsage().getTotalTokens()>limittoken){
            messages.clear();
        }
        System.out.println(response);
        return assistantMessage.getContent();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }
}
