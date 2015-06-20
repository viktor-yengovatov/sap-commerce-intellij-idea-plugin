package com.intellij.idea.plugin.hybris.project.utils;

/**
 * Created 7:09 PM 20 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface Processor<T> {

    boolean shouldContinue(T t);

}
