import InjectTransform
import com.hipac.codeless.plugin.MethodCell
import com.hipac.codeless.plugin.MethodLogVisitor
import com.hipac.codeless.plugin.ReWriterConfig
import org.gradle.api.Project
import org.objectweb.asm.*

public class MethodFilterClassVisitor extends ClassVisitor{
    private ClassVisitor mVistor
    private String className
    private String superName
    private String[] interfaces
    private boolean onlyVisit = false //仅访问而不修改类
//    public HashSet<String> visitedFragMentMethods = new HashSet<>()
//    public HashSet<String>  visitedActvityMethods = new HashSet<>()
    public static HashSet<String> shouldModifyjar = new HashSet<>()

    /**
     *
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or
     *            INVOKEINTERFACE.
     * @param owner
     *            the internal name of the method's owner class (see
     *            {@link org.objectweb.asm.Type#getInternalName() getInternalName}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link org.objectweb.asm.Type Type}).
     * @param start 方法参数起始索引（ 0：this，1+：普通参数 ）
     *
     * @param count 方法参数个数
     *
     * @param paramOpcodes 参数类型对应的ASM指令
     *
     */

    private static void visitMethodWithLoadedParams(MethodVisitor methodVisitor, int opcode, String owner, String methodName, String methodDesc, int start, int count, List<Integer> paramOpcodes) {
        for (int i = start; i < start + count; i++) {
            methodVisitor.visitVarInsn(paramOpcodes[i - start], i);
        }
        methodVisitor.visitMethodInsn(opcode, owner, methodName, methodDesc, false);
    }


   public MethodFilterClassVisitor(ClassVisitor visitor) {
        super(Opcodes.ASM5, visitor)
        mVistor = visitor
        initModifyPackage()
    }

    private void initModifyPackage(){
        if (shouldModifyjar == null){
            shouldModifyjar = new HashSet<>()
        }
        //保证执行一次，避免浪费
        if (shouldModifyjar.isEmpty()){
            Project project = InjectTransform.getProject()
            if (project != null){
                HashSet<String> inputPackages = project.traceConfig.targetPackages
                if (inputPackages != null  && inputPackages.size() >0){
                    shouldModifyjar.addAll(inputPackages)
                }
            }
        }
    }


    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        println("version: "+version+"==access: "+access+"==name: "+name+"==signature:"+signature+"==superName: "+superName)
        this.className = name
        this.superName = superName
        this.interfaces = interfaces
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    void visitSource(String source, String debug) {
        super.visitSource(source, debug)
    }

    @Override
    void visitOuterClass(String owner, String name, String desc) {
        super.visitOuterClass(owner, name, desc)
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return super.visitAnnotation(desc, visible)
    }

