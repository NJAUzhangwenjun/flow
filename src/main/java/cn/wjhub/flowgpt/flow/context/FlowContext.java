package cn.wjhub.flowgpt.flow.context;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FlowContext extends AbsContext {
    @NotEmpty(message = "topic不能为空")
    private String topic;

}
