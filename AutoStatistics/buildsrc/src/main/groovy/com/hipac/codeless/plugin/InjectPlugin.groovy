package com.hipac.codeless.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class InjectPlugin implements Plugin<Project>{
    public static File pluginTmpDir
    @Override
    void apply(Project project) {
      project.logger.error("=============statPlugin Inject apply========================")
        //添加gradle 配置
        project.extensions.create("traceConfig",ConfigExtention)
        initDir(project)
        regeister (project)
        //test
//        project.task('testPlugin') << {
//            project.logger.debug("=============statPlugin test========================")
//        }

    }

    public void regeister(Project project){
        def android = project.extensions.findByType(AppExtension)
        android.registerTransform(new InjectTransform(project))
    }

    /**
     * 保存修改后的临时文件
     * @param project
     */
    public static void initDir(Project project){
        if (!project.buildDir.exists()){
            project.buildDir.mkdirs()
        }

        File file = new File(project.buildDir,'StatInject')
        if (!file.exists()){
            file.mkdir()
        }
        pluginTmpDir = file
    }
}