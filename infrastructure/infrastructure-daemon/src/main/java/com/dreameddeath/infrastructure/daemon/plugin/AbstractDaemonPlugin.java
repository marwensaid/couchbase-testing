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

package com.dreameddeath.infrastructure.daemon.plugin;

import com.dreameddeath.infrastructure.daemon.AbstractDaemon;
import com.dreameddeath.infrastructure.daemon.model.DaemonMetricsInfo;

/**
 * Created by Christophe Jeunesse on 19/12/2015.
 */
public abstract class AbstractDaemonPlugin extends AbstractPlugin {
    private AbstractDaemon parentDaemon;


    public void enrichMetrics(DaemonMetricsInfo info){

    }

    public AbstractDaemonPlugin(AbstractDaemon daemon){
        parentDaemon = daemon;
    }
}
