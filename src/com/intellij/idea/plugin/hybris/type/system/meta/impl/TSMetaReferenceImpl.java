/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.type.system.meta.impl;

import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaClassifier;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaReference;
import com.intellij.idea.plugin.hybris.type.system.model.Relation;
import com.intellij.idea.plugin.hybris.type.system.model.RelationElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TSMetaReferenceImpl extends TSMetaEntityImpl<Relation> implements TSMetaReference {

    private final ReferenceEndImpl mySourceEnd;
    private final ReferenceEndImpl myTargetEnd;

    public TSMetaReferenceImpl(final @NotNull TSMetaModelImpl metaModel, final @NotNull Relation dom) {
        super(extractName(dom), dom);
        mySourceEnd = new ReferenceEndImpl(metaModel, dom.getSourceElement());
        myTargetEnd = new ReferenceEndImpl(metaModel, dom.getTargetElement());
    }

    private static String extractName(final @NotNull Relation domRelation) {
        return domRelation.getCode().getValue();
    }

    @NotNull
    @Override
    public ReferenceEnd getSource() {
        return mySourceEnd;
    }

    @NotNull
    @Override
    public ReferenceEnd getTarget() {
        return myTargetEnd;
    }

    private static class ReferenceEndImpl implements ReferenceEnd {

        private final TSMetaModelImpl myMetaModel;
        private final String myRoleName;
        private final String myEndType;

        public ReferenceEndImpl(final @NotNull TSMetaModelImpl metaModel, final @NotNull RelationElement dom) {
            myMetaModel = metaModel;
            myRoleName = dom.getQualifier().getValue();
            myEndType = dom.getType().getStringValue();
        }

        @NotNull
        @Override
        public String getTypeName() {
            return myEndType;
        }

        @Nullable
        @Override
        public TSMetaClassifier<?> resolveType() {
            return myMetaModel.findMetaClassByName(getTypeName());
        }

        @NotNull
        @Override
        public String getRole() {
            return myRoleName;
        }
    }
}
