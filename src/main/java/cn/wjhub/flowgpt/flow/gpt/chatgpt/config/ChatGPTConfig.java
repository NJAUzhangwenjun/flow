package cn.wjhub.flowgpt.flow.gpt.chatgpt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "gpt.chat-gpt")
@Component
@Data
public class ChatGPTConfig {
    private String apiHost;
    private String[] apiKey;
}