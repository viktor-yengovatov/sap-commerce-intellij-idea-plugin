package com.intellij.idea.plugin.hybris.runConfigurations;

import com.intellij.rt.execution.junit.IDEAJUnitListenerEx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Eugene.Kudelevsky
 */
public class HybrisJUnitListener implements IDEAJUnitListenerEx {

    @Override
    public void testRunStarted(final String name) {
        try {
            final Class<?> utilitiesClass = Class.forName("de.hybris.platform.util.Utilities");
            final Method setJUnitMethod = utilitiesClass.getMethod("setJUnitTenant");
            setJUnitMethod.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testRunFinished(final String name) {
    }

    @Override
    public void testFinished(final String className, final String methodName, final boolean succeed) {
    }

    @Override
    public void testStarted(final String className, final String methodName) {
    }

    @Override
    public void testFinished(final String className, final String methodName) {
    }
}
