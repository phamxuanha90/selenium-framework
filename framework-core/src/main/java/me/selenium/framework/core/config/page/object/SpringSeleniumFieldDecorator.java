package me.selenium.framework.core.config.page.object;

import me.selenium.framework.core.BaseComponent;
import me.selenium.framework.core.annotation.ComponentObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.*;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;

public class SpringSeleniumFieldDecorator extends DefaultFieldDecorator {
    private final ApplicationContext appContext;

    public SpringSeleniumFieldDecorator(ApplicationContext appContext) {
        super(new DefaultElementLocatorFactory(appContext.getBean(WebDriver.class)));
        this.appContext = appContext;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType.isAnnotationPresent(ComponentObject.class)) {
            AutowireCapableBeanFactory beanFactory = appContext.getAutowireCapableBeanFactory();
            Object componentObject = beanFactory.createBean(fieldType);
            PageFactory.initElements(new RelativeElementLocatorFactory(field, componentObject), componentObject);

            return componentObject;
        }
//    else if (fieldType.isAnnotationPresent(Repository.class)){
//        AutowireCapableBeanFactory beanFactory = appContext.getAutowireCapableBeanFactory();
//        Object repositoryObject = beanFactory.createBean(fieldType);
//        PageFactory.initElements(new RelativeElementLocatorFactory(field, repositoryObject), repositoryObject);
//
//        return repositoryObject;
//    }

        return super.decorate(loader, field);
    }

    class RelativeElementLocatorFactory implements ElementLocatorFactory {
        private final WebElement parentElement;

        public RelativeElementLocatorFactory(Field parentField, Object componentObject) {
            WebDriver webDriver = appContext.getBean(WebDriver.class);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            ElementLocator parentElementLocator = new DefaultElementLocator(webDriver, parentField);

            parentElement = proxyForLocator(classLoader, parentElementLocator);

            if (componentObject instanceof BaseComponent) {
                BaseComponent baseComponent = (BaseComponent) componentObject;
                baseComponent.setParentElement(parentElement);
            }
        }

        @Override
        public ElementLocator createLocator(Field field) {
            return new DefaultElementLocator(parentElement, field);
        }
    }
}
