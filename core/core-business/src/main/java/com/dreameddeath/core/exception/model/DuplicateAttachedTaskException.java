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

package com.dreameddeath.core.exception.model;

import com.dreameddeath.core.model.business.BusinessCouchbaseDocument;

/**
 * Created by ceaj8230 on 20/11/2014.
 */
public class DuplicateAttachedTaskException extends Exception {
    private String _taskUid;
    private String _jobKey;
    private BusinessCouchbaseDocument _doc;


    public DuplicateAttachedTaskException(BusinessCouchbaseDocument doc, String jobKey,String taskUid){
        this(doc,jobKey,taskUid,"The task <"+taskUid+"> is already existing of job <"+jobKey+">");
    }

    public DuplicateAttachedTaskException(BusinessCouchbaseDocument doc,String jobKey, String taskUid,  String message){
        super(message);
        _doc = doc;
        _jobKey = jobKey;
        _taskUid = taskUid;
    }

}
