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
package org.apache.olingo.server.api.edm.provider;

import java.net.URI;
import java.util.List;

import org.apache.olingo.commons.api.ODataException;

public class Reference {
  private List<Include> includes;
	private URI uri;
	private List<IncludeAnnotations> includeAnnotations;
	
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public List<Include> getIncludes(){
	  return this.includes;
  }
  
  public void setIncludes(List<Include> includes) throws ODataException {
		if (includes != null && !includes.isEmpty()) {
			for (Include inc : includes) {
				if (inc.getAlias() != null
						&& (inc.getAlias().equalsIgnoreCase("Edm")
								|| inc.getAlias().equalsIgnoreCase("odata")
								|| inc.getAlias().equalsIgnoreCase("System")
								|| inc.getAlias().equalsIgnoreCase("Transient"))) {
					throw new ODataException("Include alias MUST not use "
							+ "reserved words (Edm, odata, System, Transient)");
				}
			}
		}
		this.includes = includes;
  }
  
  public List<IncludeAnnotations> getIncludeAnnotations() {
		return includeAnnotations;
	}

	public void setIncludeAnnotations(List<IncludeAnnotations> includeAnnotations) {
		this.includeAnnotations = includeAnnotations;
	}
	
  public static class Include {
    private String namespace;
    private String alias;

    public Include (String ns) {
    	this.namespace = ns;
    }
    
    public String getNamespace() {
      return namespace;
    }

    public String getAlias() {
      return alias;
    }

    public Include setAlias(final String alias) {
      this.alias = alias;
      return this;
    }
  } 
  
  public static class IncludeAnnotations {
		private String termNamespace;
    private String qualifier;
    private String targetNamespace;

    public IncludeAnnotations (String termNS) {
    	this.termNamespace = termNS;
    }
    
    public String getQualifier() {
			return qualifier;
		}

		public IncludeAnnotations setQualifier(String qualifier) {
			this.qualifier = qualifier;
			return this;
		}

		public String getTargetNamespace() {
			return targetNamespace;
		}

		public IncludeAnnotations setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
			return this;
		}

		public String getTermNamespace() {
			return termNamespace;
		}    
  }   
}
