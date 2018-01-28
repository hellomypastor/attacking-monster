![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-9e4dc8769c17abe2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们分享了Java中if/else复杂逻辑的处理
>
>本期我们将分享Java中日志的处理（上）
>
>想必大家都用过日志，虽然日志看起来可有可无，但是等到出问题的时候，日志就派上了大用场，所以说日志打得好不好，规范不规范，直接影响了解决生产环境故障的效率，日志打的不好，有可能影响环境的性能，也有可能影响排查问题的难易程度，有可能排查问题的时间比写代码的时间还有多。
>
>那么我们就来分析下阿里Java开发手册--日志规约第一条：
>【强制】应用中不可直接使用日志系统(Log4j、Logback)中的 API，而应依赖使用日志框架 SLF4J 中的 API，使用门面模式的日志框架，有利于维护和各个类的日志处理方式统一。
```java 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
private static final Logger logger = LoggerFactory.getLogger(Abc.class);
```

##日志框架
Java中的日志框架分如下几种：

+ `Log4j` Apache Log4j是一个基于Java的日志记录工具。它是由Ceki Gülcü首创的，现在则是Apache软件基金会的一个项目。

+ `Log4j 2` Apache Log4j 2是apache开发的一款Log4j的升级产品。

+ `Commons Logging` Apache基金会所属的项目，是一套Java日志接口，之前叫Jakarta Commons Logging，后更名为Commons Logging。

+ `Slf4j` 类似于Commons Logging，是一套简易Java日志门面，本身并无日志的实现。（Simple Logging Facade for Java，缩写Slf4j）。

+ `Logback` 一套日志组件的实现(slf4j阵营)。

+ `Jul` (Java Util Logging),自Java1.4以来的官方日志实现。

##使用示例
+ **Jul**

```java
import java.util.logging.Logger;

private static final Logger logger = Logger.getLogger("name");
...
try {
...
} catch (Exception e) {
    logger.error(".....error");
}

if(logger.isDebugEnabled()) {
    logger.debug("....." + name);
}
```

+ **Log4j**

```java
import org.apache.log4j.Logger;

private static final Logger logger = Logger.getLogger(Abc.class.getNeme());
...
try {
...
} catch (Exception e) {
    logger.error(".....error");
}

if(logger.isDebugEnabled()) {
    logger.debug("....." + name);
}
```

+ **Commons Logging**

```java
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

private static final Log logger = LogFactory.getLogger(Abc.class);
...
try {
...
} catch (Exception e) {
    logger.error(".....error");
}

if(logger.isDebugEnabled()) {
    logger.debug("....." + name);
}
```

+ **Slf4j**

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger logger = LoggerFactory.getLogger(Abc.class);
...
try {
...
} catch (Exception e) {
    logger.error(".....error {}", e.getMessage(), e);
}

logger.debug(".....{}", name);
```
* **Jul**
    * 不支持占位符
    * 具体日志实现
* **Log4j**
    * 不支持占位符
    * 具体日志实现
* **Logback**
    * 不支持占位符
    * 具体日志实现
* **Commons Logging**
    * 不支持占位符
    * 日志门面
* **Slf4j**
    * 支持占位符
    * 日志门面

>Slf4j中有一个很重要的特性：`占位符`，{}可以拼接任意`字符串`，相比如其他框架的优点即不需要用`+`来拼接字符串，也就不会创建新的字符串对象，所以像log4j中需要加`isDebugEnabled()`的判断就是这个道理，在slf4j中就不需要加判断。

##门面模式
门面(Facade)模式，又称外观模式，对外隐藏了系统的复杂性，并向客户端提供了可以访问的接口，门面模式的好处是将客户端和子系统松耦合，方便子系统的扩展和维护。

正是门面模式这样的特点，使用Slf4j门面，不管日志组件使用的是log4j还是logback等等，对于调用者而言并不关心使用的是什么日志组件，而且对于日志组件的更换或者升级，调用的地方也不要做任何修改。

##源码分析

此处应有代（zhang）码（sheng）：

>首先使用静态工厂来获取Logger对象，传入的class，最终会转化为name，每个类的日志处理可能不同，所以根据传入类的名字来判断类的实现方式

```java
public static Logger getLogger(Class clazz) {
    return getLogger(clazz.getName());
}

public static Logger getLogger(String name) {
    ILoggerFactory iLoggerFactory = getILoggerFactory();
    return iLoggerFactory.getLogger(name);
}
```

>真正核心的在getILoggerFactory()中，首先判断初始化的状态`INITIALIZATION_STATE`，如果没有初始化`UNINITIALIZED`，那么会更改状态为正在初始化`ONGOING_INITIALIZATION`，并执行初始化`performInitialization()`，初始化完成之后，判断初始化的状态，如果初始化成功`SUCCESSFUL_INITIALIZATION`，那么会通过`StaticLoggerBinder`获取日志工厂`getLoggerFactory()`，这里又涉及到了`单例模式`。

```java
public static ILoggerFactory getILoggerFactory() {
    if (INITIALIZATION_STATE == UNINITIALIZED) {
        INITIALIZATION_STATE = ONGOING_INITIALIZATION;
        performInitialization();
    }
    switch (INITIALIZATION_STATE) {
        case SUCCESSFUL_INITIALIZATION:
            return StaticLoggerBinder.getSingleton().getLoggerFactory();
        case NOP_FALLBACK_INITIALIZATION:
            return NOP_FALLBACK_FACTORY;
        case FAILED_INITIALIZATION:
            throw new IllegalStateException("org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
        case ONGOING_INITIALIZATION:
            return TEMP_FACTORY;
    }
    throw new IllegalStateException("Unreachable code");
}
```

>接着我们分析`performInitialization`是如何初始化的，首先是执行`bind()`方法，然后判断如果状态为初始化成功`SUCCESSFUL_INITIALIZATION`，执行版本检查，主要是检查jdk版本与slf4j的版本，看是否匹配。

```java
private static final void performInitialization() {
    bind();
    if (INITIALIZATION_STATE == 3) {
        versionSanityCheck();
    }
}
```

>bind()方法，首先获取实现日志的加载路径，检查路径是否合法，然后初始化StaticLoggerBinder的对象，寻找合适的实现方式使用。

```java
private static final void bind() {
    try {
        Set staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
        reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);

        StaticLoggerBinder.getSingleton();
        INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
        reportActualBinding(staticLoggerBinderPathSet);
        emitSubstituteLoggerWarning();
    } catch (NoClassDefFoundError ncde) {
        String msg = ncde.getMessage();
        if (messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
            INITIALIZATION_STATE = NOP_FALLBACK_INITIALIZATION;
            Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
            Util.report("Defaulting to no-operation (NOP) logger implementation");
            Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
        } else {
            failedBinding(ncde);
            throw ncde;
        }
    } catch (NoSuchMethodError nsme) {
        String msg = nsme.getMessage();
        if ((msg != null) && (msg.indexOf("org.slf4j.impl.StaticLoggerBinder.getSingleton()") != -1)) {
            INITIALIZATION_STATE = FAILED_INITIALIZATION;
            Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
            Util.report("Your binding is version 1.5.5 or earlier.");
            Util.report("Upgrade your binding to version 1.6.x.");
        }
        throw nsme;
    } catch (Exception e) {
        failedBinding(e);
        throw new IllegalStateException("Unexpected initialization failure", e);
    }
}
```

>可以看出，bind()方法中最重要的方法就是寻找实现方式`findPossibleStaticLoggerBinderPathSet`，具体方法实现如下：

```java
private static Set findPossibleStaticLoggerBinderPathSet() {
    Set staticLoggerBinderPathSet = new LinkedHashSet();
    try {
        ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
        Enumeration paths;
        Enumeration paths;
        if (loggerFactoryClassLoader == null) {
            paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
        } else {
            paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
        }

        while (paths.hasMoreElements()) {
            URL path = (URL)paths.nextElement();
            staticLoggerBinderPathSet.add(path);
        }
    } catch (IOException ioe) {
        Util.report("Error getting resources from path", ioe);
    }
    return staticLoggerBinderPathSet;
}
```

>**注意！！前方高能！！**

>Slf4j的绝妙之处就在于此，类加载器加载类，也就是说寻找StaticLoggerBinder.class文件，然后只要实现了这个类的日志组件，都可以作为一种实现，如果有多个实现，那么谁先加载就使用谁，这个地方涉及`JVM的类加载机制`

##桥接
+ **Slf4j与其他日志组件的桥接（Bridge）**

* **slf4j-log4j12-1.7.13.jar**
    * log4j1.2版本的桥接器
* **slf4j-jdk14-1.7.13.jar**
    * java.util.logging的桥接器
* **slf4j-nop-1.7.13.jar**
    * NOP桥接器
* **slf4j-simple-1.7.13.jar**
    * 一个简单实现的桥接器
* **slf4j-jcl-1.7.13.jar**
    * Jakarta Commons Logging 的桥接器. 这个桥接器将SLF4j所有日志委派给JCL
* **logback-classic-1.0.13.jar(requires logback-core-1.0.13.jar)**
    * slf4j的原生实现，logback直接实现了slf4j的接口，因此使用slf4j与logback的结合使用也意味更小的内存与计算开销

[Slf4j Manual](https://www.slf4j.org/manual.html)中有一张图清晰的展示了接入方式，如下：

![桥接](http://upload-images.jianshu.io/upload_images/6851748-1f6febd8ef94bc0b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ **Bridging legacy APIs（桥接遗留的api）**

>* **log4j-over-slf4j-version.jar**
    * 将log4j重定向到slf4j
* **jcl-over-slf4j-version.jar**
    * 将commos logging里的Simple Logger重定向到slf4j
* **jul-to-slf4j-version.jar**
    * 将Java Util Logging重定向到slf4j

![桥接遗留api](http://upload-images.jianshu.io/upload_images/6851748-6c4d2a7f090aeafe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ **桥接注意事项**

>在使用slf4j桥接时要注意避免形成死循环，在项目依赖的jar包中不要存在以下情况

>* **log4j-over-slf4j.jar和slf4j-log4j12.jar同时存在**
  * 从名字上就能看出，前者重定向给后者，后者又委派给前者，会形成死循环
* **jul-to-slf4j.jar和slf4j-jdk14.jar同时存在**
  * 从名字上就能看出，前者重定向给后者，后者又委派给前者，会形成死循环

##总结
* 为了更好的了解Slf4j，你需要了解：
    * **JVM类加载机制**
    * **设计模式：门面模式、桥接模式**

* 简单总结Slf4j的原理：
    * 通过工厂类，提供一个的接口，用户可以通过这个门面，直接使用API实现日志的记录。
    * 而具体实现由Slf4j来寻找加载，寻找的过程，就是通过类加载加载**org/slf4j/impl/StaticLoggerBinder.class**的文件，只要实现了这个文件的日志实现系统，都可以作为一种实现方式。
    * 如果找到很多种方式，那么就寻找一种默认的方式。
    * 这就是日志接口的工作方式，**简单高效**，关键是**完全解耦**，不需要日志实现部分提供任何的修改配置，只需要符合接口的标准就可以加载进来，**有利于维护和各个类的日志处理方式统一**。