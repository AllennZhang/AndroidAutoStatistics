package com.hipac.codeless.plugin

import jdk.internal.org.objectweb.asm.Opcodes
/**
 * Created by zhangdan on 17/3/3.
 */

public class ReWriterConfig {

    /**
     * 必须是全路径形式(字节码组织形式)，不是点分形式的包名 com/codelessda/demo/codeless/agent/PluginAgent.java
     *
     */
    public static String sAgentClassName = 'com/hipac/codeless/agent/PluginAgent'

//    String name
//    String desc
//    String parent
//    String agentName
//    String agentDesc
//    String paramsStart
//    String paramsCount
//    List[] opcodes

    /*
    ('I',Opcodes.ILOAD);// I: int , retrieve integer from local variable
    ('Z',Opcodes.ILOAD);// Z: bool , retrieve integer from local variable
    ('J',Opcodes.LLOAD);// J: long , retrieve long from local variable
    ('F',Opcodes.FLOAD);// F: float , retrieve float from local variable
    ('D',Opcodes.DLOAD);// D: double , retrieve double from local variable
    */

    /**
     * interface中的方法
     */
    public final static HashMap<String, MethodCell> sInterfaceMethods = new HashMap<>()
    static {
        sInterfaceMethods.put('onClick(Landroid/view/View;)V', new MethodCell(
                'onClick',
                '(Landroid/view/View;)V',
                'android/view/View$OnClickListener',
                'onClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sInterfaceMethods.put('onClick(Landroid/content/DialogInterface;I)V', new MethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;I)V',
                'android/content/DialogInterface$OnClickListener',
                'onClick',
                '(Ljava/lang/Object;Landroid/content/DialogInterface;I)V',
                0, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
         sInterfaceMethods.put('onItemClick(Landroid/widget/AdapterView;Landroid/view/View;IJ)V', new MethodCell(
                 'onItemClick',
                 '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                 'android/widget/AdapterView$OnItemClickListener',
                 'onItemClick',
                 '(Ljava/lang/Object;Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                 0, 5,
                 [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.LLOAD]))
        sInterfaceMethods.put('onItemSelected(Landroid/widget/AdapterView;Landroid/view/View;IJ)V', new MethodCell(
                'onItemSelected',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'android/widget/AdapterView$OnItemSelectedListener',
                'onItemSelected',
                '(Ljava/lang/Object;Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                0, 5,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.LLOAD]))
        sInterfaceMethods.put('onGroupClick(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z', new MethodCell(
                'onGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z',
                'android/widget/ExpandableListView$OnGroupClickListener',
                'onGroupClick',
                '(Ljava/lang/Object;Landroid/widget/ExpandableListView;Landroid/view/View;IJ)V',
                0, 5,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.LLOAD]))
        sInterfaceMethods.put('onChildClick(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z', new MethodCell(
                'onChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z',
                'android/widget/ExpandableListView$OnChildClickListener',
                'onChildClick',
                '(Ljava/lang/Object;Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)V',
                0, 6,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.LLOAD]))
        sInterfaceMethods.put('onRatingChanged(Landroid/widget/RatingBar;FZ)V', new MethodCell(
                'onRatingChanged',
                '(Landroid/widget/RatingBar;FZ)V',
                'android/widget/RatingBar$OnRatingBarChangeListener',
                'onRatingChanged',
                '(Ljava/lang/Object;Landroid/widget/RatingBar;FZ)V',
                0, 4,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.FLOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onStopTrackingTouch(Landroid/widget/SeekBar;)V', new MethodCell(
                'onStopTrackingTouch',
                '(Landroid/widget/SeekBar;)V',
                'android/widget/SeekBar$OnSeekBarChangeListener',
                'onStopTrackingTouch',
                '(Ljava/lang/Object;Landroid/widget/SeekBar;)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ALOAD]))
        sInterfaceMethods.put('onCheckedChanged(Landroid/widget/CompoundButton;Z)V', new MethodCell(
                'onCheckedChanged',
                '(Landroid/widget/CompoundButton;Z)V',
                'android/widget/CompoundButton$OnCheckedChangeListener',
                'onCheckedChanged',
                '(Ljava/lang/Object;Landroid/widget/CompoundButton;Z)V',
                0, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onCheckedChanged(Landroid/widget/RadioGroup;I)V', new MethodCell(
                'onCheckedChanged',
                '(Landroid/widget/RadioGroup;I)V',
                'android/widget/RadioGroup$OnCheckedChangeListener',
                'onCheckedChanged',
                '(Ljava/lang/Object;Landroid/widget/RadioGroup;I)V',
                0, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))

        // Todo: 扩展
        //Dialog show dismiss
//        sInterfaceMethods.put('onShow(Landroid/content/DialogInterface;)V',new MethodCell(
//                'onShow',
//                '(Landroid/content/DialogInterface;)V',
//                'android/content/DialogInterface$OnShowListener',
//                'onDialogShow',
//                '(Ljava/lang/Object;Landroid/content/DialogInterface;)V',
//                0,2,
//                [Opcodes.ALOAD,Opcodes.ALOAD]
//        ))
//
//        sInterfaceMethods.put('onDismiss(Landroid/content/DialogInterface;)V',new MethodCell(
//                'onDismiss',
//                '(Landroid/content/DialogInterface;)V',
//                'android/content/DialogInterface$OnDismissListener',
//                'onDialogDisappear',
//                '(Ljava/lang/Object;Landroid/content/DialogInterface;)V',
//                0,2,
//                [Opcodes.ALOAD,Opcodes.ALOAD]
//        ))


    }

    /**
     * Fragment中的方法 用于页面级别的上报统计
     */
    public final static HashMap<String, MethodCell> sFragmentMethods = new HashMap<>()
    static {
        sFragmentMethods.put('onResume()V', new MethodCell(
                'onResume',
                '()V',
                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                'onFragmentResume',
                '(Ljava/lang/Object;)V',
                0, 1,
                [Opcodes.ALOAD]))
        sFragmentMethods.put('onPause()V', new MethodCell(
                'onPause',
                '()V',
                '',
                'onFragmentPause',
                '(Ljava/lang/Object;)V',
                0, 1,
                [Opcodes.ALOAD]))
        sFragmentMethods.put('onActivityCreated(Landroid/os/Bundle;)V',new MethodCell(
                'onActivityCreated',
                '(Landroid/os/Bundle;)V',
                '',
                'onFragmentActivityCreated',
                '(Ljava/lang/Object;)V',
                0,1,
                [Opcodes.ALOAD]))
//        sFragmentMethods.put('setUserVisibleHint(Z)V', new MethodCell(
//                'setUserVisibleHint',
//                '(Z)V',
//                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
//                'setFragmentUserVisibleHint',
//                '(Ljava/lang/Object;Z)V',
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        sFragmentMethods.put('onHiddenChanged(Z)V', new MethodCell(
//                'onHiddenChanged',
//                '(Z)V',
//                '',
//                'onFragmentHiddenChanged',
//                '(Ljava/lang/Object;Z)V',
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
    }

    /**
     * Activity中的方法 用于页面级别的上报统计
     */
    public final static HashMap<String,MethodCell> sActivityMethods = new HashMap<>()
    static {
        sActivityMethods.put('onResume()V',new MethodCell(
                             'onResume',
                             '()V',
                             '',
                             'onActivityResume',
                             '(Ljava/lang/Object;)V',
                              0,1,
                              [Opcodes.ALOAD]))
        sActivityMethods.put('onPause()V',new MethodCell(
                'onPause',
                '()V',
                '',
                'onActivityPause',
                '(Ljava/lang/Object;)V',
                0,1,
                [Opcodes.ALOAD]))
        sActivityMethods.put('onCreate(Landroid/os/Bundle;)V',new MethodCell(
                'onCreate',
                '(Landroid/os/Bundle;)V',
                '',
                'onActivityCreate',
                '(Ljava/lang/Object;)V',
                0,1,
                [Opcodes.ALOAD,Opcodes.ALOAD]))
        sActivityMethods.put('onNewIntent(Landroid/content/Intent;)V',new MethodCell(
                'onNewIntent',
                '(Landroid/content/Intent;)V',
                '',
                'onActivityNewIntent',
                '(Ljava/lang/Object;)V',
                0,1,
                [Opcodes.ALOAD]))
    }

}
