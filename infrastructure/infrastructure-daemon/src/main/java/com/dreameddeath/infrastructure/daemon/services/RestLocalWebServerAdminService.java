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

import com.codahale.metrics.MetricRegistry;
import com.dreameddeath.infrastructure.daemon.AbstractDaemon;
import com.dreameddeath.infrastructure.daemon.model.WebServerInfo;
import com.dreameddeath.infrastructure.daemon.services.model.webserver.WebServerStatusResponse;
import com.dreameddeath.infrastructure.daemon.services.model.webserver.WebServerStatusUpdateRequest;
import com.dreameddeath.infrastructure.daemon.utils.ServerConnectorUtils;
import com.dreameddeath.infrastructure.daemon.webserver.AbstractWebServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christophe Jeunesse on 19/09/2015.
 */
@Api(value = "/", description = "Daemon Webservers Administration service",tags = {"webServers"})
@Path("/")
public class RestLocalWebServerAdminService {
    private static final Logger LOG = LoggerFactory.getLogger(RestLocalWebServerAdminService.class);
    private AbstractDaemon daemon;

    @Autowired
    public void setDaemon(AbstractDaemon daemon){
        this.daemon = daemon;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "give info on all web servers",
            response = WebServerInfo.class,
            responseContainer = "List",
            position = 0)
    public List<WebServerInfo> getInfo(){
        List<WebServerInfo> result = new ArrayList<>(daemon.getAdditionalWebServers().size());
        for(AbstractWebServer webServer:daemon.getAdditionalWebServers()){
            result.add(new WebServerInfo(webServer));
        }
        return result;
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("{name}")
    @ApiOperation(value = "give info on a given webserver",
            response = WebServerInfo.class,
            position = 1)
    public WebServerInfo getInfo(@PathParam("name") String name){
        return new WebServerInfo(findByName(name));
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("{name}/status")
    @ApiOperation(value = "give info on a given webserver",
            response = WebServerStatusResponse.class,
            position = 2)
    public WebServerStatusResponse getStatus(@PathParam("name") String name){
        return buildStatus(findByName(name));
    }

    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @Path("{name}/status")
    @ApiOperation(value = "give status info on a given webserver",
            response = WebServerInfo.class,
            position = 3)
    public WebServerStatusResponse putStatus(@PathParam("name") String name, WebServerStatusUpdateRequest statusUpdateRequest){
        AbstractWebServer webServer = findByName(name);
        try{
            switch (statusUpdateRequest.getAction()){
                case START:
                    LOG.info("Starting webServer {}",webServer.getName());
                    webServer.start();
                    break;
                case STOP:
                    LOG.info("Stopping webServer {}",webServer.getName());
                    webServer.stop();
                    break;
                case RESTART:
                    LOG.info("Restarting webServer {}",webServer.getName());
                    webServer.stop();
                    webServer.start();
                    break;
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }


        return buildStatus(webServer);
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("{name}/metrics")
    @ApiOperation(value = "give metrics on a given webserver",
            response = MetricRegistry.class)
    public MetricRegistry getMetrics(@PathParam("name") String name){
        return findByName(name).getMetricRegistry();
    }


    protected WebServerStatusResponse buildStatus(AbstractWebServer webServer){
        WebServerStatusResponse result = new WebServerStatusResponse();
        result.setAddress(ServerConnectorUtils.getConnectorHost(webServer.getServerConnector()));
        result.setPort(ServerConnectorUtils.getConnectorPortString(webServer.getServerConnector()));
        result.setStatus(webServer.getStatus());
        return result;
    }


    private AbstractWebServer findByName(String name) throws NotFoundException{
        for(AbstractWebServer webServer:daemon.getAdditionalWebServers()){
            if(webServer.getName().equals(name)){
                return webServer;
            }
        }
        throw new NotFoundException("Cannot find server "+name);
    }


}
