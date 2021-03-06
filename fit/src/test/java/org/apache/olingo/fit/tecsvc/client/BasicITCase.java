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
package org.apache.olingo.fit.tecsvc.client;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.olingo.client.api.CommonODataClient;
import org.apache.olingo.client.api.communication.ODataClientErrorException;
import org.apache.olingo.client.api.communication.request.retrieve.EdmMetadataRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntityRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataPropertyRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataServiceDocumentRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.v4.ODataClient;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.domain.ODataError;
import org.apache.olingo.commons.api.domain.ODataServiceDocument;
import org.apache.olingo.commons.api.domain.v4.ODataAnnotation;
import org.apache.olingo.commons.api.domain.v4.ODataEntity;
import org.apache.olingo.commons.api.domain.v4.ODataEntitySet;
import org.apache.olingo.commons.api.domain.v4.ODataProperty;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.format.ODataFormat;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.fit.AbstractBaseTestITCase;
import org.apache.olingo.fit.tecsvc.TecSvcConst;
import org.junit.Test;

public class BasicITCase extends AbstractBaseTestITCase {

  private static final String SERVICE_URI = TecSvcConst.BASE_URI;

  @Test
  public void readServiceDocument() {
    ODataServiceDocumentRequest request = getClient().getRetrieveRequestFactory()
        .getServiceDocumentRequest(SERVICE_URI);
    assertNotNull(request);

    ODataRetrieveResponse<ODataServiceDocument> response = request.execute();
    assertEquals(HttpStatusCode.OK.getStatusCode(), response.getStatusCode());

    ODataServiceDocument serviceDocument = response.getBody();
    assertNotNull(serviceDocument);

    assertThat(serviceDocument.getEntitySetNames(), hasItem("ESAllPrim"));
    assertThat(serviceDocument.getFunctionImportNames(), hasItem("FICRTCollCTTwoPrim"));
    assertThat(serviceDocument.getSingletonNames(), hasItem("SIMedia"));
  }

  @Test
  public void readMetadata() {
    EdmMetadataRequest request = getClient().getRetrieveRequestFactory().getMetadataRequest(SERVICE_URI);
    assertNotNull(request);

    ODataRetrieveResponse<Edm> response = request.execute();
    assertEquals(HttpStatusCode.OK.getStatusCode(), response.getStatusCode());

    Edm edm = response.getBody();

    assertNotNull(edm);
    assertEquals("olingo.odata.test1", edm.getSchema("olingo.odata.test1").getNamespace());
    assertEquals("Namespace1_Alias", edm.getSchema("olingo.odata.test1").getAlias());
    assertNotNull(edm.getTerm(new FullQualifiedName("Core.Description")));
    assertEquals(2, edm.getSchemas().size());
  }

  @Test
  public void readEntitySet() {
    final ODataEntitySetRequest<ODataEntitySet> request = getClient().getRetrieveRequestFactory()
        .getEntitySetRequest(getClient().newURIBuilder(SERVICE_URI)
            .appendEntitySetSegment("ESMixPrimCollComp").build());
    assertNotNull(request);

    final ODataRetrieveResponse<ODataEntitySet> response = request.execute();
    assertEquals(HttpStatusCode.OK.getStatusCode(), response.getStatusCode());
    assertThat(response.getContentType(), containsString(ContentType.APPLICATION_JSON.toContentTypeString()));

    final ODataEntitySet entitySet = response.getBody();
    assertNotNull(entitySet);

    assertNull(entitySet.getCount());
    assertNull(entitySet.getNext());
    assertEquals(Collections.<ODataAnnotation> emptyList(), entitySet.getAnnotations());
    assertNull(entitySet.getDeltaLink());

    final List<ODataEntity> entities = entitySet.getEntities();
    assertNotNull(entities);
    assertEquals(3, entities.size());
    final ODataEntity entity = entities.get(2);
    assertNotNull(entity);
    final ODataProperty property = entity.getProperty("PropertyInt16");
    assertNotNull(property);
    assertNotNull(property.getPrimitiveValue());
    assertEquals(0, property.getPrimitiveValue().toValue());
  }

