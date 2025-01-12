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

package com.dreameddeath.party.process;

import com.dreameddeath.core.exception.process.JobExecutionException;
import com.dreameddeath.core.model.process.EmptyJobResult;
import com.dreameddeath.core.process.business.DocumentCreateTask;
import com.dreameddeath.core.process.common.AbstractJob;
import com.dreameddeath.party.model.base.Organization;
import com.dreameddeath.party.model.base.Party;
import com.dreameddeath.party.model.base.Person;
import com.dreameddeath.party.model.process.CreatePartyRequest;

/**
 * Created by Christophe Jeunesse on 01/08/2014.
 */
public class CreatePartyJob extends AbstractJob<CreatePartyRequest,EmptyJobResult> {
    @Override
    public CreatePartyRequest newRequest(){return new CreatePartyRequest();}
    @Override
    public EmptyJobResult newResult(){return new EmptyJobResult();}

    @Override
    public boolean init() throws JobExecutionException{
        addTask(new CreatePartyTask());
        return false;
    }

    public static class CreatePartyTask extends DocumentCreateTask<Party>{
        @Override
        public Party buildDocument(){
            Party result;
            CreatePartyRequest req = getParentJob(CreatePartyJob.class).getRequest();
            if(req.type == CreatePartyRequest.Type.person){
                Person person=newEntity(Person.class);
                person.setFirstName(req.person.firstName);
                person.setLastName(req.person.lastName);
                result = person;
            }
            else{
                Organization organization = newEntity(Organization.class);
                organization.setBrand(req.organization.brand);
                organization.setTradingName(req.organization.tradingName);
                result = organization;
            }

            return result;
        }
    }
}
