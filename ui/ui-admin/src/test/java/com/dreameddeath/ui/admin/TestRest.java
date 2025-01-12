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

package com.dreameddeath.ui.admin;

import com.dreameddeath.core.service.annotation.ServiceDef;
import com.dreameddeath.core.service.annotation.VersionStatus;
import com.dreameddeath.core.service.model.AbstractExposableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christophe Jeunesse on 24/08/2015.
 */
@Path("/tests")
@ServiceDef(domain = "test",name="test",version="1.0",status = VersionStatus.STABLE)
@Api
public class TestRest extends AbstractExposableService{
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation("get standardized message")
    public Map<String, Object> genericGet(){
        Map<String,Object> result = new HashMap<>();
        result.put("message","anonymous");
        return result;
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation("get standard generated message")
    public Map<String, Object> genericGet(@PathParam("id") String id){
        Map<String,Object> result = new HashMap<>();
        result.put("message","Welcome to you : "+id);
        return result;
    }
}
