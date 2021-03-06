package org.netbeans.gradle.project.tasks;

import java.util.concurrent.Callable;
import org.netbeans.gradle.project.NbGradleProject;
import org.netbeans.gradle.project.api.task.CommandCompleteListener;
import org.netbeans.gradle.project.api.task.CustomCommandActions;
import org.netbeans.gradle.project.api.task.GradleCommandExecutor;
import org.netbeans.gradle.project.api.task.GradleCommandTemplate;
import org.openide.util.Lookup;

public final class DefaultGradleCommandExecutor implements GradleCommandExecutor {
    private final NbGradleProject project;

    public DefaultGradleCommandExecutor(NbGradleProject project) {
        if (project == null) throw new NullPointerException("project");
        this.project = project;
    }

    @Override
    public void executeCommand(
            final GradleCommandTemplate command,
            final CustomCommandActions customActions) {

        if (command == null) throw new NullPointerException("command");
        if (customActions == null) throw new NullPointerException("customActions");

        Runnable asyncTask = GradleTasks.createAsyncGradleTask(project, new Callable<GradleTaskDef>() {
            @Override
            public GradleTaskDef call() {
                return GradleTaskDef.createFromTemplate(project, command, customActions, Lookup.EMPTY).create();
            }
        }, new CommandCompleteListener() {
            @Override
            public void onComplete(Throwable error) {
                try {
                    CommandCompleteListener completeListener = customActions.getCommandCompleteListener();
                    if (completeListener != null) {
                        completeListener.onComplete(error);
                    }
                } finally {
                    GradleTasks.projectTaskCompleteListener(project).onComplete(error);
                }
            }
        });
        asyncTask.run();
    }
}
