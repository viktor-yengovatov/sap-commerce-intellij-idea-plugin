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

package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.openapi.util.io.FileUtil;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 11:05 PM 24 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultJavaLibraryDescriptor implements JavaLibraryDescriptor {

    @NotNull
    private final File libraryFile;
    private final boolean isExported;
    private final boolean isDirectoryWithClasses;

    public DefaultJavaLibraryDescriptor(@NotNull final File libraryFile) {
        Validate.notNull(libraryFile);

        this.libraryFile = libraryFile;
        this.isExported = false;
        this.isDirectoryWithClasses = false;
    }

    public DefaultJavaLibraryDescriptor(@NotNull final File libraryFile,
                                        final boolean isExported) {
        Validate.notNull(libraryFile);

        this.libraryFile = libraryFile;
        this.isExported = isExported;
        this.isDirectoryWithClasses = false;
    }

    public DefaultJavaLibraryDescriptor(@NotNull final File libraryFile,
                                        final boolean isExported,
                                        final boolean isDirectoryWithClasses) {
        Validate.notNull(libraryFile);

        this.libraryFile = libraryFile;
        this.isExported = isExported;
        this.isDirectoryWithClasses = isDirectoryWithClasses;
    }

    @NotNull
    @Override
    public File getLibraryFile() {
        return libraryFile;
    }

    @Override
    public boolean isExported() {
        return isExported;
    }

    @Override
    public boolean isDirectoryWithClasses() {
        return isDirectoryWithClasses;
    }

    @Override
    public int compareTo(@NotNull final JavaLibraryDescriptor o) {
        return FileUtil.compareFiles(this.libraryFile, o.getLibraryFile());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(libraryFile)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }

        final DefaultJavaLibraryDescriptor other = (DefaultJavaLibraryDescriptor) obj;

        return new EqualsBuilder()
            .append(libraryFile, other.libraryFile)
            .isEquals();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultJavaLibraryDescriptor{");
        sb.append("libraryFile=").append(libraryFile);
        sb.append(", isExported=").append(isExported);
        sb.append(", isDirectoryWithClasses=").append(isDirectoryWithClasses);
        sb.append('}');
        return sb.toString();
    }
}
