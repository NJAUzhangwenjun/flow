package cn.wjhub.flowgpt.gpt.plugin.entity;

import com.unfbx.chatgpt.plugin.PluginParam;
import lombok.Data;

@Data
public class WeatherReq extends PluginParam {
    private String location;
}