/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.business.process.common.impl;

import com.intellij.idea.plugin.hybris.business.process.common.BpGraphNode;
import com.intellij.idea.plugin.hybris.business.process.jaxb.model.BpGenericAction;
import com.intellij.idea.plugin.hybris.business.process.jaxb.model.Process;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 2:41 PM 02 February 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BpGraphNodeImpl implements BpGraphNode {

    protected final BpGenericAction genericAction;
    protected final VirtualFile virtualFile;
    protected Map<String, BpGraphNode> transitions = new HashMap<String, BpGraphNode>();
    protected final Map<String, BpGraphNode> nodeMap;
    protected final Process process;

    public BpGraphNodeImpl(@NotNull final BpGenericAction genericAction,
                           @NotNull final Map<String, BpGraphNode> nodeMap,
                           @NotNull final VirtualFile virtualFile,
                           @NotNull final Process process) {
        Validate.notNull(genericAction);
        Validate.notNull(nodeMap);
        Validate.notNull(virtualFile);
        Validate.notNull(process);

        this.genericAction = genericAction;
        this.nodeMap = nodeMap;
        this.virtualFile = virtualFile;
        this.process = process;
    }

    @NotNull
    @Override
    public BpGenericAction getGenericAction() {
        return this.genericAction;
    }

    @NotNull
    @Override
    public VirtualFile getXmlVirtualFile() {
        return this.virtualFile;
    }

    @NotNull
    @Override
    public Map<String, BpGraphNode> getTransitions() {
        return this.transitions;
    }

    @NotNull
    @Override
    public Map<String, BpGraphNode> getNodesMap() {
        return this.nodeMap;
    }

    @NotNull
    @Override
    public Process getProcess() {
        return this.process;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final BpGraphNodeImpl other = (BpGraphNodeImpl) obj;

        return new EqualsBuilder()
            .append(this.genericAction.getId(), other.genericAction.getId())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(this.genericAction.getId())
            .toHashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BpGraphNodeImpl{");
        sb.append("genericAction=").append(this.genericAction.getId());
        sb.append('}');
        return sb.toString();
    }
}
