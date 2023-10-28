package cn.wjhub.flowgpt.flow.gpt;

import cn.wjhub.flowgpt.flow.gpt.plugin.WeatherPlugin;
import cn.wjhub.flowgpt.flow.gpt.plugin.entity.WeatherReq;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.plugin.PluginAbstract;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
@Slf4j
public class ChatGPTTest {

    @Autowired
    private OpenAiStreamClient client;

    @Autowired
    private OpenAiClient openAiClient;

    @Test
    public void testChatGPTAiStream() {
        // 聊天
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Message message = Message.builder().role(Message.Role.USER).content("你好！").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Collections.singletonList(message)).build();
        client.streamChatCompletion(chatCompletion, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChatGPTAiClient() {
        // 聊天
        Message message = Message.builder().role(Message.Role.USER).content("你好！").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Collections.singletonList(message)).build();
        ChatCompletionResponse response = openAiClient.chatCompletion(chatCompletion);
        log.info("response = " + response.getChoices().get(0).getMessage().getContent());
    }


    @Test
    public void plugin() {
        WeatherPlugin plugin = new WeatherPlugin(WeatherReq.class);
        plugin.setName("知心天气");
        plugin.setFunction("getLocationWeather");
        plugin.setDescription("提供一个地址，方法将会获取该地址的天气的实时温度信息。");

        PluginAbstract.Arg arg = new PluginAbstract.Arg();
        arg.setName("location");
        arg.setDescription("地名");
        arg.setType("string");
        arg.setRequired(true);

        plugin.setArgs(Collections.singletonList(arg));

        // Message message1 = Message.builder().role(Message.Role.USER).content("秦始皇统一了哪六国。").build();
        Message message2 = Message.builder().role(Message.Role.USER).content("获取上海市的天气现在多少度，然后再给出3个推荐的户外运动。").build();
        List<Message> messages = new ArrayList<>();
        // messages.add(message1);
        messages.add(message2);
        // 默认模型：GPT_3_5_TURBO_16K_0613
        // 有四个重载方法，都可以使用
        ChatCompletionResponse response = openAiClient.chatCompletionWithPlugin(messages, plugin);
        log.info("自定义的方法返回值：{}", response.getChoices().get(0).getMessage().getContent());
    }
}
