/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.gen.utils;

import static java.lang.Thread.currentThread;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.net.*;
import java.util.*;
import java.util.function.Function;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

public class ClassLoaderUtils {

    public static URLClassLoader getUrlClassLoader(MavenProject project) throws MojoFailureException {
        try {
            // collect compile classpath classes
            final List<File> files = new ArrayList<>();
            files.addAll(project.getCompileClasspathElements().stream().map(File::new).collect(toList()));

            // collect project dependencies
            files.addAll(project.getDependencyArtifacts().stream()
                            .map(a -> DependencyUtils.resolve(a, project))
                            .filter(Objects::nonNull)
                            .collect(toList()));

            final List<URL> urls = files.stream()
                            .filter(f -> f != null && f.exists())
                            .map(new FileToUrl())
                            .collect(toList());

            return new URLClassLoader(urls.toArray(new URL[urls.size()]), currentThread().getContextClassLoader());
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    private static final class FileToUrl implements Function<File, URL> {

        @Override
        public URL apply(File file) {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
