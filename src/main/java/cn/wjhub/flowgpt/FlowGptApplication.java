package cn.wjhub.flowgpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FlowGptApplication {


    public static void main(String[] args) {
        SpringApplication.run(FlowGptApplication.class, args);
    }

}
