/*
 * Copyright 2017 Courtanet
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
package io.doov.gen.utils;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;

class DependencyUtils {

    /**
     * @param artifact the artifact to resolve
     * @param project  the current Maven project
     * @return the resolved artifact from the current local repository
     */
    static File resolve(Artifact artifact, MavenProject project) {
        ProjectBuildingRequest projectBuildingRequest = project.getProjectBuildingRequest();
        ArtifactRepository localRepository = projectBuildingRequest.getLocalRepository();
        Artifact resolvedArtifact = localRepository.find(artifact);
        return resolvedArtifact != null ? resolvedArtifact.getFile() : null;
    }
}
