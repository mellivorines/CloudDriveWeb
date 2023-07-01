package io.github.mellivorines.clouddriveweb.utils

import org.reflections.Reflections
import java.lang.reflect.InvocationTargetException
import java.util.*


/**
 * @Description:反射工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */

object ReflectUtil {
    /**
     * 创建实体
     *
     * @param classPath
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    fun createInstance(classPath: String): Any {
        val o: Any = try {
            val clazz = Class.forName(classPath)
            clazz.newInstance()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw RuntimeException("the class $classPath can not be found")
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw RuntimeException("load class $classPath fail")
        } catch (e: InstantiationException) {
            e.printStackTrace()
            throw RuntimeException("load class $classPath fail")
        }
        return o
    }

    /**
     * 创建类实例
     *
     * @param clazz
     * @return
     */
    fun createInstance(clazz: Class<*>): Any {
        val o: Any = try {
            clazz.newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
            throw RuntimeException("load class " + clazz.canonicalName + " fail")
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw RuntimeException("load class " + clazz.canonicalName + " fail")
        }
        return o
    }

    /**
     * 获取Class类
     *
     * @param classPath
     * @return
     */
    fun getClass(classPath: String): Class<*> {
        return try {
            Class.forName(classPath)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw RuntimeException("the class " + classPath + "can not be found")
        }
    }

    /**
     * 注入属性值
     *
     * @param o
     * @param fieldName
     * @param fieldValue
     */
    fun injectFieldValue(o: Any, fieldName: String, fieldValue: Any?) {
        try {
            val field = o.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field[o] = fieldValue
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            throw RuntimeException("there are no field named " + fieldName + " in class " + o.javaClass.name)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw RuntimeException("inject field value fail : $fieldName")
        }
    }

    /**
     * set注入属性值
     *
     * @param o
     * @param fieldName
     * @param fieldValue
     */
    fun setFieldValue(o: Any, fieldName: String, fieldValue: Any) {
        val declaredMethods = o.javaClass.declaredMethods
        for (i in declaredMethods.indices) {
            if (declaredMethods[i].name.equals("set$fieldName", ignoreCase = true)) {
                try {
                    declaredMethods[i].invoke(o, *arrayOf(fieldValue))
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                    throw RuntimeException("set field value fail : $fieldName")
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                    throw RuntimeException("set field value fail : $fieldName")
                }
            }
        }
    }

    /**
     * 获取接口所有实现类
     *
     * @param interfaceClass
     * @param baseScanPath
     * @return
     */
    fun getAllSubTypeOf(interfaceClass: Class<*>, baseScanPath: String): Set<Class<*>> {
        if (!interfaceClass.isInterface) {
            throw RuntimeException("the class must be interface")
        }
        val reflections = Reflections(baseScanPath)
        return reflections.getSubTypesOf(interfaceClass)
    }

    /**
     * 校验改类上面有没有该注解或者子注解
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    fun hasAnnotationOrSubAnnotation(clazz: Class<*>, annotationClass: Class<out Annotation>): Boolean {
        if (clazz.isAnnotationPresent(annotationClass)) {
            return true
        }
        val annotations = clazz.annotations
        for (i in annotations.indices) {
            if (annotations[i].javaClass.isAnnotationPresent(annotationClass)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取clazz的接口集合
     *
     * @param clazz
     * @return
     */
    fun getInterfaces(clazz: Class<*>): List<Class<*>?> {
        var interfaces: List<Class<*>?> = ArrayList()
        val interfaceArr = clazz.interfaces
        if (interfaceArr.isNotEmpty()) {
            interfaces = listOf(*interfaceArr)
        }
        return interfaces
    }

    /**
     * 获取子注解
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    fun getSubAnnotation(clazz: Class<*>, annotationClass: Class<out Annotation>): Annotation {
        val annotations = clazz.annotations
        for (i in annotations.indices) {
            if (annotations[i].javaClass.isAnnotationPresent(annotationClass)) {
                return annotations[i]
            }
        }
        throw RuntimeException("there are no subType matched the annotation " + annotationClass.canonicalName)
    }
}

