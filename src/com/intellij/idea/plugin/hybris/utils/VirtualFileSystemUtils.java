/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created 1:51 AM 13 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class VirtualFileSystemUtils {

    private VirtualFileSystemUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }

    public static void removeAllFiles(@NotNull final Collection<File> files) throws IOException {
        Validate.notNull(files);

        if (files.isEmpty()) {
            return;
        }

        final LocalFileSystem localFileSystem = LocalFileSystem.getInstance();

        for (File file : files) {
            final VirtualFile virtualFile = localFileSystem.findFileByIoFile(file);

            if (null != virtualFile) {
                ApplicationManager.getApplication().runWriteAction(new RemoveFileComputable(virtualFile));
            } else {
                FileUtil.delete(file);
            }
        }
    }

    private static class RemoveFileComputable implements ThrowableComputable<Void, IOException> {

        private final VirtualFile virtualFile;

        public RemoveFileComputable(@NotNull final VirtualFile virtualFile) {
            Validate.notNull(virtualFile);

            this.virtualFile = virtualFile;
        }

        @Override
        public Void compute() throws IOException {
            this.virtualFile.delete(this);

            return null;
        }
    }

    public static VirtualFile getByUrl(@NotNull final String url){
        Validate.notNull(url);
        return VirtualFileManager.getInstance().findFileByUrl(url);
    }

}
