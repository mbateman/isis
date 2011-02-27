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


package org.apache.isis.core.progmodel.facets.object.value;

import org.apache.isis.core.commons.authentication.AuthenticationSessionProvider;
import org.apache.isis.core.commons.authentication.AuthenticationSessionProviderAware;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.config.IsisConfigurationAware;
import org.apache.isis.core.metamodel.adapter.map.AdapterMap;
import org.apache.isis.core.metamodel.adapter.map.AdapterMapAware;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.core.metamodel.runtimecontext.DependencyInjector;
import org.apache.isis.core.metamodel.runtimecontext.DependencyInjectorAware;


public abstract class ValueUsingValueSemanticsProviderFacetFactory<T> extends FacetFactoryAbstract implements 
        IsisConfigurationAware, AuthenticationSessionProviderAware, AdapterMapAware, DependencyInjectorAware  {

    private IsisConfiguration configuration;
    private AuthenticationSessionProvider authenticationSessionProvider;
    private AdapterMap adapterManager;
    private DependencyInjector dependencyInjector;
    /**
     * Lazily created.
     */
    private ValueSemanticsProviderContext context;

    protected ValueUsingValueSemanticsProviderFacetFactory(final Class<? extends Facet> adapterFacetType) {
        super(FeatureType.OBJECTS_ONLY);
    }

    protected void addFacets(final ValueSemanticsProviderAndFacetAbstract<T> adapter) {
        ValueFacetUsingSemanticsProvider facet = new ValueFacetUsingSemanticsProvider(adapter, adapter, getContext());
        FacetUtil.addFacet(facet);
    }

    // ////////////////////////////////////////////////////
    // Dependencies (injected via setter)
    // ////////////////////////////////////////////////////

    public IsisConfiguration getConfiguration() {
        return configuration;
    }

    public ValueSemanticsProviderContext getContext() {
        if(context == null) {
            context = new ValueSemanticsProviderContext(authenticationSessionProvider, getSpecificationLookup(), adapterManager, dependencyInjector);
        }
        return context;
    }

    @Override
    public void setIsisConfiguration(IsisConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setAuthenticationSessionProvider(AuthenticationSessionProvider authenticationSessionProvider) {
        this.authenticationSessionProvider = authenticationSessionProvider;
    }

    @Override
    public void setAdapterMap(AdapterMap adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public void setDependencyInjector(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
    }

}