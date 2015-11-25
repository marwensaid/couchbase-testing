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

package com.dreameddeath.infrastructure.daemon.services;

import com.dreameddeath.core.json.JsonProviderFactory;
import com.dreameddeath.core.service.client.ServiceClientFactory;
import com.dreameddeath.core.service.config.model.UpdateKeyResult;
import com.dreameddeath.core.service.utils.ServiceObjectMapperConfigurator;
import com.dreameddeath.infrastructure.daemon.discovery.DaemonDiscovery;
import com.dreameddeath.infrastructure.daemon.model.DaemonInfo;
import com.dreameddeath.infrastructure.daemon.model.WebServerInfo;
import com.dreameddeath.infrastructure.daemon.services.model.daemon.StatusResponse;
import com.dreameddeath.infrastructure.daemon.services.model.daemon.StatusUpdateRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by Christophe Jeunesse on 03/10/2015.
 */
@Path("/daemons")
@Api(value = "/daemons", description = "Daemons Discovery And Administration service")
public class RestDaemonsDiscoveryAndAdminService {
    @Autowired(required = true)
    private DaemonDiscovery daemonDiscovery;
    @Autowired(required = true)
    private ServiceClientFactory serviceFactory;

    public void setDaemonDiscovery(DaemonDiscovery daemonDiscovery){
        this.daemonDiscovery = daemonDiscovery;
    }

    public void setClientFactory(ServiceClientFactory factory){
        serviceFactory = factory;
    }

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DaemonInfo> getDaemons() throws Exception{
        return daemonDiscovery.getList();
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public DaemonInfo getDaemon(@PathParam("id") String uid) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .request(MediaType.APPLICATION_JSON)
                .get(DaemonInfo.class);
    }

    @GET
    @Path("/{id}/config")
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> getDaemonConfig(@PathParam("id") String uid) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/config")
                .request(MediaType.APPLICATION_JSON)
                .get(Map.class);
    }


    @GET
    @Path("/{id}/config/{domain}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> getDaemonConfigDomain(@PathParam("id") String uid,@PathParam("domain")String domain) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/config/{domain}")
                .resolveTemplate("domain",domain)
                .request(MediaType.APPLICATION_JSON)
                .get(Map.class);
    }

    @PUT
    @Path("/{id}/config/{domain}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    public List<UpdateKeyResult> getDaemonConfigDomain(@PathParam("id") String uid,@PathParam("domain")String domain,Map<String,String> updateRequest) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/config/{domain}")
                .resolveTemplate("domain",domain)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updateRequest,MediaType.APPLICATION_JSON), new GenericType<List<UpdateKeyResult>>(){});
    }


    @GET
    @Path("/{id}/config/{domain}/{key}")
    @Produces({ MediaType.TEXT_PLAIN })
    public String getDaemonConfigDomain(@PathParam("id") String uid,@PathParam("domain")String domain,@PathParam("key")String key) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/config/{domain}/{key}")
                .resolveTemplate("domain",domain)
                .resolveTemplate("key",key)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
    }

    @POST
    @Path("/{id}/config/{domain}/{key}")
    @Produces({ MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.TEXT_PLAIN })
    public String addDaemonConfigDomain(@PathParam("id") String uid,@PathParam("domain")String domain,@PathParam("key")String key,String value) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/config/{domain}/{key}")
                .resolveTemplate("domain",domain)
                .resolveTemplate("key",key)
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(value),String.class);
    }

    @PUT
    @Path("/{id}/config/{domain}/{key}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.TEXT_PLAIN })
    public UpdateKeyResult updateDaemonConfigDomain(@PathParam("id") String uid,@PathParam("domain")String domain,@PathParam("key")String key,String value) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/config/{domain}/{key}")
                .resolveTemplate("domain",domain)
                .resolveTemplate("key",key)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.text(value),UpdateKeyResult.class);
    }




    @GET
    @Path("/{id}/status")
    @Produces({ MediaType.APPLICATION_JSON })
    public StatusResponse getDaemonStatus(@PathParam("id") String uid) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION,uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/status")
                .request(MediaType.APPLICATION_JSON)
                .get(StatusResponse.class);
    }

    @PUT
    @Path("/{id}/status")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    public StatusResponse updateDaemonStatus(@PathParam("id") String uid,StatusUpdateRequest statusUpdateRequest) throws Exception{
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION, uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/status")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(statusUpdateRequest), StatusResponse.class);
    }

    @GET
    @Path("/{id}/webservers")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<WebServerInfo> getWebservers(@PathParam("id") String uid){
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION, uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/webservers")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<WebServerInfo>>() {});
    }

    @GET
    @Path("/{id}/webservers/{wid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public WebServerInfo getWebserverInfo(@PathParam("id") String uid,@PathParam("wid") String webServerId){
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION, uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/webservers/{wid}")
                .resolveTemplate("wid", webServerId)
                .request(MediaType.APPLICATION_JSON)
                .get(WebServerInfo.class);
    }

    @GET
    @Path("/{id}/webservers/{wid}/status")
    @Produces({ MediaType.APPLICATION_JSON })
    public com.dreameddeath.infrastructure.daemon.services.model.webserver.StatusResponse getWebserverStatus(@PathParam("id") String uid,@PathParam("wid") String webServerId){
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION, uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/webservers/{wid}/status")
                .resolveTemplate("wid", webServerId)
                .request(MediaType.APPLICATION_JSON)
                .get(com.dreameddeath.infrastructure.daemon.services.model.webserver.StatusResponse.class);
    }

    @PUT
    @Path("/{id}/webservers/{wid}/status")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    public com.dreameddeath.infrastructure.daemon.services.model.webserver.StatusResponse updateWebserverStatus(@PathParam("id") String uid,@PathParam("wid") String webServerId,com.dreameddeath.infrastructure.daemon.services.model.webserver.StatusUpdateRequest updateRequest){
        return serviceFactory
                .getClient(RestLocalDaemonAdminService.DAEMON_SERVICE_NAME, RestLocalDaemonAdminService.DAEMON_SERVICE_VERSION, uid)
                .register(JsonProviderFactory.getProvider(ServiceObjectMapperConfigurator.SERVICE_MAPPER_CONFIGURATOR))
                .path("/webservers/{wid}/status")
                .resolveTemplate("wid", webServerId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(updateRequest), com.dreameddeath.infrastructure.daemon.services.model.webserver.StatusResponse.class);
    }
}
