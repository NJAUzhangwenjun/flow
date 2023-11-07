package cn.wjhub.flowgpt.gpt.chatgpt.config;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ClientConfig {

    @Bean
    public OpenAiStreamClient openAiStreamClient(ChatGPTConfig chatGPTConfig) {
        return OpenAiStreamClient.builder()
                .apiKey(Arrays.asList(chatGPTConfig.getApiKey()))
                .apiHost(chatGPTConfig.getApiHost())
                .build();
    }

    @Bean
    public OpenAiClient openAiClient(ChatGPTConfig chatGPTConfig) {
        return OpenAiClient.builder()
                .apiKey(Arrays.asList(chatGPTConfig.getApiKey()))
                .apiHost(chatGPTConfig.getApiHost())
                .build();
    }
}
