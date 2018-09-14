package com.hipac.codeless.plugin;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class MethodLogVisitor extends MethodVisitor {

    public static final boolean showLog = true;

    public MethodLogVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv);
    }

    /**
     * turn "com.bryansharp.util" to "com/bryansharp/util"
     *
     * @param classname full class name
     * @return class path
     */
    public static String className2Path(String classname) {
        return classname.replace('.', '/');
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc,itf);
        showLog("======================visitMethodInsn================");
        showLog(opcode,owner,name,desc,itf);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
        showLog("======================visitAttribute===============");
        showLog(attribute);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        showLog("===================visitEnd======================");

    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
        showLog("======================visitFieldInsn=================");
        showLog(opcode,owner,name,desc);
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        super.visitFrame(type, nLocal, local, nStack, stack);
        showLog("=====================visitFrame=====================");
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        showLog("=====================visitLabel======================");

    }

    @Override
    public void visitLineNumber(int line, Label label) {
        super.visitLineNumber(line, label);
        showLog("=====================visitLineNumber==================");
        showLog(line,label);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        super.visitIincInsn(var, increment);
        showLog("======================visitIincInsn==================");
    }

    @Override
    public void visitIntInsn(int i, int i1) {
        super.visitIntInsn(i, i1);
        showLog("======================visitIntInsn==================");
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        showLog("=======================visitMaxs=====================");
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        showLog("=====================visitVarInsn====================");
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);
        showLog("=====================visitJumpInsn===================");
    }

    @Override
    public void visitLdcInsn(Object o) {
        super.visitLdcInsn(o);
        showLog("=========================visitLdcInsn=================");
    }

    @Override
    public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {
        super.visitLookupSwitchInsn(label, ints, labels);
        showLog("=====================visitLookupSwitchInsn=================");
    }

    @Override
    public void visitMultiANewArrayInsn(String s, int i) {
        super.visitMultiANewArrayInsn(s, i);
        showLog("====================visitMultiANewArrayInsn=================");
    }

    @Override
    public void visitTableSwitchInsn(int i, int i1, Label label, Label[] labels) {
        super.visitTableSwitchInsn(i, i1, label, labels);
        showLog("=====================visitMethodInsn================");
    }

    @Override
    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
        super.visitTryCatchBlock(label, label1, label2, s);
        showLog("=====================visitTryCatchBlock=============");
    }

    @Override
    public void visitTypeInsn(int opcode, String s) {
        super.visitTypeInsn(opcode, s);
        showLog("=====================visitTypeInsn==============");
    }

    @Override
    public void visitCode() {
        super.visitCode();
        showLog("=================visitCode===================");
    }

    @Override
    public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i) {
        super.visitLocalVariable(s, s1, s2, label, label1, i);
        showLog("================visitLocalVariable=================");
        showLog(s,s1,s2,label,label1,i);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        showLog("=================visitInsn==================");
        showLog(opcode);
    }


    public static void showLog(Object... params){
        if (!showLog) return;
        for (Object param : params) {
            System.out.println(param+"");
        }
    }
}
