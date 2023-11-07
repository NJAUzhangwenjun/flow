package cn.wjhub.flowgpt.flow.litlow;

import cn.wjhub.flowgpt.flow.context.AbsContext;
import cn.wjhub.flowgpt.flow.context.FlowContext;
import cn.wjhub.flowgpt.flow.liteTest.context.CustomContext;
import com.alibaba.fastjson.JSON;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestPropertySource(locations = "classpath:application.yml")
@Slf4j
public class TestFlow {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testConfig() {
        FlowContext flowContext = FlowContext.builder().topic("test").id("1").chainName("chain2").flowType(FlowContext.FlowType.FORM).build();
        flowContext.setData("name", "chatGpt");
        flowContext.setData("template", "Hello, ${name}");
        LiteflowResponse response = flowExecutor.execute2Resp("chain2",null,flowContext);
        System.out.println("response = " + JSON.toJSONString(response.getExecuteStepStr()));
    }

    @Test
    public void testCommand() throws Exception {
        CustomContext customContext = new CustomContext();
        customContext.setAge(18);
        customContext.setName("yoma");
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", null, customContext);
        CustomContext context = response.getContextBean(CustomContext.class);
        String stepStr = response.getExecuteStepStrWithTime();
        log.info("context = " + JSON.toJSONString(context));
        log.info("stepStr = " + stepStr);
        if (response.isSuccess()) {
            log.info("执行成功");
        } else {
            log.info("执行失败");
        }

    }

}
