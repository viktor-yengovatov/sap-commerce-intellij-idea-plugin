/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019 EPAM Systems <hybrisideaplugin@epam.com>
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

package com.intellij.idea.plugin.hybris.tools.remote.console.persistence.services.impl;

import com.intellij.idea.plugin.hybris.tools.remote.console.persistence.cache.HybrisConsoleRegionsCache;
import com.intellij.idea.plugin.hybris.tools.remote.console.persistence.pojo.Region;
import com.intellij.idea.plugin.hybris.tools.remote.console.persistence.pojo.RegionEntity;
import com.intellij.idea.plugin.hybris.tools.remote.console.persistence.services.RegionEntityService;
import com.intellij.util.Base64;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DefaultRegionEntityService implements RegionEntityService {

    private static final String SPLIT_SIGN = "_";

    private HybrisConsoleRegionsCache cache;

    public DefaultRegionEntityService(HybrisConsoleRegionsCache cache) {
        this.cache = cache;
    }

    @Override
    public <T> RegionEntity<T> save(final String regionName, final String entityName, final T entityBody) {
        String uid = Base64.encode(regionName.getBytes()) + SPLIT_SIGN + UUID.randomUUID();
        RegionEntity<T> createdEntity = new RegionEntity<>(uid, entityName, entityBody);

        getRegionByName(regionName).getEntities().put(uid, createdEntity);
        return createdEntity;
    }

    @Override
    public Optional<RegionEntity> find(final String entityId) {
        Region region = getRegionByEntityId(entityId);
        Map<String, RegionEntity> entities = region.getEntities();
        return Optional.ofNullable(entities.get(entityId));
    }

    @Override
    public void remove(final String entityId) {
        Region region = getRegionByEntityId(entityId);
        region.getEntities().remove(entityId);
    }

    @Override
    public Map<String, RegionEntity> getAll(final String regionName) {
        return Collections.unmodifiableMap(cache.findRegion(regionName).getEntities());
    }

    @Override
    public void removeAll(final String regionName) {
        cache.findRegion(regionName).getEntities().clear();
    }

    private Region getRegionByEntityId(final String entityId) {
        String regionName = new String(Base64.decode(entityId.split(SPLIT_SIGN)[0]));
        return getRegionByName(regionName);
    }

    private Region getRegionByName(final String regionName) {
        return Optional.ofNullable(cache.findRegion(regionName))
                       .orElseThrow(() -> new IllegalStateException("Region with name: " + regionName + " doesn't exist."));
    }
}