/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.execution;

import org.gradle.api.Task;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.specs.Spec;
import org.gradle.api.specs.Specs;
import org.gradle.execution.plan.ExecutionPlan;
import org.gradle.initialization.TaskSchedulingPreparer;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link BuildConfigurationAction} which filters excluded tasks.
 */
public class ExcludedTaskFilteringProjectsPreparer implements TaskSchedulingPreparer {
    private final TaskSelector taskSelector;

    public ExcludedTaskFilteringProjectsPreparer(TaskSelector taskSelector) {
        this.taskSelector = taskSelector;
    }

    @Override
    public void prepareForTaskScheduling(GradleInternal gradle, ExecutionPlan executionPlan) {
        Set<String> excludedTaskNames = gradle.getStartParameter().getExcludedTaskNames();
        if (!excludedTaskNames.isEmpty()) {
            final Set<Spec<Task>> filters = new HashSet<Spec<Task>>();
            for (String taskName : excludedTaskNames) {
                filters.add(taskSelector.getFilter(taskName));
            }
            executionPlan.useFilter(Specs.intersect(filters));
        }
    }
}
