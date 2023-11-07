package cn.wjhub.flowgpt.gpt;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.billing.BillingUsage;
import com.unfbx.chatgpt.entity.billing.CreditGrantsResponse;
import com.unfbx.chatgpt.entity.billing.Subscription;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.common.DeleteResponse;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.edits.Edit;
import com.unfbx.chatgpt.entity.edits.EditResponse;
import com.unfbx.chatgpt.entity.embeddings.Embedding;
import com.unfbx.chatgpt.entity.embeddings.EmbeddingResponse;
import com.unfbx.chatgpt.entity.engines.Engine;
import com.unfbx.chatgpt.entity.files.File;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.fineTune.Event;
import com.unfbx.chatgpt.entity.fineTune.FineTune;
import com.unfbx.chatgpt.entity.fineTune.FineTuneDeleteResponse;
import com.unfbx.chatgpt.entity.fineTune.FineTuneResponse;
import com.unfbx.chatgpt.entity.images.*;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.moderations.Moderation;
import com.unfbx.chatgpt.entity.moderations.ModerationResponse;
import com.unfbx.chatgpt.entity.whisper.Transcriptions;
import com.unfbx.chatgpt.entity.whisper.Translations;
import com.unfbx.chatgpt.entity.whisper.Whisper;
import com.unfbx.chatgpt.entity.whisper.WhisperResponse;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

/**
 * 描述： 测试类
 */
@SpringBootTest
@Slf4j
public class OpenAiClientTest {

    @Autowired
    private OpenAiClient openAiClient;


    @Test
    public void subscription() {
        Subscription subscription = openAiClient.subscription();
        log.info("用户名：{}", subscription.getAccountName());
        log.info("用户总余额（美元）：{}", subscription.getHardLimitUsd());
        log.info("更多信息看Subscription类");
    }

    @Test
    public void billingUsage() {
        LocalDate start = LocalDate.of(2023, 3, 7);
        BillingUsage billingUsage = openAiClient.billingUsage(start, LocalDate.now());
        log.info("总使用金额（美分）：{}", billingUsage.getTotalUsage());
        log.info("更多信息看BillingUsage类");
    }


    @Test
    public void chatTokensTest() {
        // 聊天模型：gpt-3.5
        List<Message> messages = new ArrayList<>(2);
        messages.add(Message.builder().role(Message.Role.USER).content("关注微信公众号：程序员的黑洞。").build());
        messages.add(Message.builder().role(Message.Role.USER).content("进入chatgpt-java交流群获取最新版本更新通知。").build());
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(messages)
                .maxTokens((4096 - TikTokensUtil.tokens(ChatCompletion.Model.GPT_3_5_TURBO.getName(), messages)))
                .build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        // 获取请求的tokens数量
        long tokens = chatCompletion.tokens();
        // 这种方式也可以
//        long tokens = TikTokensUtil.tokens(chatCompletion.getModel(),messages);
        log.info("Message集合文本：【{}】", messages, tokens);
        log.info("本地计算的请求的tokens数{}", tokens);
        log.info("本地计算的返回的tokens数{}", TikTokensUtil.tokens(chatCompletion.getModel(), chatCompletionResponse.getChoices().get(0).getMessage().getContent()));
        log.info("---------------------------------------------------");
        log.info("Open AI 官方计算的总的tokens数{}", chatCompletionResponse.getUsage().getTotalTokens());
        log.info("Open AI 官方计算的请求的tokens数{}", chatCompletionResponse.getUsage().getPromptTokens());
        log.info("Open AI 官方计算的返回的tokens数{}", chatCompletionResponse.getUsage().getCompletionTokens());
    }

