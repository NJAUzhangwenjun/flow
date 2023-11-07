package cn.wjhub.flowgpt.app;

import cn.wjhub.flowgpt.flow.context.FlowContext;
import com.yomahub.liteflow.core.FlowExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
@Slf4j
public class TemplateController {

    @Resource
    private FlowExecutor flowExecutor;

    @PostMapping("/chat")
    public String chat(@RequestBody Param param) {
        FlowContext flowContext = FlowContext.builder().topic(param.getTopic()).id("1").chainName(param.getChainName()).flowType(FlowContext.FlowType.FORM).build();
        flowContext.setData("message", param.getMessage());
        flowContext.setData("template", "${message}");
        return flowExecutor.execute2Resp("chain2", null, flowContext).getContextBean(FlowContext.class).getData("response");

    }

}

@Data
class Param {
    private String topic;
    private String chainName;
    private String message;
}