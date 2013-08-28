/**
 * ClassName: AnnotationScanTool
 * CopyRight: OpenSoft
 * Date: 12-12-24
 * Version: 1.0
 */
package com.opensoft.common.utils;

import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * Description : 注解工具类，功能：扫描指定包的注解
 * User : 康维
 */
public class AnnotationScanTool {
    private static final Logger log = LoggerFactory.getLogger(AnnotationScanTool.class);

//    public static void main(String[] args) throws MalformedURLException {
//        System.out.println(scanMethodByAnnotation(Arrays.asList("com.OpenSoft.common"), OnEvent.class));
//        System.out.println(scanByAnnotation(Arrays.asList("com.OpenSoft"), Component.class));
//    }

    /**
     * 扫描指定包下所有包含指定注解的类
     *
     * @param packageNames 上下文环境context
     * @param annotation   注解类
     * @return 包含注解的类的集合
     */
    public static Set<Class<?>> scanByAnnotation(List<String> packageNames, Class<? extends Annotation> annotation) {
        Reflections reflections = getReflections(packageNames);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * 获取Reflections
     *
     * @param packageNames 包名
     * @return Reflections
     */
    private static Reflections getReflections(List<String> packageNames) {
        ConfigurationBuilder config = new ConfigurationBuilder();
        for (String packageName : packageNames) {
            config.addUrls(ClasspathHelper.forPackage(packageName));
        }
        //添加扫描器
        config.setScanners(new MethodAnnotationsScanner(),
                new ResourcesScanner(),
                new TypeAnnotationsScanner(),
                new FieldAnnotationsScanner(),
                new MethodParameterScanner(),
                new SubTypesScanner(),
                new TypeElementsScanner());
        return new Reflections(config);
    }

    /**
     * 扫描指定包下指定注解的方法
     *
     * @param packageNames 包名
     * @param annotation   注解
     * @return 方法列表
     */
    public static Set<Method> scanMethodByAnnotation(List<String> packageNames, Class<? extends Annotation> annotation) {
        Reflections reflections = getReflections(packageNames);
        return reflections.getMethodsAnnotatedWith(annotation);
    }
}
