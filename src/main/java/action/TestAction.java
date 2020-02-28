package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;

import ui.MvvmForm;
import ui.TestDialog;
import utils.PluginUtils;

/**
 * ================================
 *
 * @author: zcb
 * @email: zhang-cb@foxmail.com
 * @time: 2020/2/19 10:50 上午
 * @version: 1.0
 * @description: =================================
 */
public class TestAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        PsiFile file = e.getData(PlatformDataKeys.PSI_FILE);

        PsiClass clazz = PluginUtils.getFileClass(file);
        ArrayList<PsiField> fields = new ArrayList<>();
        PsiField priKeyField = null;
//        for (int i = 0; i < clazz.getFields().length; i++) {
//            fields.add(clazz.getFields()[i]);
//        }
//        WriteCommandAction.runWriteCommandAction(project, () ->
//                DatabaseGenerator.genCode(file,clazz,fields,clazz.getFields()[0])
//        );

        TestDialog dialog = new TestDialog();
        dialog.pack();
        dialog.setVisible(true);

        Messages.showMessageDialog(project,"this is test","TestAction", Messages.getInformationIcon());
    }
}
