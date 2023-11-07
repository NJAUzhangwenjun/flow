package cn.wjhub.flowgpt.gpt.plugin;

import cn.wjhub.flowgpt.gpt.plugin.entity.WeatherReq;
import cn.wjhub.flowgpt.gpt.plugin.entity.WeatherResp;
import com.alibaba.fastjson2.JSON;
import com.unfbx.chatgpt.plugin.PluginAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeatherPlugin extends PluginAbstract<WeatherReq, WeatherResp> {

    public WeatherPlugin(Class<?> r) {
        super(r);
    }

    @Override
    public WeatherResp func(WeatherReq args) {
        log.info("天气插件，请求参数：{}", JSON.toJSONString(args));
        WeatherResp weatherResp = new WeatherResp();
        weatherResp.setTemp("-5到-8摄氏度");
        weatherResp.setLevel(3);
        return weatherResp;
    }

    @Override
    public String content(WeatherResp weatherResp) {
        return "当前天气温度：" + weatherResp.getTemp() + "，风力等级：" + weatherResp.getLevel();
    }
}