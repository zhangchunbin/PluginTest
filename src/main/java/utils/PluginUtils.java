package utils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
/**
 * ================================
 *
 * @author: zcb
 * @email: zhang-cb@foxmail.com
 * @time: 2020/2/19 10:52 上午
 * @version: 1.0
 * @description: =================================
 */
public class PluginUtils {
    /**
     * 获取Java文件的Class类对象
     */
    public static PsiClass getFileClass(PsiFile file) {
        for (PsiElement psiElement : file.getChildren()) {
            if (psiElement instanceof PsiClass) {
                return (PsiClass) psiElement;
            }
        }
        return null;
    }
}
