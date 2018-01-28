![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-2012eebb67452c49.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们介绍了Java8中的`Optional`，本期我们介绍Java8中新的时间日期API。

##JSR
在讲之前，首先讲一下JSR，什么是`JSR`呢？`Java Specification Requests`，是Java规范提案。
常见的JSR有：

+ `JSR-303` 参数验证
+ `JSR-310` 时间
+ `JSR-311` Restful API规范

`JSR-310`即时间规范标准：

在Java8之前，操作时间日期的类为：`Date`和`Calendar`，第三方包如**`Joda-time`**
在Java8中，新增了许多时间日期API

##Joda-time

####引入Maven依赖

```xml
<dependency>
    <groupId>joda-time</groupId>
    <artifactId>joda-time</artifactId>
    <version>2.9.2</version>
</dependency>
```

####核心类
+ Instant：不可变的类，用来表示时间轴上一个瞬时的点
+ DateTime：不可变的类，用来替换JDK的Calendar类
+ LocalDate：不可变的类，表示一个本地的日期，而不包含时间部分（没有时区信息）
+ LocalTime：不可变的类，表示一个本地的时间，而不包含日期部分（没有时区信息）
+ LocalDateTime：不可变的类，表示一个本地的日期－时间（没有时区信息）

####用法举例

```java
public static void main(String[] args) {
    DateTime today = DateTime.now();
    DateTime tomorrow = today.plusDays(1);
    DateTime oneHourAfter = today.plusHours(1);
    System.out.println(today.toString());
    System.out.println(tomorrow.toString());
    System.out.println(oneHourAfter.toString());

    DateTime day = new DateTime(2017,12,30,17,47,30);
    System.out.println(day.dayOfYear().getAsString());
    System.out.println(day.dayOfMonth().getAsString());
    System.out.println(day.dayOfWeek().getAsString());
}
```

结果：

```java
2017-12-30T17:49:24.065+08:00
2017-12-31T17:49:24.065+08:00
2017-12-30T18:49:24.065+08:00
364
30
6
```

>如果用过Joda-time的，那么Java8中新的时间日期API就很容易上手了。

##Java8中新的时间日期API

####LocalDate／LocalTime／LocalDateTime
+ now() ：根据当前时间创建对象
+ of()：根据指定日期/时间创建对象
+ plusDays／plusWeeks／plusMonths／plusYears 时间向后推移
+ minusDays／minusWeeks／minusMonths／minusYears 时间向前推移
+ plus／minus
+ withDayOfMonth/withDayOfYear/withMonth/withYear 修改
+ getDayOfMonth 获得月份天数(1-31)
+ getDayOfYear 获得年份天数(1-366)
+ getDayOfWeek 获得星期几(返回一个 DayOfWeek 枚举值)
+ getMonth 获得月份, 返回一个 Month 枚举值
+ getMonthValue 获得月份(1-12)
+ getYear 获得年份
+ until 获得两个日期之间的 Period 对象， 或者指定 ChronoUnits 的数字
+ isBefore／isAfter 比较两个 LocalDate
+ isLeapYear 判断是否是闰年

####Instant 时间戳
用于“时间戳”的运算。它是以Unix元年(传统 的设定为UTC时区1970年1月1日午夜时分)开始 所经历的描述进行运算。

####Duration 和 Period
+ Duration：用于计算两个“时间”间隔，Duration.between...
+ Period：用于计算两个“日期”间隔，Period.between...

####Clock 时钟
Clock类提供了访问当前日期和时间的方法，Clock是时区敏感的，可以用来取代System.currentTimeMillis() 来获取当前的微秒数。某一个特定的时间点也可以使用Instant类来表示，Instant类也可以用来创建老的java.util.Date对象。

```java
Clock clock = Clock.systemDefaultZone();  
long millis = clock.millis();  
Instant instant = clock.instant();  
Date date = Date.from(instant);
```

####日期的操作
+ TemporalAdjuster：时间校正器。有时我们可能需要获取例如：将日期调整到“下个周日”等操作。
+ TemporalAdjusters：该类通过静态方法提供了大量的常用TemporalAdjuster的实现。

####解析与格式化
java.time.format.DateTimeFormatter类：该类提供了三种格式化方法：

+ 1. 预定义的标准格式 
+ 2. 语言环境相关的格式 
+ 3. 自定义的格式

```java
DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;
DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss E");
```

####时区的处理
Java8中加入了对时区的支持，带时区的时间为分别为：

+ ZonedDate
+ ZonedTime
+ ZonedDateTime

其中每个时区都对应着ID，地区ID都为 “{区域}/{城市}”的格式 例如 :Asia/Shanghai等，ZoneId:该类中包含了所有的时区信息 

+ 1. getAvailableZoneIds() : 可以获取所有时区时区
+ 2. of(id) : 用指定的时区信息获取 ZoneId 对象