  @Test
  public void readException() throws Exception {
    final ODataEntityRequest<ODataEntity> request = getClient().getRetrieveRequestFactory()
        .getEntityRequest(getClient().newURIBuilder(SERVICE_URI)
            .appendEntitySetSegment("ESMixPrimCollComp").appendKeySegment("42").build());
    assertNotNull(request);

    try {
      request.execute();
      fail("Expected Exception not thrown!");
    } catch (final ODataClientErrorException e) {
      assertEquals(HttpStatusCode.BAD_REQUEST.getStatusCode(), e.getStatusLine().getStatusCode());
      final ODataError error = e.getODataError();
      assertThat(error.getMessage(), containsString("key property"));
    }
  }

  @Test
  public void readEntityRawResult() throws IOException {
    final ODataEntityRequest<ODataEntity> request = getClient().getRetrieveRequestFactory()
        .getEntityRequest(getClient().newURIBuilder(SERVICE_URI)
            .appendEntitySetSegment("ESCollAllPrim").appendKeySegment(1).build());
    assertNotNull(request);

    final ODataRetrieveResponse<ODataEntity> response = request.execute();
    assertEquals(HttpStatusCode.OK.getStatusCode(), response.getStatusCode());
    assertThat(response.getContentType(), containsString(ContentType.APPLICATION_JSON.toContentTypeString()));

    //
    final String expectedResult = "{"
        + "\"@odata.context\":\"$metadata#ESCollAllPrim/$entity\","
        + "\"PropertyInt16\":1,"
        + "\"CollPropertyString\":"
        + "[\"Employee1@company.example\",\"Employee2@company.example\",\"Employee3@company.example\"],"
        + "\"CollPropertyBoolean\":[true,false,true],"
        + "\"CollPropertyByte\":[50,200,249],"
        + "\"CollPropertySByte\":[-120,120,126],"
        + "\"CollPropertyInt16\":[1000,2000,30112],"
        + "\"CollPropertyInt32\":[23232323,11223355,10000001],"
        + "\"CollPropertyInt64\":[929292929292,333333333333,444444444444],"
        + "\"CollPropertySingle\":[1790.0,26600.0,3210.0],"
        + "\"CollPropertyDouble\":[-17900.0,-2.78E7,3210.0],"
        + "\"CollPropertyDecimal\":[12,-2,1234],"
        + "\"CollPropertyBinary\":[\"q83v\",\"ASNF\",\"VGeJ\"],"
        + "\"CollPropertyDate\":[\"1958-12-03\",\"1999-08-05\",\"2013-06-25\"],"
        + "\"CollPropertyDateTimeOffset\":[\"2015-08-12T03:08:34Z\",\"1970-03-28T12:11:10Z\","
        + "\"1948-02-17T09:09:09Z\"],"
        + "\"CollPropertyDuration\":[\"PT13S\",\"PT5H28M0S\",\"PT1H0S\"],"
        + "\"CollPropertyGuid\":[\"ffffff67-89ab-cdef-0123-456789aaaaaa\",\"eeeeee67-89ab-cdef-0123-456789bbbbbb\","
        + "\"cccccc67-89ab-cdef-0123-456789cccccc\"],"
        + "\"CollPropertyTimeOfDay\":[\"04:14:13\",\"23:59:59\",\"01:12:33\"]"
        + "}";
    assertEquals(expectedResult, IOUtils.toString(response.getRawResponse(), "UTF-8"));
  }
  
  @Test
  public void readSimpleProperty() throws Exception {
    ODataPropertyRequest<ODataProperty> request = getClient().getRetrieveRequestFactory()
        .getPropertyRequest(getClient().newURIBuilder(SERVICE_URI)            
            .appendEntitySetSegment("ESTwoPrim")
            .appendKeySegment(32766)
            .appendPropertySegment("PropertyString")
            .build());
    
    assertNotNull(request);

    ODataRetrieveResponse<ODataProperty> response = request.execute();
    assertEquals(HttpStatusCode.OK.getStatusCode(), response.getStatusCode());
    assertThat(response.getContentType(), containsString(ContentType.APPLICATION_JSON.toContentTypeString()));

    final ODataProperty property = response.getBody();
    assertNotNull(property);
    assertNotNull(property.getPrimitiveValue());
    assertEquals("Test String1", property.getPrimitiveValue().toValue());
  }
  
