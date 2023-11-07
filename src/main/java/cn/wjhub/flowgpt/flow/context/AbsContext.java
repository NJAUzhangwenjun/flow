package cn.wjhub.flowgpt.flow.context;

import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.exception.NullParamException;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Data
@SuperBuilder
@Validated
public class AbsContext implements Serializable {
    /**
     * 流程id
     */
    @NotEmpty(message = "流程id不能为空")
    protected String id;

    /**
     * 流程名称
     */
    @NotEmpty(message = "流程名称不能为空")
    protected String chainName;

    /**
     * 流程类型:表单类型,对话类型
     */
    @NotEmpty(message = "流程类型不能为空")
    protected FlowType flowType;

    /**
     * 上下文
     */
    protected final ConcurrentHashMap<String, Object> contextMap = new ConcurrentHashMap<>();

    protected <T> void putDataMap(String key, T t) {
        if (ObjectUtil.isNull(t)) {
            throw new NullParamException("data can't accept null param");
        }
        contextMap.put(key, t);
    }

    public boolean hasData(String key) {
        return contextMap.containsKey(key);
    }

    public <T> T getData(String key) {
        return (T) contextMap.get(key);
    }

    public <T> void setData(String key, T t) {
        putDataMap(key, t);
    }

    /**
     * 流程类型
     */
    public static enum FlowType {
        /**
         * 表单类型
         */
        FORM,
        /**
         * 对话类型
         */
        CHAT
    }
}
