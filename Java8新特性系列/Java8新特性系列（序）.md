 ![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-860d5b98a0d096dc.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Jdk目前已经发展到`Java 9`了，历史上有两个版本变化比较大，一个是`Java 5`，另一个就是`Java 8`。
本Java8新特性系列将着重分析理解Java8的新特性，以及其是怎么为我们开发提升效率的。

#历史版本如下：

| 版本 | 名称 | 发行日期 |
| ------------- |:-------------:| -----:|
| JDK 1.1.4 | Sparkler（宝石） | 1997-09-12 |
| JDK 1.1.5 | Pumpkin（南瓜） | 1997-12-13 |
| JDK 1.1.6 | Abigail（阿比盖尔–女子名） | 1998-04-24 |
| JDK 1.1.7 | Brutus（布鲁图–古罗马政治家和将军） | 1998-09-28 |
| JDK 1.1.8 | Chelsea（切尔西–城市名） | 1998-04-24 |
| J2SE 1.2 | Playground（运动场 | 1998-12-04 |
| J2SE 1.2.1 | none（无） | 1999-03-30 |
| J2SE 1.2.2 | Cricket（蟋蟀） | 1999-07-08 |
| J2SE 1.3 | Kestrel（美洲红隼） | 2000-05-08 |
| J2SE 1.3.1 | Ladybird（瓢虫） | 2001-05-17 |
| J2SE 1.4.0 | Merlin（灰背隼） | 2002-02-13 |
| J2SE 1.4.1 | grasshopper（蚱蜢） | 2002-09-16 |
| J2SE 1.4.2 | Mantis（螳螂） | 2003-06-26 |
| Java SE 5.0 (1.5.0) | Tiger（老虎） | 2004-09-30 |
| Java SE 6.0 (1.6.0) | Mustang（野马） | 2006-04-xx |
| Java SE 7.0 (1.7.0) | Dolphin（海豚） | 2011-07-28 |
| Java SE 8.0 (1.8.0) | Spider（蜘蛛） | 2014-03-18 |
| Java SE 9 |  | 2017-09-21 |

#最近三个版本的新特性如下：
##Java 7
>开发代号是Dolphin（海豚），于2011-07-28发行

引入的新特性包括：

+ switch语句块中允许以字符串作为分支条件；
+ 在创建泛型对象时应用类型推断；
+ 在一个语句块中捕获多种异常；
+ 支持动态语言；
+ 支持try-with-resources；
+ 引入Java NIO.2开发包；
+ 数值类型可以用2进制字符串表示，并且可以在字符串表示中添加下划线；
+ 钻石型语法；
+ null值的自动处理。

##Java 8
>开发代号是Spider（蜘蛛），于2014年3月14号发布

引入的新特性包括：

+ Lambda表达式
+ Pipelines和Streams
+ Date和Time API
+ Default方法
+ Type注解
+ Nashhorn JavaScript引擎
+ 并发计数器
+ Parallel操作
+ 移除PermGen Error
+ TLS SNI

##Java 9
>于2017年9月21日发布

引入的新特性包括：

+ 模块化系统–Jigsaw 项目
+ JShell–Java 9 REPL
+ 集合工厂方法
+ 接口中的私有方法
+ 响应式流
+ 多分辨率图像API–JEP 251
+ 进程API的改进
+ Try-With-Resources
+ 钻石（diamond）操作符范围的延伸
+ 增强的注释Deprecate
+ 统一的JVM日志
+ 注释SafeVarargs范围的延伸
+ HTTP 2 客户端
+ HTML5风格的Java帮助文档
+ 保留下划线字符。变量不能被命名为_；
+ 废弃Applet API；
+ javac不再支持Java1.4以及之前的版本；
+ 废弃Java浏览器插件；
+ 栈遍历API–栈遍历API能过滤和迟访问在堆栈跟踪中的信息


>###目前`Java8`在Java各版本使用率中已高达`60%`以上，未来还会继续上升，所以学会用Java8，用好Java8势在必行，本系列将着重分析理解Java8的新特性，以及其是怎么为我们开发提升效率的。
>
>##**人生苦短，拥抱Java8。**
 