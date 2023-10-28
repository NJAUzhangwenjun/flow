package cn.wjhub.flowgpt.flow.litlow;

import cn.wjhub.flowgpt.flow.liteTest.context.CustomContext;
import com.alibaba.fastjson2.JSON;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class TestFlow {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testConfig() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain1");
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
