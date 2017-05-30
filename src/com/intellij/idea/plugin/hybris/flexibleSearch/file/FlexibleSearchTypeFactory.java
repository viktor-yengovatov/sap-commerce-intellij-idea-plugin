package com.intellij.idea.plugin.hybris.flexibleSearch.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class FlexibleSearchTypeFactory extends FileTypeFactory {

    @Override
    public void createFileTypes(@NotNull final FileTypeConsumer consumer) {
        consumer.consume(
            FlexibleSearchFileType.getInstance(),
            FlexibleSearchFileType.getInstance().getDefaultExtension()
        );
    }

}
