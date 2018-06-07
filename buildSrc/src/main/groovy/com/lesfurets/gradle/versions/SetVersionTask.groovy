package com.lesfurets.gradle.versions

import org.gradle.api.BuildCancelledException
import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskInternal
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.util.VersionNumber

class SetVersionTask extends DefaultTask {

    @Input
    String newVersion = project.properties[VersionsPlugin.NEW_VERSION]

    @Input
    String oldVersion = project.version

    String group = VersionsPlugin.VERSIONS_PLUGIN_NAME

    @Override
    Spec<? super TaskInternal> getOnlyIf() {
        return {
            newVersion != oldVersion
        }
    }

    @TaskAction
    def action() {
        // verify version format
        def version = VersionNumber.parse(newVersion)
        if (version == VersionNumber.UNKNOWN) {
            throw new BuildCancelledException("Unknown version format: ${newVersion}")
        }
        if (version != oldVersion) {
            String contents = project.buildFile.getText("UTF-8")
            String currentVersionDeclaration = "version = \"${oldVersion}\""
            if (contents.find(currentVersionDeclaration)) {
                contents = contents.replaceFirst(currentVersionDeclaration, "version = \"${newVersion}\"")
                project.buildFile.write(contents, "UTF-8")
            }
        }
    }

}
