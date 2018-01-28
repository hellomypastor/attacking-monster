![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-942b5df3ab9a15b2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>旧时王谢堂前燕，
飞入寻常百姓家。
小编将带你们一起分析阿里巴巴Java开发手册！
##背景
>阿里巴巴Java开发手册是阿里巴巴集团技术团队的集体智慧结晶和经验总结，以Java开发者为中心视角，划分为编程规约、异常日志、单元测试、安全规约、工程结构、MySQL数据库六个维度。手册的愿景是码出高效、码出质量、效率优先、质量为本。
##目的
>之所以要写这个系列的文章，首先是学习与总结，其次是思考与理解，更是分享与交流，手册中的每一条每一项都有其背后隐藏的原理与经验，我们看到的只是冰山一角，深入挖掘其背后的知识有益于更深刻的理解，并在自己实际编程中提高自己的基本技术素养。
##主题
+ 【推荐】表达异常的分支时，少用 if-else 方式，这种方式可以改写成:

```java
if (condition) { 
    ...
    return obj; 
}
// 接着写 else 的业务逻辑代码;
```
说明:如果非得使用 if()...else if()...else...方式表达逻辑，【强制】避免后续代码维
护困难，请勿超过 3 层。
正例:超过 3 层的 if-else 的逻辑判断代码可以使用卫语句、策略模式、状态模式等来实现， 其中卫语句示例如下:

```java
public void today() { 
    if (isBusy()) {
        System.out.println(“change time.”); return;
    }
    if (isFree()) {
        System.out.println(“go to travel.”);
        return; 
    }
    System.out.println(“stay at home to learn Alibaba Java Coding Guidelines.”);
    return; 
}
```

>我们试图通过例子来分析下上面的规则，在分析之前，我们先明确在Java里，默认的equals和hashCode方法的实现，以及把一个元素放入散列集合（HashSet、HashMap等）时，散列集合对equals和hashCode方法的判定规则。
Java对象中默认的equals(Object obj)方法用来判断两个对象是否“相同”，如果“相同”则返回true，否则返回false。hashCode()方法返回一个int数，这个整数值是将该对象的内部地址转换成一个整数返回的 。
在散列集合中存储一个对象时，先进行hashCode的比较，如果hashCode不想等，则直接放入，否则继续进行equals的比较，equals不相等才放入，equals不相等就直接丢弃了。
##理解
+ 如果只重写equals而不重写hashCode会导致什么问题？

```java
package com.test;
import java.util.HashSet;
import java.util.Set;
public class OverrideEqualsTest {    
    public static void main(String[] args) {
        Set<Point> set = new HashSet<Point>();
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 1);
        System.out.println("p1.equals(p2):" + p1.equals(p2));
        set.add(p1);
        set.add(p2);
        set.add(p1);
        System.out.println("set.size():" + set.size());        
        for (Point p : set) {
            System.out.println(p);
        }
    }    
    static class Point {        
        private int x;        
        private int y;        
        public Point(int x, int y) {            
            super();            
            this.x = x;            
            this.y = y;
        }        
        @Override
        public boolean equals(Object obj) {            
            if (this == obj) {              
                return true;  
            }          
            if (obj == null) {                
                return false; 
            }           
            if (getClass() != obj.getClass()) {                
                return false;
            }
            Point other = (Point) obj;            
            if (x != other.x) {                
                return false;  
            }          
            if (y != other.y) {               
                return false;  
            }          
            return true;
        }        
        @Override
        public String toString() {            
            return "Point [x=" + x + ", y=" + y + "]";
        }
    }
}
```

运行结果：
p1.equals(p2):true
set.size():2
Point[x=1,y=1]
Point[x=1,y=1]
>分析：由于没有重写hashCode方法，p1和p2对象默认的hashCode方法返回的两个对象地址转换的整数肯定不同，所以p1和p2都可以放入set中，所以这并不是我们期望的结果。

+ ​如果只重写hashCode而不重写equals又会导致什么问题？

```java
package com.test;
import java.util.HashSet;
import java.util.Set;
public class OverrideHashCodeTest {    
    public static void main(String[] args) {
        Set<Point> set = new HashSet<Point>();
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 1);
        System.out.println("p1.equals(p2):" + p1.equals(p2));
        set.add(p1);
        set.add(p2);
        set.add(p1);
        System.out.println("set.size():" + set.size());        
        for (Point p : set) {
            System.out.println(p);
        }
    }    
    static class Point {        
        private int x;        
        private int y;        
        public Point(int x, int y) {            
            super();            
            this.x = x;            
            this.y = y;
        }        
        @Override
        public int hashCode() {            
            final int prime = 3L;            
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;            
            return result;
        }        
        @Override
        public String toString() {            
            return "Point [x=" + x + ", y=" + y + "]";
        }
    }
}
```

运行结果：
p1.equals(p2):false
set.size():2
Point[x=1,y=1]
Point[x=1,y=1]
>分析：由于没有重写equals方法，p1和p2对象的默认的equals方法通过“==”来比较，而p1和p2是两个不同的对象，所以p1和p2都可以放入set中，所以这也不是我们期望的结果。
所以综上，当我们同时重写equals和hashCode方法后，才能在散列集合操作中得到一致性的结果。

+ 对象放入散列集合后，又修改了影响hashCode的值，后果？

```java
package com.test;
public class Test {    
    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        set.add(p1);
        set.add(p2);
        System.out.println("set.size():" + set.size());
        p2.setY(3);
        set.remove(p1);
        set.remove(p2);
        System.out.println("set.size():" + set.size());
    }
}
```
运行结果：
set.size():2
set.size():1
>分析：由于在执行期间，修改了p2对象的y值，导致p2对象的hashCode返回值有变化，所以hashset的remove方法将找不到新的hashCode所映射的对象，导致内存泄漏。