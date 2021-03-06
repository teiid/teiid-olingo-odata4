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
package org.apache.olingo.server.api.processor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.constants.ODataServiceVersion;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.format.ODataFormat;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ODataServerError;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriInfo;

/**
 * <p>Processor implementation for handling default cases:
 * <ul><li>request for the metadata document</li>
 * <li>request for the service document</li>
 * <li>error handling</li></ul></p>
 * <p>This implementation is registered in the ODataHandler by default.
 * The default can be replaced by re-registering a custom implementation.</p>
 */
public class DefaultProcessor implements MetadataProcessor, ServiceDocumentProcessor, ExceptionProcessor {

  private OData odata;
  private Edm edm;

  @Override
  public void init(final OData odata, final Edm edm) {
    this.odata = odata;
    this.edm = edm;
  }

  @Override
  public void readServiceDocument(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestedContentType) throws ODataApplicationException, SerializerException {
    try {
      ODataSerializer serializer = odata.createSerializer(ODataFormat.fromContentType(requestedContentType));
      response.setContent(serializer.serviceDocument(edm, request.getRawBaseUri()));
      response.setStatusCode(HttpStatusCode.OK.getStatusCode());
      response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toContentTypeString());
    } catch (final SerializerException e) {
      response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
    }
  }

  @Override
  public void readMetadata(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestedContentType) throws ODataApplicationException, SerializerException {
    try {
      ODataSerializer serializer = odata.createSerializer(ODataFormat.fromContentType(requestedContentType));
      response.setContent(serializer.metadataDocument(edm));
      response.setStatusCode(HttpStatusCode.OK.getStatusCode());
      response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toContentTypeString());
    } catch (final SerializerException e) {
      response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
    }
  }

  @Override
  public void processException(ODataRequest request, ODataResponse response, ODataServerError serverError,
      ContentType requestedContentType) {
    try {
      if(ContentType.APPLICATION_XML.equals(requestedContentType)){
        requestedContentType = ODataFormat.JSON.getContentType(ODataServiceVersion.V40);
      }
      ODataSerializer serializer = odata.createSerializer(ODataFormat.fromContentType(requestedContentType));
      InputStream responseEntity = serializer.error(serverError);
      response.setStatusCode(serverError.getStatusCode());
      response.setContent(responseEntity);
      response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toContentTypeString());
    } catch (Exception e) {
      // This should never happen but to be sure we have this catch here to prevent sending a stacktrace to a client.
      String responseContent =
          "{\"error\":{\"code\":null,\"message\":\"An unexpected exception occurred during " +
              "error processing with message: " + e.getMessage() + "\"}}";
      response.setContent(new ByteArrayInputStream(responseContent.getBytes()));
      response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
      response.setHeader(HttpHeader.CONTENT_TYPE, ContentType.APPLICATION_JSON.toContentTypeString());
    }
  }
}
