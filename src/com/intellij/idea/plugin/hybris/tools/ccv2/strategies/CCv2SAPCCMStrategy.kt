/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for IntelliJ IDEA.
 * Copyright (C) 2019-2024 EPAM Systems <hybrisideaplugin@epam.com> and contributors
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

package com.intellij.idea.plugin.hybris.tools.ccv2.strategies

import com.intellij.idea.plugin.hybris.settings.CCv2Subscription
import com.intellij.idea.plugin.hybris.settings.components.ApplicationSettingsComponent
import com.intellij.idea.plugin.hybris.tools.ccm.SAPCCM
import com.intellij.idea.plugin.hybris.tools.ccm.SAPCCMBuildCommands
import com.intellij.idea.plugin.hybris.tools.ccm.SAPCCMDeploymentCommands
import com.intellij.idea.plugin.hybris.tools.ccm.SAPCCMEnvironmentCommands
import com.intellij.idea.plugin.hybris.tools.ccv2.dto.*
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.platform.util.progress.ProgressReporter
import com.intellij.platform.util.progress.reportProgress
import java.util.*

@Service
class CCv2SAPCCMStrategy : CCv2Strategy {

    override suspend fun fetchEnvironments(
        project: Project,
        ccv2Token: String,
        subscriptions: Collection<CCv2Subscription>
    ): SortedMap<CCv2Subscription, Collection<CCv2Environment>> = reportProgress(subscriptions.size + 1) {

        val appSettings = ApplicationSettingsComponent.getInstance()
        authCredentials(project, appSettings, it, ccv2Token) ?: return@reportProgress sortedMapOf()

        SAPCCMEnvironmentCommands.list(project, appSettings, it, subscriptions)
    }

    override suspend fun fetchBuilds(
        project: Project,
        ccv2Token: String,
        subscriptions: Collection<CCv2Subscription>
    ): SortedMap<CCv2Subscription, Collection<CCv2Build>> = reportProgress(subscriptions.size + 1) {
        val appSettings = ApplicationSettingsComponent.getInstance()
        authCredentials(project, appSettings, it, ccv2Token) ?: return@reportProgress sortedMapOf()

        SAPCCMBuildCommands.list(project, appSettings, it, subscriptions)
    }

    override suspend fun fetchDeployments(
        project: Project,
        ccv2Token: String,
        subscriptions: Collection<CCv2Subscription>
    ): SortedMap<CCv2Subscription, Collection<CCv2Deployment>> = reportProgress(subscriptions.size + 1) {
        val appSettings = ApplicationSettingsComponent.getInstance()
        authCredentials(project, appSettings, it, ccv2Token) ?: return@reportProgress sortedMapOf()

        SAPCCMDeploymentCommands.list(project, appSettings, it, subscriptions)
    }

    override suspend fun createBuild(
        project: Project,
        ccv2Token: String,
        subscription: CCv2Subscription,
        name: String,
        branch: String
    ): String? = reportProgress(2) {
        val appSettings = ApplicationSettingsComponent.getInstance()
        authCredentials(project, appSettings, it, ccv2Token) ?: return@reportProgress null

        it.sizedStep(1, "Waiting for API response...") {
            SAPCCMBuildCommands.create(project, appSettings, subscription, name, branch)
        }
    }

    override suspend fun deleteBuild(project: Project, ccv2Token: String, subscription: CCv2Subscription, build: CCv2Build) = reportProgress(2) {
        val appSettings = ApplicationSettingsComponent.getInstance()
        authCredentials(project, appSettings, it, ccv2Token) ?: return@reportProgress

        it.sizedStep(1, "Waiting for API response...") {
            SAPCCMBuildCommands.delete(project, appSettings, subscription, build)
        }
    }

    override suspend fun deployBuild(
        project: Project,
        ccv2Token: String,
        subscription: CCv2Subscription,
        environment: CCv2Environment,
        build: CCv2Build,
        mode: CCv2DeploymentDatabaseUpdateModeEnum,
        strategy: CCv2DeploymentStrategyEnum
    ): String? {
        TODO("Not yet implemented")
    }

    override suspend fun fetchEnvironmentsBuilds(
        project: Project,
        ccv2Token: String,
        subscriptions: Map<CCv2Subscription, Collection<CCv2Environment>>
    ) {
    }

    private suspend fun authCredentials(project: Project, appSettings: ApplicationSettingsComponent, progressReporter: ProgressReporter, ccv2Token: String): List<String>? {
        return progressReporter.sizedStep(1, "Authenticating with the provided token...") {
            SAPCCM.execute(project, appSettings, "config", "set", "auth-credentials", ccv2Token)
        }
    }
}