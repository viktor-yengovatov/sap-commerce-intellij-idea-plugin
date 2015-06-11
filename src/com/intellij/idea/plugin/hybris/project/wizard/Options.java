package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;

public class Options {

    public static final String ECLIPSE_REMOTE_PROJECT_STORAGE = "eclipse.remote.project.storage";
    public static Options defValue = new Options();

    @NonNls
    public String commonModulesDirectory;

    public boolean reuseOutputPaths = false;

    public static String getProjectStorageDir(final Project project) {
        return PropertiesComponent.getInstance().getValue(ECLIPSE_REMOTE_PROJECT_STORAGE);
    }

    public static void saveProjectStorageDir(final String dir) {
        PropertiesComponent.getInstance().setValue(ECLIPSE_REMOTE_PROJECT_STORAGE, dir);
    }
}