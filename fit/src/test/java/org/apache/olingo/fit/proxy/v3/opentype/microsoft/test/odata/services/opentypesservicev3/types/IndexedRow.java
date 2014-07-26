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
package org.apache.olingo.fit.proxy.v3.opentype.microsoft.test.odata.services.opentypesservicev3.types;
//CHECKSTYLE:OFF (Maven checkstyle)
import org.apache.olingo.ext.proxy.api.annotations.Key;
import org.apache.olingo.ext.proxy.api.AbstractOpenType;
import org.apache.olingo.commons.api.edm.constants.EdmContentKind;
import org.apache.olingo.client.api.edm.ConcurrencyMode;
//CHECKSTYLE:ON (Maven checkstyle)


@org.apache.olingo.ext.proxy.api.annotations.Namespace("Microsoft.Test.OData.Services.OpenTypesServiceV3")
@org.apache.olingo.ext.proxy.api.annotations.EntityType(name = "IndexedRow",
        openType = true,
        hasStream = false,
        isAbstract = false,
        baseType = "Microsoft.Test.OData.Services.OpenTypesServiceV3.Row")
public interface IndexedRow 
  extends org.apache.olingo.ext.proxy.api.Annotatable,
  org.apache.olingo.fit.proxy.v3.opentype.microsoft.test.odata.services.opentypesservicev3.types.Row  ,AbstractOpenType {

  @Override
  IndexedRow load();

    

    @Key
    
    @org.apache.olingo.ext.proxy.api.annotations.Property(name = "Id", 
                type = "Edm.Guid", 
                nullable = false,
                defaultValue = "",
                maxLenght = Integer.MAX_VALUE,
                fixedLenght = false,
                precision = 0,
                scale = 0,
                unicode = true,
                collation = "",
                srid = "",
                concurrencyMode = ConcurrencyMode.None,
                fcSourcePath = "",
                fcTargetPath = "",
                fcContentKind = EdmContentKind.text,
                fcNSPrefix = "",
                fcNSURI = "",
                fcKeepInContent = false)
    java.util.UUID getId();

    void setId(java.util.UUID _id);
    



        @Override
        Operations operations();

    interface Operations extends org.apache.olingo.fit.proxy.v3.opentype.microsoft.test.odata.services.opentypesservicev3.types.Row.Operations{
    
        }
    Annotations annotations();

    interface Annotations            extends org.apache.olingo.fit.proxy.v3.opentype.microsoft.test.odata.services.opentypesservicev3.types.Row.Annotations{

        @org.apache.olingo.ext.proxy.api.annotations.AnnotationsForProperty(name = "Id",
                   type = "Edm.Guid")
        org.apache.olingo.ext.proxy.api.Annotatable getIdAnnotations();


    }

}
