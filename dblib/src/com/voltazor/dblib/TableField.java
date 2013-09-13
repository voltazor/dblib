package com.voltazor.dblib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 19.02.13
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
@Target(value= ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TableField {

    String name();

    String type();

    boolean isId() default false;

}
