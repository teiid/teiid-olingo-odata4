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
package org.apache.olingo.commons.api.edm;

import java.net.URI;
import java.util.List;

/**
 * Represents a edmx:Reference from CSDL document
 */
public interface EdmReference {
	
	/**
	 * Get URI for the Reference
	 * @return
	 */
	URI getUri();
	
	/**
	 * edmx:Include elements that specify the schemas to include from the target document
	 * @return list of {@link EdmReferenceInclude} in reference or null if none specified
	 */
	List<EdmReferenceInclude> getIncludes();
	
	/**
	 * edmx:IncludeAnnotations elements that specify the annotations to include from the target document.
	 * @return List of {@link EdmReferenceIncludeAnnotations} or null if none specified 
	 */
	List<EdmReferenceIncludeAnnotations> getIncludeAnnotations();
}
