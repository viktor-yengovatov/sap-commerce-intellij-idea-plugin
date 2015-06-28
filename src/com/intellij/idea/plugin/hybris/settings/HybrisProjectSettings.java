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

package com.intellij.idea.plugin.hybris.settings;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created 6:51 PM 28 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectSettings {

    protected boolean hybisProject;

    public HybrisProjectSettings() {
        this.hybisProject = false;
    }

    public boolean isHybisProject() {
        return hybisProject;
    }

    public void setHybisProject(final boolean hybisProject) {
        this.hybisProject = hybisProject;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(hybisProject)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (null == o || getClass() != o.getClass()) {
            return false;
        }

        final HybrisProjectSettings other = (HybrisProjectSettings) o;

        return new EqualsBuilder()
            .append(hybisProject, other.hybisProject)
            .isEquals();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HybrisProjectSettings{");
        sb.append("hybisProject=").append(hybisProject);
        sb.append('}');
        return sb.toString();
    }
}
