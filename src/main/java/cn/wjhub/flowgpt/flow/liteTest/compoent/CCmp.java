package cn.wjhub.flowgpt.flow.liteTest.compoent;

import cn.wjhub.flowgpt.flow.liteTest.context.CustomContext;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

@LiteflowComponent("c")
@Slf4j
public class CCmp extends NodeComponent {

	@Override
	public void process() {
		CustomContext customContext = this.getContextBean(CustomContext.class);
		log.info("CCmp executed! name:{},age:{}", customContext.getName(), customContext.getAge());
		customContext.setAge(customContext.getAge() + 1);
	}

}