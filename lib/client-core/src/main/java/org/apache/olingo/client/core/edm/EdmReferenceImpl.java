/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.client.core.edm;

import java.net.URI;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmReference;
import org.apache.olingo.commons.api.edm.EdmReferenceInclude;
import org.apache.olingo.commons.api.edm.EdmReferenceIncludeAnnotations;

public class EdmReferenceImpl implements EdmReference {
	private URI uri;
	private List<EdmReferenceInclude> includes;
	private List<EdmReferenceIncludeAnnotations> includeAnnotations;
	
	public EdmReferenceImpl(URI uri, List<EdmReferenceInclude> includes,
			List<EdmReferenceIncludeAnnotations> includeAnnotations) {
		this.uri = uri;
		this.includes = includes;
		this.includeAnnotations = includeAnnotations;
	}
	
	@Override
	public URI getUri() {
		return this.uri;
	}

	@Override
	public List<EdmReferenceInclude> getIncludes() {
		return this.includes;
	}

	@Override
	public List<EdmReferenceIncludeAnnotations> getIncludeAnnotations() {
		return this.includeAnnotations;
	}
}
