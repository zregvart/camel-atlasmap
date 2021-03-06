/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.camel.component.atlasmap;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.Metadata;
import org.apache.camel.util.ResourceHelper;

import io.atlasmap.api.AtlasContextFactory;

/**
 * @version
 */
public class AtlasComponent extends DefaultComponent {

    @Metadata(label = "advanced")
    private io.atlasmap.api.AtlasContextFactory atlasContextFactory;

    public AtlasComponent() {
    }

    public io.atlasmap.api.AtlasContextFactory getAtlasContextFactory() {
        return atlasContextFactory;
    }

    /**
     * To use the {@link AtlasContextFactory} otherwise a new engine is created
     */
    public void setAtlasContextFactory(AtlasContextFactory atlasContextFactory) {
        this.atlasContextFactory = atlasContextFactory;
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        boolean cache = getAndRemoveParameter(parameters, "contentCache", Boolean.class, Boolean.TRUE);

        AtlasEndpoint endpoint = new AtlasEndpoint(uri, this, remaining);
        setProperties(endpoint, parameters);
        endpoint.setContentCache(cache);
        endpoint.setAtlasContextFactory(getAtlasContextFactory());

        // if its a http resource then append any remaining parameters and update the
        // resource uri
        if (ResourceHelper.isHttpUri(remaining)) {
            remaining = ResourceHelper.appendParameters(remaining, parameters);
            endpoint.setResourceUri(remaining);
        }

        return endpoint;
    }
}
