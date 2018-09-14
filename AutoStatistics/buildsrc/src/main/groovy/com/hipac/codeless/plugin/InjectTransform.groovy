import InjectUtils
import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.hipac.codeless.plugin.ReWriterConfig
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
/**Transform API
 * name: 给 transform 起个名字。 这个 name 并不是最终的名字， 在 TransformManager 中会对名字再处理：
 *
 * inputTypes: transform 要处理的数据类型。
 * CLASSES 表示要处理编译后的字节码，可能是 jar 包也可能是目录
 * RESOURCES 表示处理标准的 java 资源
 *
 *
 * scopes：transform 的作用域
 *   type	                 Des
 * PROJECT	             只处理当前项目
 * SUB_PROJECTS	         只处理子项目
 * PROJECT_LOCAL_DEPS	 只处理当前项目的本地依赖,例如jar, aar
 * EXTERNAL_LIBRARIES	 只处理外部的依赖库
 * PROVIDED_ONLY	     只处理本地或远程以provided形式引入的依赖库
 * TESTED_CODE	         测试代码
 *
 *
 * isIncremental : 当前 Transform 是否支持增量编译
 *
 *
 *  addTransform 方法在执行过程中，会将 Transform 包装成一个 AndroidTask 对象。
 *  所以可以理解为一个 Transform 就是一个 Task
 *
 */

public class InjectTransform extends Transform{

   private static Project project
    //默认添加
    static HashSet<String> targetPackages = ['butterknife.internal.DebouncingOnClickListener'];
    static AppExtension  android

    InjectTransform(Project project) {
        this.project = project
    }

    public static Project getProject(){
        return project
    }


    @Override
    String getName() {
        return "stat"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        android = project.extensions.getByType(AppExtension)
        String packageName = getAppPackageName()
        if (packageName != null){
            println("===packageName======: "+packageName)
            //添加本应用包名下的class
            targetPackages.add(packageName)
        }

        if (!Utils.emptyString(project.traceConfig.pluginAgentName)) {
            ReWriterConfig.sAgentClassName = project.traceConfig.pluginAgentName
        }
        //添加配置文件里面的class
        HashSet<String> inputPackages = project.traceConfig.targetPackages
        if (inputPackages != null && inputPackages.size() >0){
            targetPackages.addAll(inputPackages)
        }

        //  Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        def Context context = transformInvocation.getContext()
        def Collection<TransformInput> inputs = transformInvocation.inputs
        def TransformOutputProvider  outputProvider = transformInvocation.outputProvider
       //遍历目录
        inputs.each {TransformInput input->
            input.directoryInputs.each {DirectoryInput directoryInput->
                //输出目录
                def dest =    outputProvider.getContentLocation(directoryInput.name,directoryInput.contentTypes,directoryInput.scopes,Format.DIRECTORY)
                File dir = directoryInput.file
                if (dir){
                    HashMap<String,File> modifyMap = new HashMap<>()
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                        File classFile ->
                   //=================TODO注入自己的代码====================
                            File modified = InjectUtils.modifyClassFile(dir, classFile, context.getTemporaryDir());
                            if (modified != null) {
                                //key为相对路径
                                modifyMap.put(classFile.absolutePath.replace(dir.absolutePath, ""), modified);
                            }
                    }
                     //拷贝目录
                    FileUtils.copyDirectory(directoryInput.file,dest)
                    modifyMap.entrySet().each {Map.Entry<String,File> entry ->
                        File target = new File(dest.absolutePath+entry.getKey())
                        if (target.exists()){
                            target.delete()
                        }
                        //拷贝文件
                        FileUtils.copyFile(entry.getValue(),target)

//                        if (project.statConfig.keepModifiedJar){
//
//                        }
                        //保存修改后的class输出文件
                        InjectUtils.saveModifiedJarForCheck(entry.getValue())
                        entry.getValue().delete()
                    }
                }


            }

        }


        //遍历jar文件
        inputs.each {TransformInput input->
            input.jarInputs.each {JarInput jarInput->

                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, destName.length() - 4);
                }
                //输出目录
                def dest =  outputProvider.getContentLocation(jarName+md5Name,jarInput.contentTypes,jarInput.scopes,Format.JAR)
                File modifiedJar = null
                if (InjectUtils.isJarNeedModify(jarInput.file)){
                    modifiedJar = InjectUtils.modifyJarFile(jarInput.file,context.getTemporaryDir())
                }
                if (modifiedJar == null){
                    modifiedJar = jarInput.file
                }else {
                    InjectUtils.saveModifiedJarForCheck(modifiedJar)
                }
                FileUtils.copyFile(modifiedJar,dest)
            }
        }
        println("=============statPlugin Inject end========================")
    }


    /**
     * 获取应用程序包名
     * @return
     */
    private static String getAppPackageName() {
        String packageName
        try {
            def manifestFile = android.sourceSets.main.manifest.srcFile
            println("XmlParser manifestFile: " + manifestFile)
            packageName = new XmlParser().parse(manifestFile).attribute('package')
            println("XmlParser packageName: " + packageName)
        } catch (Exception e) {
            println("XmlParser Exception: " + e.getMessage())
        }
        return packageName
    }


}