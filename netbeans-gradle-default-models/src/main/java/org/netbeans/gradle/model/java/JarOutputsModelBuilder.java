package org.netbeans.gradle.model.java;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskCollection;
import org.netbeans.gradle.model.ProjectInfoBuilder;
import org.netbeans.gradle.model.util.ClassLoaderUtils;

public enum JarOutputsModelBuilder
implements
        ProjectInfoBuilder<JarOutputsModel> {

    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(JarOutputsModelBuilder.class.getName());

    public JarOutputsModel getProjectInfo(Project project) {
        if (!project.getPlugins().hasPlugin("java")) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Class<? extends Task> jarClass = (Class<? extends Task>)ClassLoaderUtils
                .tryGetClass(project, "org.gradle.api.tasks.bundling.Jar");

        if (jarClass == null) {
            LOGGER.warning("Cannot find class of Jar tasks.");
            return new JarOutputsModel(Collections.<JarOutput>emptySet());
        }

        List<JarOutput> result = new LinkedList<JarOutput>();
        TaskCollection<? extends Task> allJars = project.getTasks().withType(jarClass);

        for (Task jar: allJars) {
            result.add(new JarOutput(jar.getName(), (File)jar.property("archivePath")));
        }

        return new JarOutputsModel(result);
    }
}
