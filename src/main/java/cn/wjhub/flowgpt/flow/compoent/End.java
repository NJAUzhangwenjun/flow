package cn.wjhub.flowgpt.flow.compoent;


import cn.wjhub.flowgpt.flow.context.FlowContext;
import com.yomahub.liteflow.annotation.LiteflowCmpDefine;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.extern.slf4j.Slf4j;

@LiteflowComponent("end")
@LiteflowCmpDefine(NodeTypeEnum.COMMON)
@Slf4j
public class End {

    @LiteflowMethod(value = LiteFlowMethodEnum.IS_END, nodeType = NodeTypeEnum.COMMON)
    public boolean isEnd(NodeComponent bindCmp) {
        FlowContext context = bindCmp.getContextBean(FlowContext.class);
        log.info("End executed! id:{},chainName:{},flowType:{}", context.getId(), context.getChainName(), context.getFlowType());
        return Boolean.TRUE;
    }
}
