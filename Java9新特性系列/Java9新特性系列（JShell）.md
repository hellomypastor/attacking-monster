![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-b5fd4009d86914d1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# JShell是什么？
引入JDK官方的[Summary](http://openjdk.java.net/jeps/222)：
>Provide an interactive tool to evaluate declarations, statements, and expressions of the Java programming language, together with an API so that other applications can leverage this functionality.

+ JShell是Java的REPL工具(Read-eval-print-loop)：交互式解析器，一种命令行工具。它允许你无需使用类或者方法包装来执行Java语句。

+ 像Python和Scala之类的语言早就有交互式编程环境REPL了，以交互式的方式对语句和表达式进行求值。开发者只需要输入一些代码，就可以在编译前获得对程序的反馈。而之前的Java版本要想执行代码，必须创建文件、声明类、提供测试方法方可实现。

#JShell的理念
`即写即得，快速运行`

#JShell的目标
+ Java9中终于拥有了REPL工具：jShell。利用jShell在没有创建类
的情况下直接声明变量，计算表达式，执行语句。即开发时可以
在命令行里直接运行java的代码，而无需创建Java文件，无需跟
人解释”public static void main(String[] args)”这句废话。
+ jShell也可以从文件中加载语句或者将语句保存到文件中。
+ jShell也可以是tab键进行自动补全和自动添加分号。

#JShell使用举例
+ 进入JShell：
![进入shell](http://upload-images.jianshu.io/upload_images/2855474-34ff70475f523327.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 帮助：
![帮助](http://upload-images.jianshu.io/upload_images/2855474-63d7f089570840ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 基本使用：
![基本使用](http://upload-images.jianshu.io/upload_images/2855474-7b4c1788e5fbd843.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ Java表达式
JShell终端还可以自己计算Java表达式。字符串连接、方法回调、算法，诸如此类
![Java表达式](http://upload-images.jianshu.io/upload_images/2855474-a48e24fe84b5ddbf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 分号对于纯语句是可选的：
![分号对于纯语句可选](http://upload-images.jianshu.io/upload_images/2855474-b45d1f7205faff09.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 导入指定的包
![导入指定的包](http://upload-images.jianshu.io/upload_images/2855474-b9f533bf979e9ba6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 向前引用
你在定义方法时可以引用其他方法或变量，且这些方法或变量仅会在一段时间后被定义
+ Tab键自动补全代码
![Tab键自动补全代码](http://upload-images.jianshu.io/upload_images/2855474-5355e73e1b89c85a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ REPL网络
使用JShell时，我们不会受限于机器和网络访问，这带来了一些有趣的机会。例如，想想把它当做一个终端来与服务器交流，远程连接到服务器并且从外面控制一些参数。另一个选择是查询数据库，这里真的是有无限可能。
![REPL网络](http://upload-images.jianshu.io/upload_images/2855474-1466df68e43df40e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 保存和加载工作空间
    + 列出当前session中所有有效的代码片段
![代码片段](http://upload-images.jianshu.io/upload_images/2855474-a2045db5764bbcbb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

    + 列出当前session中所有创建过的变量
![创建过的变量](http://upload-images.jianshu.io/upload_images/2855474-803ceda76ef23bcd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

    + 列出当前session中所有创建过的方法
![创建过的方法](http://upload-images.jianshu.io/upload_images/2855474-60fd1e84b22722b3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 使用外部编辑器来编写java代码

```shell
/edit add
```
+ 从外部文件加载源代码

```shell
/open xxx.java
```
+ 没有受检异常/编译时异常
![没有受检异常](http://upload-images.jianshu.io/upload_images/2855474-764f973a8d337fb9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果你一直担心受检异常会毁掉你的REPL经历，无需再担心，JShell在后台为你隐藏好了
+ 退出shell

```shell
/exit
```
+ JShell API

>JShell除了可以作为单独的JDK工具，还可以提供一个API，它能对外部调用开放所有的功能

+ 在运行中修改定义

>你可以重新声明变量、方法或者类，而无需担心原始的声明。一旦你这样操作了，就会收到一个信息提示你发生了什么，但除此之外一切照旧。

**怎么样？是不是心动了？赶紧试试吧～**