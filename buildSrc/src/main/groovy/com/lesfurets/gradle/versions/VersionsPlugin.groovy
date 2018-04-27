package com.lesfurets.gradle.versions

import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionsPlugin implements Plugin<Project> {

    public static final String VERSIONS_PLUGIN_NAME = 'versions'
    public static final String NEW_VERSION = 'versions.newVersion'
    public static final String OLD_VERSION = 'versions.oldVersion'
    public static final String WRITE_VERSION_TASK_NAME = 'writeVersion'

    void apply(Project target) {

        def versions = target.rootProject.extensions.create(VERSIONS_PLUGIN_NAME, VersionsExtension, target.rootProject)
        def setVersionTask = target.rootProject.tasks.create(WRITE_VERSION_TASK_NAME, SetVersionTask)
        if (target.rootProject.properties[NEW_VERSION] != null) {
            String newProjectVersion = target.rootProject.properties[NEW_VERSION]
            def oldVersion = target.rootProject.version
            versions.oldVersion = oldVersion
            versions.newVersion = newProjectVersion
            target.rootProject.allprojects {
                it.ext."$OLD_VERSION" = oldVersion
                it.version = newProjectVersion
            }

            setVersionTask.newVersion = newProjectVersion
            setVersionTask.oldVersion = oldVersion
        }

    }

}
