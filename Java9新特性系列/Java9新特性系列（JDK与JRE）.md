![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-b0d3a5c0a5270988.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# JDK与JRE
JDK = JRE + 开发工具集(例如Javac编译工具等) 
JRE = JVM + JavaSE标准类库
#JDK8的目录结构
![JDK8目录结构](http://upload-images.jianshu.io/upload_images/2855474-75377787ceb911ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ bin：包含命令行开发和调试工具，如javac，jar和javadoc
+ include：包含在编译本地代码时使用的 C/C++头文件
+ lib：包含 JDK 工具的几个JAR和其他类型的文件。 它有一个tools.jar文件，其中包含javac编译器的Java类
+ jre/bin：包含基本命令，如java命令。 在Windows平台上， 它包含系统的运行时动态链接库(DLL)
+ jre/lib：包含用户可编辑的配置文件，如.properties和.policy文件。包含几个JAR。 rt.jar文件包含运行时的Java类和资源文件
#JDK9的目录结构
![JDK9目录结构](http://upload-images.jianshu.io/upload_images/2855474-b274b577edf0c72b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ bin：包含所有命令。 在Windows平台上，它继续包含系 统的运行时动态链接库
+ conf：包含用户可编辑的配置文件，例如以前位于jre\lib目录中的.properties 和.policy文件
+ include：包含要在以前编译本地代码时使用的C/C++头文件。 它只存在于 JDK 中
+ jmods：包含JMOD格式的平台模块。创建自定义运行时映像时需要它。它只存在于JDK中