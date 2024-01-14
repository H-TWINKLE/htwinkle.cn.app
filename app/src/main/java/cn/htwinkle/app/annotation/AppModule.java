package cn.htwinkle.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AppModule {
    String value();

    String description() default "";

    int defaultResourcesId() default 0;

    String imgResourcesUrl() default "";

    String[] permissions() default {};
}
