package cn.wjhub.flowgpt.flow.compoent;

import cn.wjhub.flowgpt.flow.context.FlowContext;
import com.yomahub.liteflow.annotation.LiteflowCmpDefine;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.extern.slf4j.Slf4j;

@LiteflowComponent("start")
@LiteflowCmpDefine(NodeTypeEnum.COMMON)
@Slf4j
public class Start {
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeType = NodeTypeEnum.COMMON)
    public void processStart(NodeComponent bindCmp) {
        FlowContext context = bindCmp.getContextBean(FlowContext.class);
        log.info("Start executed! id:{},chainName:{},flowType:{}", context.getId(), context.getChainName(), context.getFlowType());
    }
}
