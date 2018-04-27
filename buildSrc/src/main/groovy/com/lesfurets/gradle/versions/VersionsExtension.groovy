package com.lesfurets.gradle.versions

import org.gradle.api.Project

class VersionsExtension {
    String newVersion
    String oldVersion

    VersionsExtension(Project project) {
        newVersion = project.version
        oldVersion = project.version
    }
}
