package template;


import entity.CodeTemplate;
import utils.VelocityUtil;

import java.util.Map;

public class VelocityTemplateEngine extends BaseTemplateEngine {


    protected String doEvaluate(CodeTemplate template, Environment environment) {
        return VelocityUtil.evaluate(template.getCodeTemplate(), environment.bindings);
    }

    @Override
    protected String generateClassName(String classNameTemplate, Map<String, Object> environment) {
        return VelocityUtil.evaluate(classNameTemplate, environment);
    }

}
