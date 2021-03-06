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
package org.apache.isis.objectstore.jdo.metamodel.facets.object.discriminator;

import java.util.List;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.FacetFactory;
import org.apache.isis.core.metamodel.facets.object.objectspecid.ObjectSpecIdFacet;
import org.apache.isis.core.metamodel.spec.ObjectSpecId;
import org.apache.isis.core.metamodel.facets.AbstractFacetFactoryTest;

public class GivenJdoDiscriminatorAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private JdoDiscriminatorAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new JdoDiscriminatorAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    public void testFeatureTypes() {
        final List<FeatureType> featureTypes = facetFactory.getFeatureTypes();
        assertTrue(contains(featureTypes, FeatureType.OBJECT));
        assertFalse(contains(featureTypes, FeatureType.PROPERTY));
        assertFalse(contains(featureTypes, FeatureType.COLLECTION));
        assertFalse(contains(featureTypes, FeatureType.ACTION));
        assertFalse(contains(featureTypes, FeatureType.ACTION_PARAMETER));
    }

    public void testDiscriminatorValueAnnotationPickedUpOnClass() {
        @Discriminator("CUS")
        class Customer {
        }

        facetFactory.process(new FacetFactory.ProcessClassContext(Customer.class, methodRemover, facetHolder));

        final Facet facet = facetHolder.getFacet(JdoDiscriminatorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof JdoDiscriminatorFacetDefault);
    }

    public void testObjectSpecIdAnnotationPickedUpOnClass() {
        @Discriminator("CUS")
        class Customer {
        }
        
        facetFactory.process(new FacetFactory.ProcessClassContext(Customer.class, methodRemover, facetHolder));
        
        final Facet facet = facetHolder.getFacet(ObjectSpecIdFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ObjectSpecIdFacetInferredFromJdoDiscriminatorValueAnnotation);
    }
    
    public void testIfNoEntityAnnotationThenNoFacet() {

        class Customer {
        }

        facetFactory.process(new FacetFactory.ProcessClassContext(Customer.class, methodRemover, facetHolder));

        final Facet facet = facetHolder.getFacet(ObjectSpecIdFacet.class);
        assertNull(facet);
    }

    public void testAnnotationValue() {
        @Discriminator("CUS")
        class Customer {
        }

        facetFactory.process(new FacetFactory.ProcessClassContext(Customer.class, methodRemover, facetHolder));

        final ObjectSpecIdFacet discriminatorValueFacet = facetHolder.getFacet(ObjectSpecIdFacet.class);
        assertEquals(ObjectSpecId.of("CUS"), discriminatorValueFacet.value());
    }

    public void testNoMethodsRemoved() {
        @PersistenceCapable
        class Customer {
        }

        facetFactory.process(new FacetFactory.ProcessClassContext(Customer.class, methodRemover, facetHolder));

        assertNoMethodsRemoved();
    }
}
