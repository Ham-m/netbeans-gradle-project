package org.netbeans.gradle.project.api.task;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.openide.util.Parameters;

/**
 * Defines custom actions to be associated with a
 * {@link GradleCommandTemplate Gradle command}.
 * <P>
 * Note that you can only instantiate this class using its {@link Builder}.
 * For common actions, you may use the declared {@code static final} fields.
 * <P>
 * Methods of this class are safe to be accessed by multiple threads
 * concurrently without any synchronization.
 *
 * @see BuiltInGradleCommandQuery
 * @see GradleCommandExecutor
 */
public final class CustomCommandActions {
    /**
     * Defines an immutable {@code CustomCommandActions} with the
     * {@link #getTaskKind() task kind} of {@link TaskKind#BUILD}.
     * <P>
     * Aside from the task kind property all other properties are {@code null}.
     */
    public static final CustomCommandActions BUILD = new Builder(TaskKind.BUILD).create();

    /**
     * Defines an immutable {@code CustomCommandActions} with the
     * {@link #getTaskKind() task kind} of {@link TaskKind#RUN}.
     * <P>
     * Aside from the task kind property all other properties are {@code null}.
     */
    public static final CustomCommandActions RUN = new Builder(TaskKind.RUN).create();

    /**
     * Defines an immutable {@code CustomCommandActions} with the
     * {@link #getTaskKind() task kind} of {@link TaskKind#DEBUG}.
     * <P>
     * Aside from the task kind property all other properties are {@code null}.
     */
    public static final CustomCommandActions DEBUG = new Builder(TaskKind.DEBUG).create();

    /**
     * Defines an immutable {@code CustomCommandActions} with the
     * {@link #getTaskKind() task kind} of {@link TaskKind#OTHER}.
     * <P>
     * Aside from the task kind property all other properties are {@code null}.
     */
    public static final CustomCommandActions OTHER = new Builder(TaskKind.OTHER).create();

    /**
     * Defines a class which might be used to create
     * {@link CustomCommandActions} instances. To create new
     * {@code CustomCommandActions} instances: Create a new
     * {@code CustomCommandActions.Builder}, set the appropriate properties and
     * call {@link #create() create()}.
     * <P>
     * Methods of this class cannot be accessed concurrently but access to its
     * methods might be synchronized with an external lock.
     */
    public static final class Builder {
        private final TaskKind taskKind;
        private CommandCompleteListener commandCompleteListener;
        private TaskOutputProcessor stdOutProcessor;
        private TaskOutputProcessor stdErrProcessor;
        private ContextAwareCommandAction contextAwareAction;

        /**
         * Creates a new {@code Builder} with the specified task kind and with
         * all other properties initially set to {@code null}. By default, you
         * should use {@link TaskKind#BUILD}.
         *
         * @param taskKind the kind of task affecting the output window
         *   handling of the executed task. This argument cannot be
         *   {@code null}.
         *
         * @throws NullPointerException thrown if the specified task kind is
         *   {@code null}
         */
        public Builder(@Nonnull TaskKind taskKind) {
            Parameters.notNull("taskKind", taskKind);

            this.taskKind = taskKind;
            this.commandCompleteListener = null;
            this.stdOutProcessor = null;
            this.stdErrProcessor = null;
            this.contextAwareAction = null;
        }

        /**
         * Sets the {@code CommandCompleteListener} to be executed after the
         * associated Gradle command completes. An invocation of this method
         * overwrites previous invocation of this method.
         * <P>
         * You may set this property to {@code null}, if there is nothing to do
         * after the Gradle command completes.
         *
         * @param commandCompleteListener the {@code CommandCompleteListener} to
         *   be executed after the associated Gradle command completes. This
         *   argument can be {@code null}, if there is nothing to do after the
         *   Gradle command completes.
         */
        public void setCommandCompleteListener(@Nullable CommandCompleteListener commandCompleteListener) {
            this.commandCompleteListener = commandCompleteListener;
        }

        /**
         * Sets the {@code TaskOutputProcessor} to be called after each line
         * written to the standard output by the associated Gradle command.
         * An invocation of this method overwrites previous invocation of this
         * method.
         * <P>
         * You may set this property to {@code null}, if there is nothing to do
         * after lines are written to the standard output.
         *
         * @param stdOutProcessor the {@code TaskOutputProcessor} to be called
         *   after each line written to the standard output by the associated
         *   Gradle command. This argument can be {@code null}, if there is
         *   nothing to do after lines are written to the standard output.
         */
        public void setStdOutProcessor(@Nullable TaskOutputProcessor stdOutProcessor) {
            this.stdOutProcessor = stdOutProcessor;
        }

