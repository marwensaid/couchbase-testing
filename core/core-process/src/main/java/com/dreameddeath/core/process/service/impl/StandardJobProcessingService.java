/*
 * Copyright Christophe Jeunesse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dreameddeath.core.process.service.impl;

import com.dreameddeath.core.process.exception.JobExecutionException;
import com.dreameddeath.core.process.model.AbstractJob;
import com.dreameddeath.core.process.service.IJobProcessingService;
import com.dreameddeath.core.process.service.JobContext;

/**
 * Created by Christophe Jeunesse on 25/11/2014.
 */
public abstract class StandardJobProcessingService<T extends AbstractJob> implements IJobProcessingService<T> {

    @Override
    public boolean preprocess(JobContext context, T job) throws JobExecutionException {
        return false;
    }

    @Override
    public boolean postprocess(JobContext context, T job) throws JobExecutionException {
        return false;
    }

    @Override
    public boolean cleanup(JobContext context, T job) throws JobExecutionException {
        return false;
    }
}