    @Override
    AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitTypeAnnotation(i, typePath, s, b)
    }

    @Override
    void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute)
    }

    @Override
    void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access)
    }

    @Override
    FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return super.visitField(access, name, desc, signature, value)
    }

    /**
     * 这里使我们hook被代理方法的入手点
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor newMethodVistor = null
        if (interfaces != null && interfaces.length >0){
            MethodCell methodCell = ReWriterConfig.sInterfaceMethods.get(name+desc)
            if (methodCell != null && interfaces.contains(methodCell.parent)){
                if (onlyVisit){
                    newMethodVistor = new MethodLogVisitor(mVistor.visitMethod(access, name, desc, signature, exceptions))
                }else {
                    try {
                        MethodVisitor methodVisitor = mVistor.visitMethod(access, name, desc, signature, exceptions)
                        newMethodVistor = new MethodLogVisitor(methodVisitor) {
                            @Override
                            void visitCode() {
                                super.visitCode()
                                //插入修改代码
//                                println("MethodVistor-modifing: "+methodCell.name+desc)
                                visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassName, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                            }
                        }
                    }catch (Exception e){
                        println("MethodVistor-visitMethod: "+e.toString())
                        newMethodVistor = null
                    }
                }
            }
        }

        if (instanceOfFragment(superName)) {
            if (ReWriterConfig.sFragmentMethods.containsKey(name + desc)){
                MethodCell methodCell = ReWriterConfig.sFragmentMethods.get(name + desc)
            if (methodCell != null) {
                if (onlyVisit) {
                    newMethodVistor = new MethodLogVisitor(mVistor.visitMethod(access, name, desc, signature, exceptions))
                } else {
                 try {
                     MethodVisitor methodVisitor1 = mVistor.visitMethod(access, name, desc, signature, exceptions)
                     newMethodVistor = new MethodLogVisitor(methodVisitor1) {
                         @Override
                         void visitCode() {
                             super.visitCode()
                             visitMethodWithLoadedParams(methodVisitor1, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassName, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                         }
                     }
                 }catch (Exception e){
                     println("MethodVistor-instanceOfActivity-visitMethod: "+e.toString())
                     newMethodVistor = null
                  }
                 }
            }
         }
        }else if (instanceOfActivity(superName)) {
            if (ReWriterConfig.sActivityMethods.containsKey(name + desc)){
                MethodCell methodCell = ReWriterConfig.sActivityMethods.get(name + desc)
                if (methodCell != null) {
                    if (onlyVisit) {
                        newMethodVistor = new MethodLogVisitor(mVistor.visitMethod(access, name, desc, signature, exceptions))
                    } else {
                        try {
                            MethodVisitor methodVisitor2 = mVistor.visitMethod(access, name, desc, signature, exceptions)
                            newMethodVistor = new MethodLogVisitor(methodVisitor2) {
                                @Override
                                void visitCode() {
                                    super.visitCode()
                                    visitMethodWithLoadedParams(methodVisitor2, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassName, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                                }
                            }
                        }catch (Exception e){
                            println("MethodVistor-instanceOfActivity-visitMethod: "+e.toString())
                            newMethodVistor = null
                        }
                    }
                }
            }
        }else if (isBaseActivity(className)){
            if (ReWriterConfig.sActivityMethods.containsKey(name + desc)){
                MethodCell methodCell = ReWriterConfig.sActivityMethods.get(name + desc)
                if (methodCell != null) {
                    if (onlyVisit) {
                        newMethodVistor = new MethodLogVisitor(mVistor.visitMethod(access, name, desc, signature, exceptions))
                    } else {
                       try {
                           MethodVisitor methodVisitor3 = mVistor.visitMethod(access, name, desc, signature, exceptions)
                           newMethodVistor = new MethodLogVisitor(methodVisitor3) {
                               @Override
                               void visitCode() {
                                   super.visitCode()
                                   visitMethodWithLoadedParams(methodVisitor3, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassName, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                               }
                           }
                       }catch (Exception e){
                           println("MethodVistor-isBaseActivity-visitMethod: "+e.toString())
                           newMethodVistor = null
                       }
                    }
                }
            }
        }else if (isBaseFragment(className)){
            if (ReWriterConfig.sFragmentMethods.containsKey(name + desc)){
                MethodCell methodCell = ReWriterConfig.sFragmentMethods.get(name + desc)
                if (methodCell != null) {
                    if (onlyVisit) {
                        newMethodVistor = new MethodLogVisitor(mVistor.visitMethod(access, name, desc, signature, exceptions))
                    } else {
                        try {
                            MethodVisitor methodVisitor4 = mVistor.visitMethod(access, name, desc, signature, exceptions)
                            newMethodVistor = new MethodLogVisitor(methodVisitor4) {
                                @Override
                                void visitCode() {
                                    super.visitCode()
                                    visitMethodWithLoadedParams(methodVisitor4, Opcodes.INVOKESTATIC, ReWriterConfig.sAgentClassName, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
                                }
                            }
                        }catch (Exception e){
                            println("MethodVistor-isBaseFragment-visitMethod: "+e.toString())
                            newMethodVistor = null
                        }
                    }
                }
            }
        }

        if (newMethodVistor != null){
            return newMethodVistor
        }else {
            return mVistor.visitMethod(access,name,desc,signature,exceptions)
        }

    }

    @Override
    void visitEnd() {
        super.visitEnd()

    }



    /**
     * 扫描fragment的子类
     * @param superName
     * @return
     */
    private static boolean instanceOfFragment(String superName) {
        return superName.equals('android/app/Fragment') || superName.equals('android/support/v4/app/Fragment')
    }


    /**
     * 扫描activity的子类
     * @param superName
     * @return
     */
    private static boolean instanceOfActivity(String superName){
        return superName.equals('android/support/v7/app/AppCompatActivity') || superName.equals('android/support/v4/app/FragmentActivity') || superName.equals('android/app/Activity')
    }


    private static boolean isBaseActivity(String classFullName){
        boolean isBase = false
        if (classFullName == null){
            isBase = false
        }
        String newName = classFullName.replace(File.separator,".")
        for (String className : shouldModifyjar){
            if (className.equals(newName) && newName.indexOf("BaseActivity") != -1/*newName.contains("BaseActivity")*/){
                isBase = true
                break
            }
        }
        return isBase
    }

    private static boolean isBaseFragment(String classFullName){
        boolean isBase = false
        if (classFullName == null){
            isBase = false
        }
        String newName = classFullName.replace(File.separator,".")
        for (String className : shouldModifyjar){
            if (className.equals(newName) && newName.indexOf("BaseFragment") != -1/*newName.contains("BaseFragment")*/){
                isBase = true
                break
            }
        }
        return isBase
    }
}