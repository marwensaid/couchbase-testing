/*
 * Copyright Christophe Jeunesse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.dreameddeath.core.process.service;

import com.dreameddeath.core.exception.process.TaskExecutionException;
import com.dreameddeath.core.model.process.AbstractTask;

/**
 * Created by ceaj8230 on 23/11/2014.
 */
public interface ITaskProcessingService<T extends AbstractTask> {
    public boolean init(TaskContext ctxt,T task) throws TaskExecutionException;
    public boolean preprocess(TaskContext ctxt,T task) throws TaskExecutionException;
    public boolean process(TaskContext ctxt,T task) throws TaskExecutionException;
    public boolean postprocess(TaskContext ctxt,T task) throws TaskExecutionException;
    public boolean finish(TaskContext ctxt,T task) throws TaskExecutionException;
    public boolean cleanup(TaskContext ctxt,T task) throws TaskExecutionException;
}