    @Test
    public void testJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setTimeZone(TimeZone.getTimeZone("GMT+8"))
                .setLocale(Locale.CHINA);
        Completion completion = Completion.builder().prompt("你好啊").build();
        String jsonStr = objectMapper.writeValueAsString(completion);
        Completion completion1 = objectMapper.readValue(jsonStr, Completion.class);
    }

    @Test
    public void creditGrants() {
        CreditGrantsResponse creditGrantsResponse = openAiClient.creditGrants();
        log.info("账户总余额（美元）：{}", creditGrantsResponse.getTotalGranted());
        log.info("账户总使用金额（美元）：{}", creditGrantsResponse.getTotalUsed());
        log.info("账户总剩余金额（美元）：{}", creditGrantsResponse.getTotalAvailable());
    }


    @Test
    public void chat() {
        // 聊天模型：gpt-3.5
        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> {
            log.info(JSON.toJSONString(e.getMessage()));
        });
    }

    @Test
    public void speechToTextTranscriptions() {
        Transcriptions transcriptions = Transcriptions.builder()
                .model(Whisper.Model.WHISPER_1.getName())
                .prompt("提示语")
                .language("zh")
                .temperature(0.2)
                .responseFormat(Whisper.ResponseFormat.VTT.getName())
                .build();

        // 语音转文字
        WhisperResponse whisperResponse =
                openAiClient.speechToTextTranscriptions(new java.io.File("C:\\Users\\grt\\Desktop\\1.m4a"), transcriptions);
        log.info(JSON.toJSONString(whisperResponse.getText()));
    }

    @Test
    public void speechToTextTranscriptionsV2() {
        // 语音转文字
        WhisperResponse whisperResponse =
                openAiClient.speechToTextTranscriptions(new java.io.File("C:\\Users\\grt\\Desktop\\1.m4a"));
        log.info(JSON.toJSONString(whisperResponse.getText()));
    }

    @Test
    public void speechToTextTranslations() {
        Translations translations = Translations.builder()
                .model(Whisper.Model.WHISPER_1.getName())
//                .prompt("提示语")
                .temperature(0.2)
                .responseFormat(Whisper.ResponseFormat.JSON.getName())
                .build();
        // 语音转文字+翻译
        WhisperResponse whisperResponse =
                openAiClient.speechToTextTranslations(new java.io.File("C:\\Users\\**\\Desktop\\1.m4a"), translations);
        log.info(JSON.toJSONString(whisperResponse.getText()));
    }

    @Test
    public void speechToTextTranslationsV2() {
        // 语音转文字+翻译
        WhisperResponse whisperResponse =
                openAiClient.speechToTextTranslations(new java.io.File("C:\\Users\\**\\Desktop\\1.m4a"));
        log.info(JSON.toJSONString(whisperResponse.getText()));
    }

    @Test
    public void models() {
        List<Model> models = openAiClient.models();
        models.forEach(e -> {
            System.out.print(e.getOwnedBy() + " ");
            System.out.print(e.getId() + " ");
            log.info(JSON.toJSONString(e.getObject() + " "));
        });
    }

    @Test
    public void model() {
        Model model = openAiClient.model("code-davinci-002");
        log.info(JSON.toJSONString(model.toString()));
    }

    @Test
    public void completions() {
//        CompletionResponse completions = v2.completions("Java Stream list to map");
//        Arrays.stream(completions.getChoices()).forEach(System.out::println);

        CompletionResponse completions = openAiClient.completions("我想申请转专业，从计算机专业转到会计学专业，帮我完成一份两百字左右的申请书");
        Arrays.stream(completions.getChoices()).forEach(System.out::println);
    }

    // 对话测试
    @Test
    public void completionsV3() {
        String question = "Human: 帮我把下面的文本翻译成英文；我爱你中国\n";
        Completion q = Completion.builder()
                .prompt(question)
                .stop(Arrays.asList(" Human:", " Bot:"))

                .echo(true)
                .build();
        CompletionResponse completions = openAiClient.completions(q);
        String text = completions.getChoices()[0].getText();

        q.setPrompt(text + "\n" + "再翻译成韩文\n");
        completions = openAiClient.completions(q);
        text = completions.getChoices()[0].getText();

        q.setPrompt(text + "\n" + "再翻译成日文\n");
        completions = openAiClient.completions(q);
        text = completions.getChoices()[0].getText();
        log.info(JSON.toJSONString(text));
    }

    @Test
    public void completionsV2() {
        Completion q = Completion.builder()
                .prompt("三体人是什么？")
                .n(2)
                .bestOf(3)
                .build();
        CompletionResponse completions = openAiClient.completions(q);
        log.info(JSON.toJSONString(completions));
    }

    @Test
    public void editText() {
        // 文本修改
//        Edit edit = Edit.builder().input("我爱你麻麻").instruction("帮我修改错别字").model(Edit.Model.TEXT_DAVINCI_EDIT_001.getName()).build();
        // 代码修改 NB....
        Edit edit = Edit.builder().input("System.out.pri(\"AAAAA\");").instruction("帮我修改这个java代码").model(Edit.Model.CODE_DAVINCI_EDIT_001.getName()).build();
        EditResponse editResponse = openAiClient.edit(edit);
        log.info(JSON.toJSONString(editResponse));
    }


    @Test
    public void genImages() {
        Image image = Image.builder().prompt("电脑画面").responseFormat(ResponseFormat.B64_JSON.getName()).build();
        ImageResponse imageResponse = openAiClient.genImages(image);
        log.info(JSON.toJSONString(imageResponse));
    }

    @Test
    public void genImagesV2() {
        ImageResponse imageResponse = openAiClient.genImages("睡着的小朋友");
        log.info(JSON.toJSONString(imageResponse));
    }

    /**
     * Invalid input image - format must be in ['RGBA', 'LA', 'L'], got RGB.
     */

    @Test
    public void editImageV2() {
        ImageEdit imageEdit = ImageEdit.builder().prompt("去除图片中的文字").build();
        List<Item> images = openAiClient.editImages(new java.io.File("C:\\Users\\FLJS188\\Desktop\\o.png"),
                imageEdit);
        log.info(JSON.toJSONString(images));
    }

    @Test
    public void editImageV3() {
        List<Item> images = openAiClient.editImages(new java.io.File("C:\\Users\\***\\Desktop\\1.png"),
                "去除图片中的文字");
        log.info(JSON.toJSONString(images));
    }

    @Test
    public void editImage() {
        List<Item> images = openAiClient.editImages(new java.io.File("C:\\Users\\***\\Desktop\\1.png"),
                "去除图片中的文字");
        log.info(JSON.toJSONString(images));
    }


    @Test
    public void variationsImagesV2() {
        ImageVariations imageVariations = ImageVariations.builder().build();
        ImageResponse imageResponse = openAiClient.variationsImages(new java.io.File("C:\\Users\\***\\Desktop\\12.png"), imageVariations);
        log.info(JSON.toJSONString(imageResponse));
    }

    @Test
    public void variationsImages() {
        ImageResponse imageResponse = openAiClient.variationsImages(new java.io.File("C:\\Users\\***\\Desktop\\12.png"));
        log.info(JSON.toJSONString(imageResponse));
    }


    @Test
    public void embeddingsV2() {
        Embedding embedding = Embedding.builder().input(Arrays.asList("我爱你亲爱的姑娘", "i love you")).build();
        EmbeddingResponse embeddings = openAiClient.embeddings(embedding);
        log.info(JSON.toJSONString(embeddings));
    }

    @Test
    public void embeddingsV3() {
        EmbeddingResponse embeddings = openAiClient.embeddings(Arrays.asList("我爱你亲爱的姑娘", "i love you"));
        log.info(JSON.toJSONString(embeddings));
    }

    @Test
    public void embeddings() {
        EmbeddingResponse embeddings = openAiClient.embeddings("我爱你");
        log.info(JSON.toJSONString(embeddings));
    }


    @Test
    public void files() {
        List<File> files = openAiClient.files();
        log.info(JSON.toJSONString(files));
    }

    @Test
    public void retrieveFile() {
        File files = openAiClient.retrieveFile("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
        log.info(JSON.toJSONString(files));
    }

    /**
     * 不支持免费用户： To help mitigate abuse, downloading of fine-tune training files is disabled for free accounts.
     * 暂时没有测试
     */
    @Test
    @Ignore
    public void retrieveFileContent() {
//        ResponseBody responseBody = v2.retrieveFileContent("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
//        log.info(JSON.toJSONString(responseBody);
    }

    @Test
    public void uploadFileV1() {
        UploadFileResponse uploadFileResponse = openAiClient.uploadFile(new java.io.File("C:\\Users\\***\\Desktop\\2.txt"));
        log.info(JSON.toJSONString(uploadFileResponse));
    }

    @Test
    public void uploadFileV2() {
        UploadFileResponse uploadFileResponse = openAiClient.uploadFile("fine-tune", new java.io.File("C:\\Users\\***\\Desktop\\2.txt"));
        log.info(JSON.toJSONString(uploadFileResponse));
    }

    @Test
    public void deleteFile() {
        DeleteResponse deleteResponse = openAiClient.deleteFile("file-GreIoKq6lWHvq8PDwDZIGJjm");
        log.info(JSON.toJSONString(deleteResponse));
    }

    @Test
    public void moderations() {
        ModerationResponse moderations = openAiClient.moderations("I want to kill them.");
        log.info(JSON.toJSONString(moderations));
    }

    @Test
    public void moderationsv3() {
        List<String> list = Collections.singletonList("I want to kill them.");
        ModerationResponse moderations = openAiClient.moderations(list);
        log.info(JSON.toJSONString(moderations));
    }

    @Test
    public void moderationsV2() {
        Moderation moderation = Moderation.builder().input(Collections.singletonList("I want to kill them.")).build();
        ModerationResponse moderations = openAiClient.moderations(moderation);
        log.info(JSON.toJSONString(moderations));
    }


    @Test
    public void engines() {
        List<Engine> engines = openAiClient.engines();
        log.info(JSON.toJSONString(engines));
    }

    @Test
    public void engine() {
        Engine engines = openAiClient.engine("code-davinci-002");
        log.info(JSON.toJSONString(engines));
    }

    @Test
    public void fineTune() {
        FineTuneResponse fineTuneResponse = openAiClient.fineTune("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
        log.info(JSON.toJSONString(fineTuneResponse));
    }

    @Test
    public void fineTuneV2() {
        FineTune fineTune = FineTune.builder()
                .trainingFile("file-OcQb9zg35cxa4WLBZJ9K2523")
                .suffix("grttttttttt")
                .model(FineTune.Model.ADA.getName())
                .build();
        FineTuneResponse fineTuneResponse = openAiClient.fineTune(fineTune);
        log.info(JSON.toJSONString(fineTuneResponse));
    }

    @Test
    public void fineTunes() {
        List<FineTuneResponse> fineTuneResponses = openAiClient.fineTunes();
        log.info(JSON.toJSONString(fineTuneResponses));
    }

    @Test
    public void retrieveFineTune() {
        FineTuneResponse fineTuneResponses = openAiClient.retrieveFineTune("ft-bU0xJzVfrgOjqoy1e9lC2oDP");
        log.info(JSON.toJSONString(fineTuneResponses));
    }

    @Test
    public void cancelFineTune() {
        // status发生变化 pending -> cancelled
        FineTuneResponse fineTuneResponses = openAiClient.cancelFineTune("ft-KohbEOCbPyNTyQmt5UV1F1cb");
        log.info(JSON.toJSONString(fineTuneResponses));
    }

    @Test
    public void fineTuneEvents() {
        List<Event> events = openAiClient.fineTuneEvents("ft-KohbEOCbPyNTyQmt5UV1F1cb");
        log.info(JSON.toJSONString(events));
    }

    @Test
    public void deleteFineTuneModel() {
        FineTuneDeleteResponse deleteResponse = openAiClient.deleteFineTuneModel("ada:ft-winter:grttttttttt-2023-02-17-01-29-27");
        log.info(JSON.toJSONString(deleteResponse));
    }
}