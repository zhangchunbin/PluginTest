package action;

import com.google.common.collect.Maps;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import core.CodeFactory;
import core.DatabaseGenerator;
import utils.AndroidUtils;
import utils.VelocityUtil;

import java.io.IOException;
import java.util.Map;

/**
 * ================================
 *
 * @author: zcb
 * @email: zcbin2@grgbanking.com
 * @time: 2020/2/24 9:57 上午
 * @version: 1.0
 * @desc: 测试Velocity模版
 * =================================
 */
public class VelocityAction extends AnAction {

    private String seeTemplate;

    public VelocityAction() {
        super();
        try {
            seeTemplate = FileUtil.loadTextAndClose(VelocityAction.class.getResourceAsStream("/templates/See.vm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project==null){
            return;
        }
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification("CodeMaker plugin is not available during indexing");
            return;
        }

        PsiFile javaFile = e.getData(CommonDataKeys.PSI_FILE);

        Map<String, Object> map = Maps.newHashMap();
        map.put("interface", "TestInterface");
        map.put("method", "test");
        map.put("paramsType", "void");
        String seeDoc = VelocityUtil.evaluate(seeTemplate, map);

        //todo 不能放在主线程中
        //输出生成文档
        VirtualFile baseDir = AndroidUtils.getAppPackageBaseDir(project);
        //判断跟目录下面是否有velocity文件夹
        VirtualFile dbDir = baseDir.findChild("velocity");
        if (dbDir==null){
            //没有就创建一个
            try {
                dbDir = baseDir.createChildDirectory(null,"velocity");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //开始生成java类
        genCode(project,dbDir,seeDoc);
    }

    /**
     * 生成代码
     * @param project
     * @param dbDir
     * @param content
     */
    private void genCode(Project project,VirtualFile dbDir,String content) {
        String name = "VelocityTemplate.java";
        VirtualFile virtualFile = dbDir.findChild(name);
        if (virtualFile == null) {
            //第一步：么有这个文件，第一次使用代码字符串创建类
            PsiFile initFile = PsiFileFactory.getInstance(project).createFileFromText(name
                    , JavaFileType.INSTANCE
                    , content);
            //第二布： 把生产的文件加到db目录下
            WriteCommandAction.runWriteCommandAction(project,()->{
                PsiManager.getInstance(project).findDirectory(dbDir).add(initFile);
            });
                virtualFile = dbDir.findChild(name);
        }
    }
}
