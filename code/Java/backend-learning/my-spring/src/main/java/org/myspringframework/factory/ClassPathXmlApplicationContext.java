package org.myspringframework.factory;

import org.myspringframework.annotation.Autowire;
import org.myspringframework.annotation.Controller;
import org.myspringframework.annotation.Repository;
import org.myspringframework.annotation.Service;
import org.myspringframework.parser.SpringConfigParser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathXmlApplicationContext {

    private String springConfig;
    private List<String> classPath = new ArrayList<>();
    //key: 接口， value：实现类的对象
    private Map<Class<?>, List<Object>> iocInterfaces = new ConcurrentHashMap<>();
    //这才是ioc核心容器。上面的那个只是接口 key: 被注解的类, value：被注解的类的对象 和上面的value是一个玩意。
    private Map<Class<?>, Object> iocContainer = new ConcurrentHashMap<>();
    //这个也是ioc核心容器， key： 被注解类的类值（有value的话用value, 没value用小写。 理解： Student类 Student student = new Student()的这个具体对象, 我们把小写的student放进去， value： 被注解的类的对象
    private Map<String, Object> iocNameContainer = new ConcurrentHashMap<>();

    /**
     * 构造方法
     *
     * @param springConfig application.xml 路径
     */
    public ClassPathXmlApplicationContext(String springConfig) {
        this.springConfig = springConfig;
        init();
    }

    /**
     * 初始化 Spring IoC容器
     */
    private void init() {
        //1. 调用 XML 解析方法， 解析 applicationContext.xml， 获取到要扫描的包。
        String componentScanPackage = SpringConfigParser.getComponentScanPackage(springConfig);
        System.out.println("The package path that is parsed: " + componentScanPackage);
        //2. 扫描这个包下面 所有的类。
        loadClasses(componentScanPackage);
        System.out.println("Classpath list: " + classPath);
        //3. 实例化对象
        doInitInstance();
        System.out.println("Interface and Class which implement the interface: " + iocInterfaces);
        System.out.println("Class and Object of this class: " + iocContainer);
        System.out.println("Name container: " + iocNameContainer);
        //4. Autowire => DI
        doDI();
    }

    /**
     * 依赖注入
     */
    private void doDI() {
        // 1. 获取 iocContainer中的所有类
        Set<Class<?>> classes = iocContainer.keySet();
        for (Class<?> clz : classes) {
            // 2. 获取类里面 声明的变量
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                // 如果被 Autowire 注解的话
                if (field.isAnnotationPresent(Autowire.class)) {
                    // 0. 先声明 bean对象， 后面要通过三种方式去找
                    Object bean;
                    // 1. 获取他的注解名， @Autowire(value = "????"), 默认情况下Autowire的value是没有的
                    Autowire autowireAnnotation = field.getAnnotation(Autowire.class);
                    String value = autowireAnnotation.value();
                    // 相同，则说明是默认值。不同，则说明不是默认值，存在多个实现类（默认），直接通过value去找。
                    // 我们规定程序员， 如果注解不带 value, 则代表这个注解的类只会出现一次，即使这个类实现的接口，有多个实现类。
                    // 比如 UserServiceImpl1 UserServiceImpl2 UserServiceImpl3.。。。。都实现类UserService接口， 但是，如果你在UserServiceImpl1中，没有使用value, 则其他几个类你都用不了
                    if (!"".equals(value)) {
                        bean = getBeanByValue(value);
                        if (bean == null) {
                            throw new RuntimeException("No bean is using the annotation with the value " + value); //没找着，比如autowire里面的值，用错了。
                        }
                    } else {
                        //否则，说明注解只会出现一次, 这时候直接通过类名去找 UserServiceImpl userService的情况。
                        Class<?> type = field.getType();
                        bean = getBeanByType(type);
                        //还没找到，就通过接口去找。 注意， 这时候通过接口去找， 就算他有多个实现类，只能找一个。到了第三部
                        if (bean == null) {
                            bean = getBeanByInterface(type);
                            if (bean == null) {
                                throw new RuntimeException("No qualify bean available");
                            }
                        }
                    }
                    // 到了这里，就说明找到了， 通过反射赋值
                    field.setAccessible(true);
                    /**
                     * 给具体的对象中的字段赋值，
                     * 比如UserController有多个new出来的类， 找到具体的这个userController类， 对他里面的UserService userSerivce, 赋值。
                     */
                    Object o = iocContainer.get(clz);
                    try {
                        // 给具体的一个对象中的这个字段赋值
                        field.set(o, bean);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("aaaa");
    }

    /**
     * 通过 @Autowire(value = "xx") 中的value来拿。
     * 因为一个接口可能存在多个实现类，此时， 我们必须要用value给他起跟接口实现类相同的别名，不然其他两个方式，都会产生混淆，拿不到对的。
     * 因此这个get方式，有机最高
     *
     * @param name 比如UserServiceImpl
     * @return
     * @Autowired UserServiceImpl userService;
     * 这种情况
     */
    public Object getBeanByValue(String name) {
        return iocNameContainer.get(name);
    }

    /**
     * 通过类名拿
     * 处理 @Autowired
     *      UserServiceImpl userService, 这种情况。 可能不会写接口名字，而是直接写类名
     *  在第二个
     */
    public Object getBeanByType(Class<?> clz) {
        return iocContainer.get(clz);
    }

    /**
     * 通过接口名拿。
     * 优先级是最低的，因为前面两个可以解决绝大多数 异常情况（多个实现类， 不按接口声明）
     * 走到这里了，说明这个接口应该只有一个实现类，如果有多个，那就说名这个程序员，没有按照我们的要求，使用value跟接口实现类的value匹配上。
     * 所以我们需要特判以下，如果list里面的元素超过一个，就直接报错， 否则，返回第一个
     *
     * @param clz
     * @return
     */
    public Object getBeanByInterface(Class<?> clz) {
        List<Object> objectList = iocInterfaces.get(clz);
        if (objectList.size() > 1) {
            throw new RuntimeException("mutilple bean sharing same annotation");
        }
        return objectList.get(0);
    }

    /**
     * 执行类的实例化
     */
    private void doInitInstance() {
        //1. 通过 类路径，获取class对象
        for (String classPath : classPath) {
            try {
                Class<?> c = Class.forName(classPath);
                //2. 判断该类 是否被Service注解或者Controller注解 注释. 如果是， 则通过无参构造创建该类的对象
                if (c.isAnnotationPresent(Service.class) || c.isAnnotationPresent(Controller.class) || c.isAnnotationPresent(Repository.class)) {
                    Object o = c.newInstance();
                    //获取 该类实现的接口
                    Class<?>[] interfaces = c.getInterfaces();
                    for (Class<?> clz : interfaces) {
                        // 获取 该接口所有的实现类
                        List<Object> objects = iocInterfaces.get(clz);
                        // 如果没有实现类
                        if (objects == null) {
                            List<Object> objects1 = new ArrayList<>();
                            objects1.add(o);
                            iocInterfaces.put(clz, objects1);
                        } else {
                            objects.add(o);
                        }
                    }
                    iocContainer.put(c, o);
                    // 接下来都是Autowire的操作了。
                    // 此时 c要么是被 Controller注解， 要么是被Service注解
                    Controller controllerAnnotation = c.getAnnotation(Controller.class);
                    Service serviceAnnotation = c.getAnnotation(Service.class);
                    Repository repositoryAnnotation = c.getAnnotation(Repository.class);
                    // 注解默认是没有 value的
                    String annotationValue = "";
                    // 判断其有没有值
                    if (controllerAnnotation != null) {
                        annotationValue = controllerAnnotation.value();
                    } else if (serviceAnnotation != null) {
                        annotationValue = serviceAnnotation.value();
                    } else if (repositoryAnnotation != null) {
                        annotationValue = repositoryAnnotation.value();
                    }
                    String objectValue = "";
                    if ("".equals(annotationValue)) {
                        String classname = c.getSimpleName();
                        //System.out.println(classname);// UserController UserServiceImpl
                        //将其转换成首字母小写的类名。
                        objectValue = String.valueOf(classname.charAt(0)).toLowerCase() + classname.substring(1);
                        //System.out.println(objectValue); // userController userServiceImpl
                    } else {
                        objectValue = annotationValue;
                    }
                    // 如果该对象名已经存在于容器中， 那不管他是之前/现在是那个类，哪个注解的，我们现在都不能给他放进去。
                    if (iocNameContainer.containsKey(objectValue)) {
                        throw new RuntimeException("iocNameContainer: name alreay exist"); //可以玩一下 @Controller(value = "aaa") @Service(value = "aaa")
                    }
                    // 不存在， 就放进去
                    iocNameContainer.put(objectValue, o);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 加载扫描包下的所有的类 com.anicaaz
     * 注意 这里是 类， 而不是.java 结尾的 文件。
     * 也就是说， 我们扫描的其实是 target 下面的那个 classes。
     */
    private void loadClasses(String componentScanPackage) {
        // System.out.println(resource); 当前是在test-classes下， 我们想要的是classes下的类 //file:/home/anicaa/%e6%96%87%e6%a1%a3/backup-blog/blog/source/code/Java/backend-learning/my-spring/target/test-classes/
        // 1. 将 com.anicaaz 的 . 替换成 /。 即 com/anicaaz
        componentScanPackage = componentScanPackage.replace(".", "/");
        System.out.println("New componentScanPackageName: " + componentScanPackage);

        // 构建 target/classes/目录的绝对路径， 并将 步骤1的结果，拼接上去
        // 2. 去掉 当前路径的 file:/,  变成 classPath 类路径， 或绝对路径。 注意linux 是去掉 file:, 需要保留 /
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        String urlString = resource.toString();
        // 2.1 我的机器是包含中文路径的，所以要转成utf-8
        try {
            urlString = URLDecoder.decode(urlString, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String absolutePath = urlString.replace("file:", "");
        System.out.println("Absolute path: " + absolutePath); // /home/anicaa/%e6%96%87%e6%a1%a3/backup-blog/blog/source/code/Java/backend-learning/my-spring/target/test-classes/
        // 2.2. 将test-classes 替换成 classes
        if (absolutePath.contains("test-classes")) {
            absolutePath = absolutePath.replace("test-classes", "classes");
        }
        // 4. 最后加上 需要被扫描的路径
        absolutePath += componentScanPackage;
        System.out.println("Final absolutePath: " + absolutePath);
        // 4. 递归 扫描 该路径下所有的类
        findAllClasses(new File(absolutePath));
    }

    /**
     * 递归扫描 指定路径下的 所有class
     *
     * @param file classPath那个目录对应的包 (文件夹)
     */
    private void findAllClasses(File file) {
        // 1.将该目录下的所有文件转换成files
        File[] files = file.listFiles();
        // 2.递归寻找
        for (File f : files) {
            //如果是目录，则继续往下面找
            if (f.isDirectory()) {
                findAllClasses(f);
            } else {
                String name = f.getName();
                //如果 文件名以 .class 结尾， 则算作结果
                if (name.endsWith(".class")) {
                    String path = f.getPath();
                    classPath.add(handleClassPath(path));
                }
            }
        }
    }

    /**
     * 将字符串转换成 com.anicaaz.springplayground.controller格式/
     * 我这里是 Linux写法， 所以用的是 /， 如果是 Windows, 则需要 变成 \\.  第一个\负责转义
     * 这个方法结束后，就是真的类路径了，可以用反射。
     *
     * @param path
     * @return
     */
    private String handleClassPath(String path) {
        // 1. 获取 class/ c开头的下标
        // classes/ 长度为8
        // .class 长度为6
        int index = path.indexOf("classes/");
        // 2. 从 com 开头 截取
        path = path.substring(index + 8, path.length() - 6);
        // 3. 最后 把 / 替换成 .
        path = path.replace('/', '.');
        System.out.println("Class inside target/classes: " + path);
        return path;
    }
}
