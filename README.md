# AndroidAutoStatistics
### Android无痕埋点sdk，实现所有页面曝光（onResume()）和点击事件（onClick()...）的自动埋点统计



## 基于ASM及Transform API开发的一个支持字节码插桩的Gradle 插件

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