  @Test
  public void readSimplePropertyContextURL() throws Exception {
    ODataPropertyRequest<ODataProperty> request = getClient().getRetrieveRequestFactory()
        .getPropertyRequest(getClient().newURIBuilder(SERVICE_URI)            
            .appendEntitySetSegment("ESTwoPrim")
            .appendKeySegment(32766)
            .appendPropertySegment("PropertyString")
            .build());    
    ODataRetrieveResponse<ODataProperty> response = request.execute();
    String expectedResult = 
        "{\"@odata.context\":\"$metadata#ESTwoPrim(32766)/PropertyString\"," +
        "\"value\":\"Test String1\"}";
    assertEquals(expectedResult, IOUtils.toString(response.getRawResponse(), "UTF-8"));    
  }  
  
  @Test
  public void readComplexProperty() throws Exception {
    ODataPropertyRequest<ODataProperty> request = getClient().getRetrieveRequestFactory()
        .getPropertyRequest(getClient().newURIBuilder(SERVICE_URI)            
            .appendEntitySetSegment("ESMixPrimCollComp")
            .appendKeySegment(7)
            .appendPropertySegment("PropertyComp")
            .build());    
    ODataRetrieveResponse<ODataProperty> response = request.execute();
    assertEquals(HttpStatusCode.OK.getStatusCode(), response.getStatusCode());
    assertThat(response.getContentType(), containsString(ContentType.APPLICATION_JSON.toContentTypeString()));

    final ODataProperty property = response.getBody();
    assertNotNull(property);
    assertNotNull(property.getComplexValue());
    assertEquals("TEST B", property.getComplexValue().get("PropertyString").getPrimitiveValue().toValue());   
  }  
  
  @Test
  public void readComplexPropertyContextURL() throws Exception {
    ODataPropertyRequest<ODataProperty> request = getClient().getRetrieveRequestFactory()
        .getPropertyRequest(getClient().newURIBuilder(SERVICE_URI)            
            .appendEntitySetSegment("ESMixPrimCollComp")
            .appendKeySegment(7)
            .appendPropertySegment("PropertyComp")
            .build());    
    ODataRetrieveResponse<ODataProperty> response = request.execute();
    String expectedResult = 
        "{\"@odata.context\":\"$metadata#ESMixPrimCollComp(7)/PropertyComp\"," +
        "\"PropertyInt16\":222,\"PropertyString\":\"TEST B\"}";
    assertEquals(expectedResult, IOUtils.toString(response.getRawResponse(), "UTF-8"));    
  }  
  
  @Test(expected=ODataClientErrorException.class)
  public void readUnknownProperty() throws Exception {
    ODataPropertyRequest<ODataProperty> request = getClient().getRetrieveRequestFactory()
        .getPropertyRequest(getClient().newURIBuilder(SERVICE_URI)            
            .appendEntitySetSegment("ESTwoPrim")
            .appendKeySegment(32766)
            .appendPropertySegment("Unknown")
            .build());    
    ODataRetrieveResponse<ODataProperty> response = request.execute();
    assertEquals(HttpStatusCode.NOT_FOUND.getStatusCode(), response.getStatusCode());
  }   
  
  @Test
  public void readNoContentProperty() throws Exception {
    ODataPropertyRequest<ODataProperty> request = getClient().getRetrieveRequestFactory()
        .getPropertyRequest(getClient().newURIBuilder(SERVICE_URI)            
            .appendEntitySetSegment("ESTwoPrim")
            .appendKeySegment(-32766)
            .appendPropertySegment("PropertyString")
            .build());    
    ODataRetrieveResponse<ODataProperty> response = request.execute();
    assertEquals(HttpStatusCode.NO_CONTENT.getStatusCode(), response.getStatusCode());
  }   
  
  @Test
  public void readPropertyValue() throws Exception {
    ODataPropertyRequest<ODataProperty> request = getClient().getRetrieveRequestFactory()
        .getPropertyRequest(getClient().newURIBuilder(SERVICE_URI)            
            .appendEntitySetSegment("ESTwoPrim")
            .appendKeySegment(32766)
            .appendPropertySegment("PropertyString")
            .appendValueSegment()
            .build());    
    ODataRetrieveResponse<ODataProperty> response = request.execute();
    assertEquals("Test String1", IOUtils.toString(response.getRawResponse(), "UTF-8"));
  }   

  @Override
  protected CommonODataClient<?> getClient() {
    ODataClient odata = ODataClientFactory.getV4();
    odata.getConfiguration().setDefaultPubFormat(ODataFormat.JSON);
    return odata;
  }
}
