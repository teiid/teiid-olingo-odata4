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
package org.apache.olingo.server.core.uri.validator;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.core.edm.provider.EdmProviderImpl;
import org.apache.olingo.server.core.uri.parser.Parser;
import org.apache.olingo.server.core.uri.parser.UriParserException;
import org.apache.olingo.server.core.uri.parser.UriParserSemanticException;
import org.apache.olingo.server.core.uri.parser.UriParserSyntaxException;
import org.apache.olingo.server.tecsvc.provider.EdmTechProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class UriValidatorTest {

  private static final String URI_ALL = "$all";
  private static final String URI_BATCH = "$batch";
  private static final String URI_CROSSJOIN = "$crossjoin(ESAllPrim)";
  private static final String URI_ENTITY_ID = "/$entity";
  private static final String URI_METADATA = "$metadata";
  private static final String URI_SERVICE = "";
  private static final String URI_ENTITY_SET = "/ESAllPrim";
  private static final String URI_ENTITY_SET_COUNT = "/ESAllPrim/$count";
  private static final String URI_ENTITY = "/ESAllPrim(1)";
  private static final String URI_MEDIA_STREAM = "/ESMedia(1)/$value";
  private static final String URI_REFERENCES = "/ESAllPrim/$ref";
  private static final String URI_REFERENCE = "/ESAllPrim(1)/$ref";
  private static final String URI_PROPERTY_COMPLEX = "/ESCompComp(1)/PropertyComp";
  private static final String URI_PROPERTY_COMPLEX_COLLECTION =
      "/ESCompCollComp(1)/PropertyComp/CollPropertyComp";
  private static final String URI_PROPERTY_COMPLEX_COLLECTION_COUNT =
      "/ESCompCollComp(1)/PropertyComp/CollPropertyComp/$count";
  private static final String URI_PROPERTY_PRIMITIVE = "/ESAllPrim(1)/PropertyString";
  private static final String URI_PROPERTY_PRIMITIVE_COLLECTION = "/ESCollAllPrim/CollPropertyString";
  private static final String URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT =
      "/ESCollAllPrim/CollPropertyString/$count";
  private static final String URI_PROPERTY_PRIMITIVE_VALUE = "/ESAllPrim(1)/PropertyString/$value";
  private static final String URI_SINGLETON = "/SI";
  private static final String URI_NAV_ENTITY = "/ESKeyNav/NavPropertyETKeyNavOne";
  private static final String URI_NAV_ENTITY_SET = "/ESKeyNav/NavPropertyETKeyNavMany";

  private static final String QO_FILTER = "$filter='1' eq '1'";
  private static final String QO_FORMAT = "$format=bla/bla";
  private static final String QO_EXPAND = "$expand=*";
  private static final String QO_ID = "$id=Products(0)";
  private static final String QO_COUNT = "$count=true";
  private static final String QO_ORDERBY = "$orderby=true";
//  private static final String QO_SEARCH = "$search='bla'";
  private static final String QO_SELECT = "$select=*";
  private static final String QO_SKIP = "$skip=3";
  private static final String QO_SKIPTOKEN = "$skiptoken=123";
  private static final String QO_LEVELS = "$expand=*($levels=1)";
  private static final String QO_TOP = "$top=1";

  private String[][] urisWithValidSystemQueryOptions = {
      { URI_ALL, QO_FILTER, }, { URI_ALL, QO_FORMAT }, { URI_ALL, QO_EXPAND }, { URI_ALL, QO_COUNT },
      { URI_ALL, QO_ORDERBY }, /* { URI_ALL, QO_SEARCH }, */{ URI_ALL, QO_SELECT }, { URI_ALL, QO_SKIP },
      { URI_ALL, QO_SKIPTOKEN }, { URI_ALL, QO_LEVELS },

      { URI_CROSSJOIN, QO_FILTER, }, { URI_CROSSJOIN, QO_FORMAT },
      { URI_CROSSJOIN, QO_EXPAND }, { URI_CROSSJOIN, QO_COUNT }, { URI_CROSSJOIN, QO_ORDERBY },
      /* { URI_CROSSJOIN, QO_SEARCH }, */{ URI_CROSSJOIN, QO_SELECT }, { URI_CROSSJOIN, QO_SKIP },
      { URI_CROSSJOIN, QO_SKIPTOKEN }, { URI_CROSSJOIN, QO_LEVELS }, { URI_CROSSJOIN, QO_TOP },

      { URI_ENTITY_ID, QO_ID, QO_FORMAT }, { URI_ENTITY_ID, QO_ID, }, { URI_ENTITY_ID, QO_ID, QO_EXPAND },
      { URI_ENTITY_ID, QO_ID, QO_SELECT }, { URI_ENTITY_ID, QO_ID, QO_LEVELS },

      { URI_METADATA, QO_FORMAT },

      { URI_SERVICE, QO_FORMAT },

      { URI_ENTITY_SET, QO_FILTER, }, { URI_ENTITY_SET, QO_FORMAT }, { URI_ENTITY_SET, QO_EXPAND },
      { URI_ENTITY_SET, QO_COUNT }, { URI_ENTITY_SET, QO_ORDERBY }, /* { URI_ENTITY_SET, QO_SEARCH }, */
      { URI_ENTITY_SET, QO_SELECT },
      { URI_ENTITY_SET, QO_SKIP }, { URI_ENTITY_SET, QO_SKIPTOKEN }, { URI_ENTITY_SET, QO_LEVELS },
      { URI_ENTITY_SET, QO_TOP },

      { URI_ENTITY_SET_COUNT, QO_FILTER }, /* { URI_ENTITY_SET_COUNT, QO_SEARCH }, */

      { URI_ENTITY, QO_FORMAT }, { URI_ENTITY, QO_EXPAND }, { URI_ENTITY, QO_SELECT }, { URI_ENTITY, QO_LEVELS },

      { URI_MEDIA_STREAM, QO_FORMAT },

      { URI_REFERENCES, QO_FILTER }, { URI_REFERENCES, QO_FORMAT }, { URI_REFERENCES, QO_ORDERBY },
      /* { URI_REFERENCES, QO_SEARCH }, */{ URI_REFERENCES, QO_SKIP }, { URI_REFERENCES, QO_SKIPTOKEN },
      { URI_REFERENCES, QO_TOP },

      { URI_REFERENCE, QO_FORMAT },

      { URI_PROPERTY_COMPLEX, QO_FORMAT }, { URI_PROPERTY_COMPLEX, QO_SELECT }, { URI_PROPERTY_COMPLEX, QO_EXPAND },
      { URI_PROPERTY_COMPLEX, QO_LEVELS },

      { URI_PROPERTY_COMPLEX_COLLECTION, QO_FILTER }, { URI_PROPERTY_COMPLEX_COLLECTION, QO_FORMAT },
      { URI_PROPERTY_COMPLEX_COLLECTION, QO_EXPAND }, { URI_PROPERTY_COMPLEX_COLLECTION, QO_COUNT },
      { URI_PROPERTY_COMPLEX_COLLECTION, QO_SKIP }, { URI_PROPERTY_COMPLEX_COLLECTION, QO_SKIPTOKEN },
      { URI_PROPERTY_COMPLEX_COLLECTION, QO_LEVELS }, { URI_PROPERTY_COMPLEX_COLLECTION, QO_TOP },
      { URI_PROPERTY_COMPLEX_COLLECTION, QO_ORDERBY },

      { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_FILTER }, /* { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_SEARCH }, */

      { URI_PROPERTY_PRIMITIVE, QO_FORMAT },

      { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_FILTER }, { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_FORMAT },
      { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_ORDERBY }, { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_SKIP },
      { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_SKIPTOKEN }, { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_TOP },

      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_FILTER },
      /* { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_SEARCH }, */

      { URI_PROPERTY_PRIMITIVE_VALUE, QO_FORMAT },

      { URI_SINGLETON, QO_FORMAT }, { URI_SINGLETON, QO_EXPAND }, { URI_SINGLETON, QO_SELECT },
      { URI_SINGLETON, QO_LEVELS },

      { URI_NAV_ENTITY, QO_FORMAT }, { URI_NAV_ENTITY, QO_EXPAND }, { URI_NAV_ENTITY, QO_SELECT },
      { URI_NAV_ENTITY, QO_LEVELS },

      { URI_NAV_ENTITY_SET, QO_FILTER, }, { URI_NAV_ENTITY_SET, QO_FORMAT }, { URI_NAV_ENTITY_SET, QO_EXPAND },
      { URI_NAV_ENTITY_SET, QO_COUNT }, { URI_NAV_ENTITY_SET, QO_ORDERBY },
      /* { URI_NAV_ENTITY_SET, QO_SEARCH }, */{ URI_NAV_ENTITY_SET, QO_SELECT }, { URI_NAV_ENTITY_SET, QO_SKIP },
      { URI_NAV_ENTITY_SET, QO_SKIPTOKEN }, { URI_NAV_ENTITY_SET, QO_LEVELS }, { URI_NAV_ENTITY_SET, QO_TOP },

      { "FINRTInt16()" },
      { "FICRTETKeyNav()" },
      { "FICRTESTwoKeyNavParam(ParameterInt16=1)" },
      { "FICRTCollString()" },
      { "FICRTCTTwoPrim()" },
      { "FICRTCollCTTwoPrim()" },
      { "FICRTETMedia()" },

      { "ESTwoKeyNav/olingo.odata.test1.BAESTwoKeyNavRTESTwoKeyNav" },
      { "ESAllPrim/olingo.odata.test1.BAESAllPrimRTETAllPrim" },
      { "AIRTPrimCollParam" },
      { "AIRTETParam" },
      { "AIRTPrimParam" },

  };

  private String[][] urisWithNonValidSystemQueryOptions = {
      { URI_ALL, QO_ID, }, { URI_ALL, QO_TOP },

      { URI_BATCH, QO_FILTER, }, { URI_BATCH, QO_FORMAT }, { URI_BATCH, QO_ID, }, { URI_BATCH, QO_EXPAND },
      { URI_BATCH, QO_COUNT }, { URI_BATCH, QO_ORDERBY }, /* { URI_BATCH, QO_SEARCH }, */{ URI_BATCH, QO_SELECT },
      { URI_BATCH, QO_SKIP }, { URI_BATCH, QO_SKIPTOKEN }, { URI_BATCH, QO_LEVELS }, { URI_BATCH, QO_TOP },

      { URI_CROSSJOIN, QO_ID, },

      { URI_ENTITY_ID, QO_ID, QO_FILTER, },
      { URI_ENTITY_ID, QO_ID, QO_COUNT }, { URI_ENTITY_ID, QO_ORDERBY }, /* { URI_ENTITY_ID, QO_SEARCH }, */

      { URI_ENTITY_ID, QO_ID, QO_SKIP }, { URI_ENTITY_ID, QO_ID, QO_SKIPTOKEN }, { URI_ENTITY_ID, QO_ID, QO_TOP },

      { URI_METADATA, QO_FILTER, }, { URI_METADATA, QO_ID, }, { URI_METADATA, QO_EXPAND },
      { URI_METADATA, QO_COUNT }, { URI_METADATA, QO_ORDERBY }, /* { URI_METADATA, QO_SEARCH }, */
      { URI_METADATA, QO_SELECT }, { URI_METADATA, QO_SKIP }, { URI_METADATA, QO_SKIPTOKEN },
      { URI_METADATA, QO_LEVELS }, { URI_METADATA, QO_TOP },

      { URI_SERVICE, QO_FILTER }, { URI_SERVICE, QO_ID }, { URI_SERVICE, QO_EXPAND }, { URI_SERVICE, QO_COUNT },
      { URI_SERVICE, QO_ORDERBY }, /* { URI_SERVICE, QO_SEARCH }, */{ URI_SERVICE, QO_SELECT },
      { URI_SERVICE, QO_SKIP }, { URI_SERVICE, QO_SKIPTOKEN }, { URI_SERVICE, QO_LEVELS }, { URI_SERVICE, QO_TOP },

      { URI_ENTITY_SET, QO_ID },

      { URI_ENTITY_SET_COUNT, QO_FORMAT }, { URI_ENTITY_SET_COUNT, QO_ID },
      { URI_ENTITY_SET_COUNT, QO_EXPAND }, { URI_ENTITY_SET_COUNT, QO_COUNT },
      { URI_ENTITY_SET_COUNT, QO_ORDERBY },
      { URI_ENTITY_SET_COUNT, QO_SELECT }, { URI_ENTITY_SET_COUNT, QO_SKIP }, { URI_ENTITY_SET_COUNT, QO_SKIPTOKEN },
      { URI_ENTITY_SET_COUNT, QO_LEVELS }, { URI_ENTITY_SET_COUNT, QO_TOP },

      { URI_ENTITY, QO_FILTER }, { URI_ENTITY, QO_ID }, { URI_ENTITY, QO_COUNT }, /* { URI_ENTITY, QO_ORDERBY }, */
      /* { URI_ENTITY, QO_SEARCH }, */{ URI_ENTITY, QO_SKIP }, { URI_ENTITY, QO_SKIPTOKEN }, { URI_ENTITY, QO_TOP },

      { URI_MEDIA_STREAM, QO_FILTER }, { URI_MEDIA_STREAM, QO_ID, }, { URI_MEDIA_STREAM, QO_EXPAND },
      { URI_MEDIA_STREAM, QO_COUNT }, { URI_MEDIA_STREAM, QO_ORDERBY }, /* { URI_MEDIA_STREAM, QO_SEARCH }, */
      { URI_MEDIA_STREAM, QO_SELECT }, { URI_MEDIA_STREAM, QO_SKIP }, { URI_MEDIA_STREAM, QO_SKIPTOKEN },
      { URI_MEDIA_STREAM, QO_LEVELS }, { URI_MEDIA_STREAM, QO_TOP },

      { URI_REFERENCES, QO_ID, }, { URI_REFERENCES, QO_EXPAND }, { URI_REFERENCES, QO_COUNT },
      { URI_REFERENCES, QO_SELECT }, { URI_REFERENCES, QO_LEVELS },

      { URI_REFERENCE, QO_FILTER }, { URI_REFERENCE, QO_ID, }, { URI_REFERENCE, QO_EXPAND },
      { URI_REFERENCE, QO_COUNT }, { URI_REFERENCE, QO_ORDERBY }, /* { URI_REFERENCE, QO_SEARCH }, */
      { URI_REFERENCE, QO_SELECT }, { URI_REFERENCE, QO_SKIP }, { URI_REFERENCE, QO_SKIPTOKEN },
      { URI_REFERENCE, QO_LEVELS }, { URI_REFERENCE, QO_TOP },

      { URI_PROPERTY_COMPLEX, QO_FILTER }, { URI_PROPERTY_COMPLEX, QO_ID, }, { URI_PROPERTY_COMPLEX, QO_COUNT },
      { URI_PROPERTY_COMPLEX, QO_ORDERBY }, /* { URI_PROPERTY_COMPLEX, QO_SEARCH }, */
      { URI_PROPERTY_COMPLEX, QO_SKIP }, { URI_PROPERTY_COMPLEX, QO_SKIPTOKEN }, { URI_PROPERTY_COMPLEX, QO_TOP },

      { URI_PROPERTY_COMPLEX_COLLECTION, QO_ID, },
      /* { URI_PROPERTY_COMPLEX_COLLECTION, QO_SEARCH }, */{ URI_PROPERTY_COMPLEX_COLLECTION, QO_SELECT },

      { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_FORMAT },
      { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_ID, }, { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_EXPAND },
      { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_COUNT }, { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_ORDERBY },
      { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_SELECT },
      { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_SKIP }, { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_SKIPTOKEN },
      { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_LEVELS }, { URI_PROPERTY_COMPLEX_COLLECTION_COUNT, QO_TOP },

      { URI_PROPERTY_PRIMITIVE, QO_FILTER }, { URI_PROPERTY_PRIMITIVE, QO_ID, }, { URI_PROPERTY_PRIMITIVE, QO_EXPAND },
      { URI_PROPERTY_PRIMITIVE, QO_COUNT }, { URI_PROPERTY_PRIMITIVE, QO_ORDERBY },
      /* { URI_PROPERTY_PRIMITIVE, QO_SEARCH }, */{ URI_PROPERTY_PRIMITIVE, QO_SELECT },
      { URI_PROPERTY_PRIMITIVE, QO_SKIP }, { URI_PROPERTY_PRIMITIVE, QO_SKIPTOKEN },
      { URI_PROPERTY_PRIMITIVE, QO_LEVELS }, { URI_PROPERTY_PRIMITIVE, QO_TOP },

      { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_ID, }, { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_EXPAND },
      { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_COUNT }, /* { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_SEARCH }, */
      { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_SELECT }, { URI_PROPERTY_PRIMITIVE_COLLECTION, QO_LEVELS },

      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_FORMAT },
      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_ID, }, { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_EXPAND },
      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_COUNT },
      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_ORDERBY },
      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_SELECT }, { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_SKIP },
      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_SKIPTOKEN },
      { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_LEVELS }, { URI_PROPERTY_PRIMITIVE_COLLECTION_COUNT, QO_TOP },

      { URI_PROPERTY_PRIMITIVE_VALUE, QO_FILTER }, { URI_PROPERTY_PRIMITIVE_VALUE, QO_ID, },
      { URI_PROPERTY_PRIMITIVE_VALUE, QO_EXPAND }, { URI_PROPERTY_PRIMITIVE_VALUE, QO_COUNT },
      { URI_PROPERTY_PRIMITIVE_VALUE, QO_ORDERBY },/* { URI_PROPERTY_PRIMITIVE_VALUE, QO_SEARCH }, */
      { URI_PROPERTY_PRIMITIVE_VALUE, QO_SELECT }, { URI_PROPERTY_PRIMITIVE_VALUE, QO_SKIP },
      { URI_PROPERTY_PRIMITIVE_VALUE, QO_SKIPTOKEN }, { URI_PROPERTY_PRIMITIVE_VALUE, QO_LEVELS },
      { URI_PROPERTY_PRIMITIVE_VALUE, QO_TOP },

      { URI_SINGLETON, QO_FILTER }, { URI_SINGLETON, QO_ID }, { URI_SINGLETON, QO_COUNT },
      { URI_SINGLETON, QO_ORDERBY }, /* { URI_SINGLETON, QO_SEARCH }, */{ URI_SINGLETON, QO_SKIP },
      { URI_SINGLETON, QO_SKIPTOKEN }, { URI_SINGLETON, QO_TOP },

      { URI_NAV_ENTITY, QO_FILTER }, { URI_NAV_ENTITY, QO_ID }, { URI_NAV_ENTITY, QO_COUNT },
      { URI_NAV_ENTITY, QO_ORDERBY }, /* { URI_NAV_ENTITY, QO_SEARCH }, */{ URI_NAV_ENTITY, QO_SKIP },
      { URI_NAV_ENTITY, QO_SKIPTOKEN }, { URI_SINGLETON, QO_TOP },

      { URI_NAV_ENTITY_SET, QO_ID },

  };

  private Parser parser;
  private Edm edm;

  @Before
  public void before() {
    parser = new Parser();
    edm = new EdmProviderImpl(new EdmTechProvider());
  }

  @Test
  public void validateSelect() throws Exception {
    parseAndValidate("/ESAllPrim(1)", "$select=PropertyString", HttpMethod.GET);
  }

  @Test
  public void validateForHttpMethods() throws Exception {
    String uri = URI_ENTITY;
    parseAndValidate(uri, null, HttpMethod.GET);
    parseAndValidate(uri, null, HttpMethod.POST);
    parseAndValidate(uri, null, HttpMethod.PUT);
    parseAndValidate(uri, null, HttpMethod.DELETE);
    parseAndValidate(uri, null, HttpMethod.PATCH);
    parseAndValidate(uri, null, HttpMethod.MERGE);
  }

  @Test
  public void validateOrderBy() throws Exception {
    parseAndValidate("/ESAllPrim", "$orderby=PropertyString", HttpMethod.GET);
  }

  @Test(expected = UriParserSemanticException.class)
  public void validateOrderByInvalid() throws Exception {
    parseAndValidate("/ESAllPrim(1)", "$orderby=XXXX", HttpMethod.GET);
  }

  @Test(expected = UriParserSyntaxException.class)
  public void validateCountInvalid() throws Exception {
    parseAndValidate("ESAllPrim", "$count=foo", HttpMethod.GET);
  }

  @Test(expected = UriParserSyntaxException.class)
  public void validateTopInvalid() throws Exception {
    parseAndValidate("ESAllPrim", "$top=foo", HttpMethod.GET);
  }

  @Test(expected = UriParserSyntaxException.class)
  public void validateSkipInvalid() throws Exception {
    parseAndValidate("ESAllPrim", "$skip=foo", HttpMethod.GET);
  }

  @Test(expected = UriParserSyntaxException.class)
  public void validateDoubleSystemOptions() throws Exception {
    parseAndValidate("ESAllPrim", "$skip=1&$skip=2", HttpMethod.GET);
  }

  @Test(expected = UriValidationException.class)
  public void validateKeyPredicatesWrongKey() throws Exception {
    parseAndValidate("ESTwoKeyNav(xxx=1, yyy='abc')", null, HttpMethod.GET);
  }

  @Test
  public void validateKeyPredicates() throws Exception {
    parseAndValidate("ESTwoKeyNav(PropertyInt16=1, PropertyString='abc')", null, HttpMethod.GET);
  }

  @Test(expected = UriValidationException.class)
  public void validateKeyPredicatesWrongValueType() throws Exception {
    parseAndValidate("ESTwoKeyNav(PropertyInt16='abc', PropertyString=1)", null, HttpMethod.GET);
  }

  @Test(expected = UriValidationException.class)
  public void validateKeyPredicatesWrongValueTypeForValidateMethod() throws Exception {
    parseAndValidate("ESTwoKeyNav(PropertyInt16='abc', PropertyString='abc')", null, HttpMethod.GET);
  }
  
  @Test
  public void checkValidSystemQueryOption() throws Exception {
    List<String[]> uris = constructUri(urisWithValidSystemQueryOptions);

    for (String[] uri : uris) {
      try {
        parseAndValidate(uri[0], uri[1], HttpMethod.GET);
      } catch (final UriParserException e) {
        fail("Failed for uri: " + uri);
      } catch (final UriValidationException e) {
        fail("Failed for uri: " + uri);
      }
    }
  }

  @Test
  public void checkNonValidSystemQueryOption() throws Exception {
    List<String[]> uris = constructUri(urisWithNonValidSystemQueryOptions);

    for (String[] uri : uris) {
      try {
        parseAndValidate(uri[0], uri[1], HttpMethod.GET);
        fail("Validation Exception not thrown: " + uri);
      } catch (UriParserSemanticException e) {
      } catch (UriValidationException e) {
      }
    }
  }

  private List<String[]> constructUri(final String[][] uriParameterMatrix) {
    List<String[]> uris = new ArrayList<String[]>();
    for (String[] uriParameter : uriParameterMatrix) {
      String path = uriParameter[0];
      String query = "";
      for (int i = 1; i < uriParameter.length; i++) {
        query += uriParameter[i];
        if (i < (uriParameter.length - 1)) {
          query += "&";
        }
      }
      uris.add(new String[] { path, query });
    }
    return uris;
  }

  private void parseAndValidate(final String path, final String query, final HttpMethod method)
      throws UriParserException, UriValidationException {
    UriInfo uriInfo = parser.parseUri(path.trim(), query, null, edm);
    new UriValidator().validate(uriInfo, method);
  }

}
