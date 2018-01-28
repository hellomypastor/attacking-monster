![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-02dd2668d031a36c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们分享了Java中日志的处理（上）：Java中日志的相关知识、Slf4j的原理及源码分析
>
>本期我们将分享Java中日志的处理（下）


##首先看下阿里Java开发手册中日志规约的剩余几条并给出分析：

+ 2.【强制】日志文件推荐至少保存 15 天，因为有些异常具备以“周”为频次发生的特点。

>分析：
>+ 如果使用的是`Log4j`，且采用的`RollingFileAppender`方式, 通过设置`maxBackupIndex`属性来指定要保留的日志文件数的最大值可以间接实现删除N天前的日志文件
>+ 如果使用的是`Log4j`,且采用的`DailyRollingFileAppender`方式，由于该方式不支持`maxBackupIndex`，需要重新实现`DailyRollingFileAppender`，用以支持`maxBackupIndex`的设置
>+ 如果使用的是`Logback`,可以通过设置`maxHistory`实现删除N天前的日志

+ 3.【强制】应用中的扩展日志(如打点、临时监控、访问日志等)命名方式: appName_logType_logName.log。logType:日志类型，推荐分类有stats/desc/monitor/visit 等;logName:日志 述。这种命名的好处:通过文件名就可知道日志文件属于什么应用，什么类型，什么目的，也有利于归类查找。
正例:mppserver应用中单独监控时区转换异常，如:
mppserver_monitor_timeZoneConvert.log
说明:推荐对日志进行分类，如将错误日志和业务日志分开存放，便于开发人员查看，也便于 通过日志对系统进行及时监控。

+ 4.【强制】对trace/debug/info级别的日志输出，必须使用条件输出形式或者使用占位符的方 式。
说明:logger.debug("Processing trade with id: " + id + " and symbol: " + symbol); 如果日志级别是warn，上述日志不会打印，但是会执行字符串拼接操作，如果 symbol是对象， 会执行toString()方法，浪费了系统资源，执行了上述操作，最终日志却没有打印。 

>正例:(条件)

```java
if (logger.isDebugEnabled()) {
    logger.debug("Processing trade with id: " + id + " and symbol: " + symbol);
}
```

>正例:(占位符)

```java
logger.debug("Processing trade with id: {} and symbol : {} ", id, symbol);
```
>分析：
>+ 正如上篇分析的，推荐所有使用`Slf4j`，打印日志统一使用`占位符`，且不需判读isxxxEnabled()

+ 5.【强制】避免重复打印日志，浪费磁盘空间，务必在 log4j.xml 中设置 additivity=false。 
正例:

```xml
<logger name="com.taobao.dubbo.config" additivity="false">
```
+ 6.【强制】异常信息应该包括两类信息:案发现场信息和异常堆栈信息。如果不处理，那么通过 关键字 throws 往上抛出。
正例:

```java
logger.error(各类参数或者对象 toString + "_" + e.getMessage(), e);
```

+ 7.【推荐】谨慎地记录日志。生产环境禁止输出 debug 日志;有选择地输出 info日志;如果使 用 warn 来记录刚上线时的业务行为信息，一定要注意日志输出量的问题，避免把服务器磁盘 撑爆，并记得及时删除这些观察日志。 说明:大量地输出无效日志，不利于系统性能升，也不利于快速定位错误点。记录日志时请 思考:这些日志真的有人看吗?看到这条日志你能做什么?能不能给问题排查带来好处?
+ 8.【参考】可以使用warn 日志级别来记录用户输入参数错误的情况，避免用户投诉时，无所适 从。注意日志输出的级别，error级别只记录系统逻辑出错、异常等重要的错误信息。如非必 要，请不要在此场景打出 error级别。

##补充
+ 1、 涉及到多线程时，日志中最好将线程id打印出来，以区分不同的线程

```java
public final class LogIdThreadLocal {
    private static ThreadLocal<String> logIdThreadLocal = new ThreadLocal<String>();
    ...
}

public class MyPatternLayout extends PatternLayout {
    private static final String SPLIT_STRING = "|";

    @Override
    public String format(LoggingEvent event) {
        String log = super.format(event);

        String threadLocalId = LogIdThreadLocal.getLogId();

        if (StringUtils.isEmpty(threadLocalId)) {
            threadLocalId = LogIdThreadLocal.create();
        }

        return log + threadLocalId + SPLIT_STRING + event.getMessage() + Layout.LINE_SEP;
    }
}
```
```properties
log4j.appender.output.layout=com.test.log.MyPatternLayout
log4j.appender.output.layout.ConversionPattern=%-d{yyyy-MM-dd HH:mm:ss:SSS}|%t|%-5p|%C{1}.%M:%L| 
```

+ 2、 提供动态修改日志级别的接口或者使用Log4j Web Tracker
>对于生产环境，默认的日志级别可能是error/warning/info，对于debug的日志就没有打出来，如果要让debug日志能打印出来，那么常见的方法就是修改log4j.xml或者log4j.properties文件，修改了之后需要重启tomcat，我们知道，生产环境是不可能随随便便重启的，那么有没有其他方法呢？答案是：有。
Log4j为我们提供了这样的API,通过调用Log4j的API，提供rest接口，使得客户端可以动态修改某个日志的级别：

```java
private String changeLoggerLevel(String loggerName, String level) {  
    Logger logger = LogManager.exists(loggerName);  
    String result = null;  
    if (logger != null) {  
        logger.setLevel(Level.toLevel(level));  
        result = logger.getName() + "|" + logger.getLevel();  
    } else {  
        result = "logger not exist.";  
    }  
    return result;  
}  
```

>此外，推荐一个开源的第三方组件：[Log4j Web Track](https://github.com/mrsarm/log4jwebtracker)（链接为Github地址），如下图：

![Log4j Web Tracker](http://upload-images.jianshu.io/upload_images/2855474-1e9a56be7d438d0b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
>相关配置：

```xml
<servlet>
    <servlet-name>TrackerServlet</servlet-name>
    <servlet-class>log4jwebtracker.servlet.TrackerServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>TrackerServlet</servlet-name>
    <url-pattern>/tracker/*</url-pattern>
</servlet-mapping>

<servlet>
    <servlet-name>Log4jInitServlet</servlet-name>
    <servlet-class>log4jwebtracker.servlet.init.Log4jInitServlet</servlet-class>
    <init-param>
        <param-name>log4jConfigLocation</param-name>
        <param-value>WEB-INF/classes/log4j.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
```
>通过阅读源码，其也是调用了Log4j的API，进行了展示：

```java
public abstract class LoggingUtils {

	static synchronized public List getFileAppenders() {
		List list = new ArrayList();
		Enumeration e = LogManager.getRootLogger().getAllAppenders();
		while(e.hasMoreElements()) {
			Appender a = (Appender) e.nextElement();
			if(a instanceof FileAppender) {
				list.add(a);
			}
		}
		return list;
	}

	static synchronized public FileAppender getFileAppender(String appenderName) {
		Enumeration e = LogManager.getRootLogger().getAllAppenders();
		while(e.hasMoreElements()) {
			Appender a = (Appender) e.nextElement();
			if(a instanceof FileAppender && a.getName().equals(appenderName)) {
				return (FileAppender) a;
			}
		}
		return null;
	}

	static public boolean contains(List loggers, String loggerName) {
		int i=0;
		while(i<loggers.size()) {
			if(((Logger)loggers.get(i)).getName().equals(loggerName)) {
				return true;
			}
			i++;
		}
		return false;
	}

	static public List getLoggers() {
		Enumeration e = LogManager.getCurrentLoggers();
		List loggersList = new LinkedList();
		while(e.hasMoreElements()) {
			loggersList.add(e.nextElement());
		}
		Collections.sort(loggersList, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Logger log0 = (Logger) arg0;
				Logger log1 = (Logger) arg1;
				return log0.getName().compareTo(log1.getName());
			}
		});
		loggersList.add(0, LogManager.getRootLogger());
		return loggersList;
	}
}
```

>**如果有兴趣的话，我们可以实现一个Web Tracker的门面，类似于Slf4j，那么对于Log4j1/2、LogBack、Juc、Commons Logging的日志都能实现可视化以及动态修改日志级别的功能**

