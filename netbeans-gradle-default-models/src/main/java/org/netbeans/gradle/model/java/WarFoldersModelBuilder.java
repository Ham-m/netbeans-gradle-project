package org.netbeans.gradle.model.java;

import java.io.File;
import org.gradle.api.Project;
import org.netbeans.gradle.model.ProjectInfoBuilder;

public enum WarFoldersModelBuilder
implements
        ProjectInfoBuilder<WarFoldersModel> {

    INSTANCE;

    public WarFoldersModel getProjectInfo(Project project) {
        if (!project.getPlugins().hasPlugin("war")) {
            return null;
        }

        return new WarFoldersModel((File)project.property("webAppDir"));
    }
}
