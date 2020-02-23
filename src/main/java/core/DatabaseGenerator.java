package core;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import utils.AndroidUtils;
import utils.PluginUtils;

import java.io.IOException;
import java.util.ArrayList;
public class DatabaseGenerator {

    public static void genCode(PsiFile file,PsiClass clazz,ArrayList<PsiField> fields,PsiField prikeyField){
        Project project = file.getProject();
        //app包名根目录 ...\app\src\main\java\PACKAGE_NAME\
        VirtualFile baseDir = AndroidUtils.getAppPackageBaseDir(project);

        //判断跟目录下面是否有db文件夹
        VirtualFile dbDir = baseDir.findChild("db");
        if (dbDir==null){
            //没有就创建一个
            try {
                dbDir = baseDir.createChildDirectory(null,"db");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //生成 SqliteOpenHelper类
        genHelperFile(clazz,fields,prikeyField,project,dbDir);

        //生成数据类对应的Columns字段
        genColumnFile(clazz,fields,project,dbDir);
        //生成 每个数据类创建一个Dao类，包括基本的curd方法
        genDaoCode(clazz,fields,prikeyField,project,dbDir);

    }


    private static void genHelperFile(PsiClass clazz,ArrayList<PsiField> fields,PsiField priKeyField,
                                      Project project,VirtualFile dbDir){
        String name = "DatabaseHelper.java";
        VirtualFile virtualFile = dbDir.findChild(name);
        if (virtualFile == null){
            //第一步：么有这个文件，第一次使用代码字符串创建类
            PsiFile initFile = PsiFileFactory.getInstance(project).createFileFromText(name
                    ,JavaFileType.INSTANCE
                    ,CodeFactory.genSqliteOpenHelperInitCode(dbDir));
            //第二布： 把生产的文件加到db目录下
            PsiManager.getInstance(project).findDirectory(dbDir).add(initFile);
            virtualFile = dbDir.findChild(name);
        }

        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        // 用拼接的代码生成create table方法
        String createTableCode = CodeFactory.genCreateTableCode(clazz, fields, priKeyField);
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiMethod createTableMethod = factory.createMethodFromText(createTableCode, psiFile);
        // 将创建的method添加到DatabaseHelper Class中
        PsiClass fileClass = PluginUtils.getFileClass(psiFile);
        fileClass.add(createTableMethod);
        // 在DatabaseHelper类中的onCreate方法里，添加create table方法的调用语句
        PsiMethod onCreateMethod = fileClass.findMethodsByName("onCreate", false)[0];
        onCreateMethod.getBody().add(factory.createStatementFromText(createTableMethod.getName() + "(db);", fileClass));

    }
    private static void genColumnFile(PsiClass clazz, ArrayList<PsiField> fields, Project project, VirtualFile dbDir) {
        String name = "DataContract.java";
        VirtualFile virtualFile = dbDir.findChild(name);
        if(virtualFile == null) {
            // 没有就创建一个，第一次使用代码字符串创建个类
            PsiFile initFile = PsiFileFactory.getInstance(project).createFileFromText(
                    name, JavaFileType.INSTANCE, CodeFactory.genDataContractInitCode(dbDir));

            // 加到db目录下
            PsiManager.getInstance(project).findDirectory(dbDir).add(initFile);
            virtualFile = dbDir.findChild(name);
        }

        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        // 用拼接的代码生成Columns Class
        String beanColumnsCode = CodeFactory.genBeanColumnsCode(clazz, fields);
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiClass beanColumnsClass = factory.createClassFromText(beanColumnsCode, psiFile);
        // 将创建的class添加到DataContract Class中
        PsiClass fileClass = PluginUtils.getFileClass(psiFile);
        fileClass.add(beanColumnsClass.getInnerClasses()[0]);
    }

    private static void genDaoCode(PsiClass clazz, ArrayList<PsiField> fields, PsiField priKeyField,
                                   Project project, VirtualFile dbDir) {
        String name = clazz.getName() + "Dao.java";
        // 使用代码字符串创建个类
        PsiFile initFile = PsiFileFactory.getInstance(project).createFileFromText(
                name, JavaFileType.INSTANCE, CodeFactory.genDaoCode(clazz, fields, priKeyField, dbDir));

        // 加到db目录下
        PsiManager.getInstance(project).findDirectory(dbDir).add(initFile);
    }



}
