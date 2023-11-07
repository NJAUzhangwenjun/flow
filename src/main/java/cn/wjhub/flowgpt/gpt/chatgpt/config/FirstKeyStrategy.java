package cn.wjhub.flowgpt.gpt.chatgpt.config;

import com.unfbx.chatgpt.function.KeyStrategyFunction;

import java.util.List;

/**
 * 描述：自定义的key使用策略
 * 总是使用第一个key
 */
public class FirstKeyStrategy implements KeyStrategyFunction<List<String>, String> {

    /**
     * 总是使用第一个
     * @param keys
     * @return
     */
    @Override
    public String apply(List<String> keys) {
        return keys.get(0);
    }
}