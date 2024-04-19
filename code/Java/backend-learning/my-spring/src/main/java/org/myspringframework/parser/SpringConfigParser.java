package org.myspringframework.parser;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * 负责解析 Spring 配置文件
 */
public class SpringConfigParser {

    /**
     * 获取 要扫描的包 的路径
     *
     * @return 路径
     */
    public static String getComponentScanPackage(String springConfig) {
        // 使用DOM4J解析配置文件, SAXReader解析器
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = null;
        try {
            // 通过 输入流， 通过 SpringConfigParser 的 类加载器，获取 applicationContext.xml 的路径
            inputStream = SpringConfigParser.class.getClassLoader().getResourceAsStream(springConfig);
            // 读取该 xml文件
            Document document = saxReader.read(inputStream);
            // 获取到 xml文件 的根节点
            Element rootElement = document.getRootElement();
            // 找到 component-scan 标签
            Element element = rootElement.element("component-scan");
            // 获取 base package 所对应的 属性对象
            Attribute attribute = element.attribute("base-package");
            // 通过 属性对象， 拿到 String 类型的值
            return attribute.getValue();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
