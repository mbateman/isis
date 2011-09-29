/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.viewer.json.viewer.resources.capabilities;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.isis.viewer.json.applib.JsonRepresentation;
import org.apache.isis.viewer.json.applib.RepresentationType;
import org.apache.isis.viewer.json.applib.RestfulMediaType;
import org.apache.isis.viewer.json.applib.RestfulResponse.HttpStatusCode;
import org.apache.isis.viewer.json.applib.capabilities.CapabilitiesResource;
import org.apache.isis.viewer.json.viewer.JsonApplicationException;
import org.apache.isis.viewer.json.viewer.representations.LinkReprBuilder;
import org.apache.isis.viewer.json.viewer.resources.ResourceAbstract;

/**
 * Implementation note: it seems to be necessary to annotate the implementation with {@link Path} rather than the
 * interface (at least under RestEasy 1.0.2 and 1.1-RC2).
 */
@Path("/capabilities")
public class CapabilitiesResourceServerside extends ResourceAbstract implements CapabilitiesResource {


    @GET
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_CAPABILITIES})
    public Response capabilities() {
        init();
        fakeRuntimeExceptionIfXFail();

        JsonRepresentation representation = JsonRepresentation.newMap();
        representation.mapPut("self", LinkReprBuilder.newBuilder(getResourceContext(), "self", "capabilities/").build());
        
        JsonRepresentation capabilities = JsonRepresentation.newMap();
        representation.mapPut("capabilities", capabilities);

        capabilities.mapPut("concurrencyChecking", "no");
        capabilities.mapPut("transientObjects", "no");
        capabilities.mapPut("deleteObjects", "no");
        capabilities.mapPut("simpleArguments", "no");
        capabilities.mapPut("partialArguments", "no");
        capabilities.mapPut("followLinks", "no");
        capabilities.mapPut("validateOnly", "no");
        capabilities.mapPut("pagination", "no");
        capabilities.mapPut("sorting", "no");
        capabilities.mapPut("domainModel", "rich");

        representation.mapPut("links", JsonRepresentation.newArray());
        representation.mapPut("extensions", JsonRepresentation.newMap());
        
        return responseOfOk(RepresentationType.CAPABILITIES, Caching.ONE_DAY, representation).build();
    }

    private void fakeRuntimeExceptionIfXFail() {
        HttpHeaders httpHeaders = getResourceContext().getHttpHeaders();
        if(httpHeaders.getRequestHeader("X-Fail") != null) {
            throw JsonApplicationException.create(HttpStatusCode.METHOD_FAILURE);
        }
    }


}