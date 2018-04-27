package com.lesfurets.gradle.git

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class GitPushTask extends DefaultTask {

    @Input
    @Optional
    Collection<String> remotes = ['origin']

    @Input
    @Optional
    boolean tags = true

    @Input
    @Optional
    boolean dryRun = false

    @TaskAction
    def action() {
        def pushArgs = ['push']
        if (dryRun) {
            pushArgs += '--dry-run'
        }
        pushArgs += remotes
        if (tags) {
            pushArgs += '--tags'
        }
        project.exec {
            executable 'git'
            args pushArgs
        }
    }
}
