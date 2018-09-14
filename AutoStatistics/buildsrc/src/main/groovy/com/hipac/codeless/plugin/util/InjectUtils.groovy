import com.hipac.codeless.plugin.InjectPlugin
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import Utils
import InjectTransform
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import MethodFilterClassVisitor

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

public class InjectUtils{

    /**
     * //修改class文件
     * @param className 类名
     * @param sourceBytes 源文件字节码
     * @return
     */
  static byte[] modifyClasses(String className,byte[] sourceBytes){
    byte[] classBytesCode = null
    try {
      println("====start modifying ${className}====");
      classBytesCode = modifyClass(sourceBytes);
//      println("====revisit modified ${className}====");
      onlyVisitClassMethod(classBytesCode);
      println("====finish modifying ${className}====");
    }catch (Exception e){
      println("InjectUtils-modifyClass-error:"+e.toString())
    }
    if (classBytesCode == null){
        classBytesCode = sourceBytes
    }
    return classBytesCode
  }


private static byte[] modifyClass(byte[] srcClass) throws IOException {
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
    ClassVisitor methodFilterCV = new MethodFilterClassVisitor(classWriter)
    ClassReader cr = new ClassReader(srcClass)
    cr.accept(methodFilterCV, 0);
    return classWriter.toByteArray();
  }


  private static void onlyVisitClassMethod(byte[] srcClass) throws IOException {
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    MethodFilterClassVisitor methodFilterCV = new MethodFilterClassVisitor(classWriter);
    methodFilterCV.onlyVisit = true
    ClassReader cr = new ClassReader(srcClass)
    cr.accept(methodFilterCV, 0)
  }


  static File modifyClassFile(File dir,File classFile,File tempDir){
    File modified
    try {
      String className = Utils.path2Classname(classFile.getAbsolutePath().replace(dir.absolutePath+File.separator,""))
//      println("InjectUtil-modifyClassFile-className:"+className)
      //读取源文件
      byte[] sourceBytes = IOUtils.toByteArray(new FileInputStream(classFile))
      if (shouldModifyClass(className)){
        byte[] modifiedBytes = InjectUtils.modifyClasses(className,sourceBytes)
        if (modifiedBytes){
          modified = new File(tempDir,className.replace('.','')+".class")
          if (modified.exists()){
              modified.delete()
          }
          modified.createNewFile()
          new FileOutputStream(modified).write(modifiedBytes)
        }
      }

    }catch (Exception e){
      println(e.toString())
    }
    return modified
  }


  static boolean shouldModifyClass(String className){
    if (className == null ){
       return false
    }
    if (InjectTransform.targetPackages != null){
      Iterator<String> iterator = InjectTransform.targetPackages.iterator()
      // 注意，闭包里的return语句相当于continue，不会跳出遍历，故用while或for
      while (iterator.hasNext()){
        String packageName = iterator.next()
        if (className.contains(packageName)){
          //过滤系统自动生成的类
          return (!className.contains("R\$") && !className.endsWith("R") && !className.endsWith("BuildConfig"))
        }
      }
    }
    return false
  }

  public static void saveModifiedJarForCheck(File optJar) {
    if (InjectPlugin.pluginTmpDir == null){
      return
    }
    File checkJarFile = new File(InjectPlugin.pluginTmpDir, optJar.getName())
    if (checkJarFile.exists()) {
      checkJarFile.delete()
    }
    FileUtils.copyFile(optJar, checkJarFile);
  }

  public static boolean isJarNeedModify(File soureFile){
       boolean modify = false
       if (InjectTransform.targetPackages != null && InjectTransform.targetPackages.size()>0){
          if (soureFile){
            //读取原jar文件
            JarFile jarFile = new JarFile(soureFile)
            Enumeration<JarEntry> enumeration = jarFile.entries()
            while (enumeration.hasMoreElements()){
              JarEntry jarEntry = enumeration.nextElement()
              String entryName = jarEntry.getName()
             if (entryName.endsWith(".class")){
               String className = entryName.replace(File.separator,".").replace(".class","")
               if (shouldModifyClass(className)){
                 modify = true
                 break
               }
             }
            }
          }
       }
    return modify
  }

  public static File modifyJarFile(File jarFile,File tempDir){
     if (jarFile){
       //设置输出文件
         String hexName = DigestUtils.md5Hex(jarFile.absolutePath).substring(0,8)
         File optJar = new File(tempDir,hexName+jarFile.name)
         JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

       //读取原jar文件
       JarFile jarF = new JarFile(jarFile)
       Enumeration<JarEntry> enumeration = jarF.entries()
       while (enumeration.hasMoreElements()){
         JarEntry jarEntry = enumeration.nextElement()
         InputStream inputStream = jarF.getInputStream(jarEntry)
         String entryName = jarEntry.getName()
         ZipEntry zipEntry = new ZipEntry(entryName)
         jarOutputStream.putNextEntry(zipEntry)
         byte[] modifiedClassBytes = null
         byte[] sourceBytes = IOUtils.toByteArray(inputStream)
         if (entryName.endsWith(".class")) {
          String className = Utils.path2Classname(entryName)
           if (shouldModifyClass(className)) {
             println("${hexName} is modifing")
             modifiedClassBytes = InjectUtils.modifyClasses(className, sourceBytes)
           }
         }
         if (modifiedClassBytes == null) {
           jarOutputStream.write(sourceBytes);
         } else {
           jarOutputStream.write(modifiedClassBytes);
         }
         jarOutputStream.closeEntry();
       }
       println("${hexName} is modified")
       jarOutputStream.close()
       jarF.close()
       return optJar
     }
    return null
  }
}