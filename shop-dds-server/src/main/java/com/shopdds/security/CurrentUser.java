package com.shopdds.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前登录用户注入注解
 * <p>
 * 在 Controller 方法参数上标注，由 {@link CurrentUserArgumentResolver} 注入 LoginUser 信息。
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}