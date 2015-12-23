/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 合并两个实体，主要用于更新前，合并前台提交的实体和数据库实体
 * @Author: lin.shi
 * @CreateTime: 2015-12-05 20:29
 */
public class MergeUtil {

    /**
     * 合并两个实体，将source字段设置到target对象
     * <p/>
     * 只有当source里面字段值不为null时，才会将该字段的值更新到target
     * <p/>
     * 两个实体必须属于同一类型
     *
     * @param source 源实体    (一般指前台传递的对象)
     * @param target 目标实体   (数据库查询对象)
     */
    public static Object merge(Object source, Object target) {
        return copyProperties(source, target, null, null);
    }

    /**
     * @param source           源实体    (一般指前台传递的对象)
     * @param target           目标实体   (数据库查询对象)
     * @param ignoreProperties 忽略字段集合
     * @return
     */
    public static Object merge(Object source, Object target, String[] ignoreProperties) {
        return copyProperties(source, target, null, ignoreProperties);
    }

    private static Object copyProperties(Object source, Object target, Class<?> editable, String[] ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null &&
                    (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        if (value == null) {
                            continue;
                        }
                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
        return target;
    }

}