        /**
         * Sets the {@code TaskOutputProcessor} to be called after each line
         * written to the standard error by the associated Gradle command.
         * An invocation of this method overwrites previous invocation of this
         * method.
         * <P>
         * You may set this property to {@code null}, if there is nothing to do
         * after lines are written to the standard error.
         *
         * @param stdErrProcessor the {@code TaskOutputProcessor} to be called
         *   after each line written to the standard error by the associated
         *   Gradle command. This argument can be {@code null}, if there is
         *   nothing to do after lines are written to the standard error.
         */
        public void setStdErrProcessor(@Nullable TaskOutputProcessor stdErrProcessor) {
            this.stdErrProcessor = stdErrProcessor;
        }

        /**
         * Sets the custom code to be executed before and after the Gradle
         * command has. The custom code may use the {@code Lookup} context
         * used to start the associated Gradle command and also the
         * {@link org.netbeans.api.project.Project Project} instance.
         * <P>
         * You may set this property to {@code null}, if there is no such action
         * is needed. The default value for this property is {@code null}.
         *
         * @param contextAwareAction the custom code to be executed before and
         *   after the Gradle command has. This argument can be {@code null}, if
         *   no code needs to be executed.
         */
        public void setContextAwareAction(@Nullable ContextAwareCommandAction contextAwareAction) {
            this.contextAwareAction = contextAwareAction;
        }

        /**
         * Creates a new {@code CustomCommandActions} instances with the
         * properties currently set for this builder. Subsequent modifications
         * to this builder has no effect on the returned instance.
         *
         * @return a new {@code CustomCommandActions} instances with the
         *   properties currently set for this builder. This method may never
         *   return {@code null}.
         */
        @Nonnull
        public CustomCommandActions create() {
            return new CustomCommandActions(this);
        }
    }

    private final TaskKind taskKind;
    private final CommandCompleteListener commandCompleteListener;
    private final TaskOutputProcessor stdOutProcessor;
    private final TaskOutputProcessor stdErrProcessor;
    private final ContextAwareCommandAction contextAwareAction;

    private CustomCommandActions(Builder builder) {
        this.taskKind = builder.taskKind;
        this.commandCompleteListener = builder.commandCompleteListener;
        this.stdOutProcessor = builder.stdOutProcessor;
        this.stdErrProcessor = builder.stdErrProcessor;
        this.contextAwareAction = builder.contextAwareAction;
    }

    /**
     * Returns the kind of tasks affecting the output window handling of the
     * executed task.
     *
     * @return the kind of tasks affecting the output window handling of the
     *   executed task. This method may never return {@code null}.
     */
    @Nonnull
    public TaskKind getTaskKind() {
        return taskKind;
    }

    /**
     * Returns the {@code CommandCompleteListener} to be executed after the
     * associated Gradle commands completes or {@code null} if there is no
     * action to be taken.
     *
     * @return the {@code CommandCompleteListener} to be executed after the
     *   associated Gradle commands completes or {@code null} if there is no
     *   action to be taken
     */
    @Nullable
    @CheckForNull
    public CommandCompleteListener getCommandCompleteListener() {
        return commandCompleteListener;
    }

    /**
     * Returns the {@code TaskOutputProcessor} to be called after each line
     * written to the standard output by the associated Gradle command or
     * {@code null} if there is nothing to do after lines are written to the
     * standard output.
     *
     * @return the {@code TaskOutputProcessor} to be called after each line
     *   written to the standard output by the associated Gradle command or
     *   {@code null} if there is nothing to do after lines are written to the
     *   standard output
     */
    @Nullable
    @CheckForNull
    public TaskOutputProcessor getStdOutProcessor() {
        return stdOutProcessor;
    }

    /**
     * Returns the {@code TaskOutputProcessor} to be called after each line
     * written to the standard error by the associated Gradle command or
     * {@code null} if there is nothing to do after lines are written to the
     * standard error.
     *
     * @return the {@code TaskOutputProcessor} to be called after each line
     *   written to the standard error by the associated Gradle command or
     *   {@code null} if there is nothing to do after lines are written to the
     *   standard error
     */
    @Nullable
    @CheckForNull
    public TaskOutputProcessor getStdErrProcessor() {
        return stdErrProcessor;
    }

    /**
     * Returns the custom code to be executed before and after the Gradle
     * command has. The custom code may use the {@code Lookup} context
     * used to start the associated Gradle command and also the
     * {@link org.netbeans.api.project.Project Project} instance.
     *
     * @return the custom code to be executed before and after the Gradle
     *   command has. This method may return {@code null}, if no such action
     *   needs to be executed.
     */
    @Nullable
    @CheckForNull
    public ContextAwareCommandAction getContextAwareAction() {
        return contextAwareAction;
    }
}
