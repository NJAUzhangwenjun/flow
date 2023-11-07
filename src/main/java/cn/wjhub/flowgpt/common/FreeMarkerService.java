package cn.wjhub.flowgpt.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class FreeMarkerService {

    private final Configuration configuration;

    /**
     * 渲染模板
     * @param templateString 模板字符串
     * @param paramMap 参数
     * @return 渲染后的字符串
     */
    public String renderTemplate(String templateString, Map<String, Object> paramMap) throws IOException, TemplateException {
        Template template = new Template("template", templateString, configuration);
        StringWriter writer = new StringWriter();
        template.process(paramMap, writer);
        return writer.toString();
    }
}
