package io.github.mellivorines.cloud.drive.web.common.annotation


/**
 * @Description:接口需要分享码url标识注解
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/2
 */

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class NeedShareCode

