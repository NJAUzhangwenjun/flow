package cn.wjhub.flowgpt.flow.compoent;

import cn.wjhub.flowgpt.common.FreeMarkerService;
import cn.wjhub.flowgpt.flow.context.FlowContext;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.yomahub.liteflow.annotation.LiteflowCmpDefine;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@LiteflowComponent("prompt")
@LiteflowCmpDefine(NodeTypeEnum.COMMON)
@Slf4j
@AllArgsConstructor
public class Prompt {

    // mock Message history
    private static final Map<String, List<Message>> MESSAGE_HISTORY = new HashMap<>();

    private final FreeMarkerService freeMarkerService;

    private final OpenAiClient openAiClient;

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeType = NodeTypeEnum.COMMON)
    public void processPrompt(NodeComponent bindCmp) {
        FlowContext context = bindCmp.getContextBean(FlowContext.class);
        String prompt = buildPrompt(context);
        if (!StringUtils.hasLength(prompt)) {
            log.error("Prompt executed! id:{},chainName:{},flowType:{},prompt is empty", context.getId(), context.getChainName(), context.getFlowType());
            throw new RuntimeException("prompt is empty");
        }

        String response = callAi(prompt, context.getTopic());
        context.setData("response", response);
        log.info("Prompt executed! id:{},chainName:{},flowType:{},response:{}", context.getId(), context.getChainName(), context.getFlowType(), response);

    }

    private String callAi(String prompt, String topic) {
        Message message = Message.builder().role(Message.Role.USER).content(prompt).build();
        List<Message> messages = MESSAGE_HISTORY.getOrDefault(topic, new CopyOnWriteArrayList<>());
        messages.add(message);
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        String response = openAiClient.chatCompletion(chatCompletion).getChoices().get(0).getMessage().getContent();
        Assert.hasLength(response, "response is empty");
        return response;
    }

    private String buildPrompt(FlowContext context) {
        try {
            return freeMarkerService.renderTemplate(context.getContextMap().get("template").toString(), context.getContextMap());
        } catch (IOException | TemplateException e) {
            log.error("Prompt executed! id:{},chainName:{},flowType:{},buildPrompt error", context.getId(), context.getChainName(), context.getFlowType());
            throw new RuntimeException(e);
        }
    }
}
