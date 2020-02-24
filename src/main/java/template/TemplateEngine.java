package template;


import entity.CodeTemplate;

import java.util.List;

public interface TemplateEngine {

    GeneratedSource evaluate(CodeTemplate template, List<entity.ClassEntry> selectClasses, entity.ClassEntry currentClass);
}
