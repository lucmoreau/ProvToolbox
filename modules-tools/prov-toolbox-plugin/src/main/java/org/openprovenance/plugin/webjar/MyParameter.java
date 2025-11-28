package org.openprovenance.plugin.webjar;


import java.lang.annotation.*;

/* Same as Parameter from Maven, but retention is runtime */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface MyParameter {
    String alias() default "";

    String property() default "";

    String defaultValue() default "";

    boolean required() default false;

    boolean readonly() default false;
}
