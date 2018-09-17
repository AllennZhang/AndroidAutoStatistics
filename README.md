# AndroidAutoStatistics
### Android无痕埋点sdk，实现所有页面曝光（onResume()）和点击事件（onClick()...）的自动埋点统计
```
主要思路：鉴于手动埋点sdk诸如百度统计，友盟统计等都需要在BaseActivity的onResume（），onPause(）方法手动调用埋点api，
        点击事件的埋点需要在view的onClick方法里面手动调用埋点api，前提是需要在网页注册统计的事件id和参数。
        第一步：如果能够自动实现在这些需要统计的方法里面植入我们的代理方法即可实现自动埋点，即使用Aop来实现hook，
        aop的框架很多aspectJ,javassist,ASM，这里借助gradle1.5.0之后的Transform API及ASM实现自定义gradle插件。
        第二步：在第一步的基础上在植入的代理方法实现自己的业务逻辑，viewPath,viewPage的获取，业务数据的获取
        第三步：数据上报与映射，viewPath对应的就是百度统计平台注册的事件id。
        这里特别感谢nailperry给出的经验分享：https://www.jianshu.com/p/250c83449dc0
 ```       
  
### 项目结构
<div>
  <img src="https://github.com/YouriZhang/imagefolder/blob/master/autotrace.png" width="300" height="400"/> 
</div>    



#### 基于ASM及Transform API开发的一个支持字节码插桩的Gradle 插件

```
buildsrc module   gradle本地插件

ConfigExtention:用于集成的项目中app build.gradle进行的配置项
use like this:
apply plugin: 'trace_plugin'

traceConfig{
    showLog = false
    keepModifiedJar = true
    //需要被hook的class都需要在此列表配置,可以具体到类，也可以是包名，
    // note: 主项目的包名默认插件运行时自动获取，故无需再对本应用自身包名进行配置

    targetPackages=['com.yt.statistics.statplugin.base.BaseActivity','com.yt.statistics.statplugin.base.BaseFragment']
}

ReWriterConfig: 以字节码形式组织的配置，对方法组织形式为MethodCell
               sAgentClassName为指定的代理类，实现了需要被hook方法的具体逻辑

```

```
track module 数据存储与上报sdk
 core包为核心类

```

```
debugger module debug调试工具

```

## 感谢
* https://github.com/nailperry-zd/CodelessDA-Gradle-Plugin

* https://github.com/BryanSharp/hibeaver

