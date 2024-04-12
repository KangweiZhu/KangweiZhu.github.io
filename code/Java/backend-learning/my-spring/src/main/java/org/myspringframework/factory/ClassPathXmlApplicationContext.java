package org.myspringframework.factory;

import org.myspringframework.annotation.Controller;
import org.myspringframework.annotation.Service;
import org.myspringframework.parser.SpringConfigParser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext {

    private String springConfig;
    private List<String> classPath = new ArrayList<>();

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
        System.out.println(classPath);
        //3. 实例化对象
        doInitInstance();
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
                if (c.isAnnotationPresent(Service.class) ||  c.isAnnotationPresent(Controller.class)) {
                    Object o = c.newInstance();
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
        System.out.println("Final ClassPath: " + path);
        return path;
    }
}
