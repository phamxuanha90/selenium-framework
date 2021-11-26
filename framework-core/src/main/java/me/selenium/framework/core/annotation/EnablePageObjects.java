package me.selenium.framework.core.annotation;

import me.selenium.framework.core.config.CoreConfig;
import me.selenium.framework.core.config.page.object.PageObjectDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({CoreConfig.class, PageObjectDefinitionRegistrar.class})
public @interface EnablePageObjects {
    String[] basePackages() default "";
}
