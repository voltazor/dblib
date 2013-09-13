package com.voltazor.dblib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 19.02.13
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
@Target(value= ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DatabaseTable {

    String name();

}
