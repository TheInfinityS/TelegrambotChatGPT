package kg.mega.chatgpt.api;


import kg.mega.chatgpt.entity.chat.ChatRequest;
import kg.mega.chatgpt.entity.chat.ChatResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import io.reactivex.Single;
public interface Api {
    String DEFAULT_API_HOST = "https://api.openai.com/";
    @POST("v1/chat/completions")
    Single<ChatResponse> chatCompletion(@Body ChatRequest chatRequest);
}
