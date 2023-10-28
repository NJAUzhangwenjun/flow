package cn.wjhub.flowgpt.flow.gpt.plugin.entity;

import lombok.Data;

@Data
public class WeatherResp {
    /**
     * 温度
     */
    private String temp;
    /**
     * 风力等级
     */
    private Integer level;
}