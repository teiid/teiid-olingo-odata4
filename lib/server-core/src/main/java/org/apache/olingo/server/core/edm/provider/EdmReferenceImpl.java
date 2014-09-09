/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.server.core.edm.provider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmReference;
import org.apache.olingo.commons.api.edm.EdmReferenceInclude;
import org.apache.olingo.commons.api.edm.EdmReferenceIncludeAnnotations;
import org.apache.olingo.server.api.edm.provider.Reference;
import org.apache.olingo.server.api.edm.provider.Reference.Include;
import org.apache.olingo.server.api.edm.provider.Reference.IncludeAnnotations;

public class EdmReferenceImpl implements EdmReference {
	private Reference reference;
	private List<EdmReferenceInclude> edmIncludes;
	private List<EdmReferenceIncludeAnnotations> edmIncludeAnnotations;
	
	public EdmReferenceImpl(Reference reference) {
		this.reference = reference;
	}
	
	@Override
	public URI getUri() {
		return this.reference.getUri();
	}

	@Override
	public List<EdmReferenceInclude> getIncludes() {
		if (this.edmIncludes == null) {
			buildIncludes();
		}
		return this.edmIncludes;
	}
	
	private void buildIncludes() {
		List<Include> includes = this.reference.getIncludes();
		if (includes != null && !includes.isEmpty()) {
			this.edmIncludes = new ArrayList<EdmReferenceInclude>();
			for (Include incl:includes) {
				this.edmIncludes.add(new EdmReferenceIncludeImpl(incl));
			}
		}
	}

	@Override
	public List<EdmReferenceIncludeAnnotations> getIncludeAnnotations() {
		if (this.edmIncludeAnnotations == null) {
			buildIncludeAnnotations();
		}
		return this.edmIncludeAnnotations;
	}

	private void buildIncludeAnnotations() {
		List<IncludeAnnotations> includes = this.reference.getIncludeAnnotations();
		if (includes != null && !includes.isEmpty()) {
			this.edmIncludeAnnotations = new ArrayList<EdmReferenceIncludeAnnotations>();
			for (IncludeAnnotations incl:includes) {
				this.edmIncludeAnnotations.add(new EdmReferenceIncludeAnnotationsImpl(incl));
			}
		}
	}	
}
