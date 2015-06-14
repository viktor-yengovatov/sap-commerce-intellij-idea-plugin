package com.intellij.idea.plugin.hybris.project.exceptions;

/**
 * Created 11:22 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisConfigurationException extends Exception {

    public HybrisConfigurationException(final String message) {
        super(message);
    }

    public HybrisConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HybrisConfigurationException(final Throwable cause) {
        super(cause);
    }
}
