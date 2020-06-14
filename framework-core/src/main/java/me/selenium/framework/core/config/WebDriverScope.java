package me.selenium.framework.core.config;


import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebDriverScope implements Scope {
    public static final String NAME = "WEB_DRIVER";
    private final Map<String, Object> instances = new HashMap();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        synchronized (instances) {
            Object instance = instances.get(name);

            if (instance == null) {
                instance = objectFactory.getObject();
                instances.put(name, instance);
            }

            return instance;
        }
    }

    @Override
    public Object remove(String name) {
        synchronized (instances) {
            return instances.remove(name);
        }
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // do nothing
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    public boolean reset() {
        boolean reset = false;
        synchronized (instances) {
            Iterator iter = instances.values().iterator();

            while (iter.hasNext()) {
                Object instance = iter.next();
                reset = true;
                if (instance instanceof WebDriver) {
                    ((WebDriver) instance).quit();
                }
            }

            instances.clear();
            return reset;
        }
    }

    public static WebDriverScope getFrom(ApplicationContext context) {
        if (!(context instanceof ConfigurableApplicationContext)) {
            return null;
        }

        Scope scope = ((ConfigurableApplicationContext) context)
                .getBeanFactory()
                .getRegisteredScope(NAME);

        return (scope instanceof WebDriverScope) ? (WebDriverScope) scope : null;
    }
}
