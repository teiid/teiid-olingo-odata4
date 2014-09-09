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

import org.apache.olingo.commons.api.ODataException;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import java.util.List;

public abstract class EdmProvider {

  /**
   * This method should return an {@link EnumType} or <b>null</b> if nothing is found
   * 
   * @param enumTypeName
   * @return {@link EnumType} for given name
   * @throws ODataException
   */
  public EnumType getEnumType(final FullQualifiedName enumTypeName) throws ODataException {
    return null;
  }

  /**
   * This method should return an {@link TypeDefinition} or <b>null</b> if nothing is found
   * 
   * @param typeDefinitionName
   * @return {@link TypeDefinition} for given name
   * @throws ODataException
   */
  public TypeDefinition getTypeDefinition(final FullQualifiedName typeDefinitionName) throws ODataException {
    return null;
  }

  /**
   * This method should return an {@link EntityType} or <b>null</b> if nothing is found
   * 
   * @param entityTypeName
   * @return {@link EntityType} for the given name
   * @throws ODataException
   */
  public EntityType getEntityType(final FullQualifiedName entityTypeName) throws ODataException {
    return null;
  }

  /**
   * This method should return a {@link ComplexType} or <b>null</b> if nothing is found.
   * 
   * @param complexTypeName
   * @return {@link ComplexType} for the given name
   * @throws ODataException
   */
  public ComplexType getComplexType(final FullQualifiedName complexTypeName) throws ODataException {
    return null;
  }

  /**
   * This method should return a list of all {@link Action} for the FullQualifiedname or <b>null</b> if nothing is found
   * 
   * @param actionName
   * @return List of {@link Action} or null
   * @throws ODataException
   */
  public List<Action> getActions(final FullQualifiedName actionName) throws ODataException {
    return null;
  }

  /**
   * This method should return a list of all {@link Function} for the FullQualifiedname or <b>null</b> if nothing is
   * found
   * 
   * @param functionName
   * @return List of {@link Function} or null
   * @throws ODataException
   */
  public List<Function> getFunctions(final FullQualifiedName functionName) throws ODataException {
    return null;
  }

  /**
   * This method should return a {@link Term} for the FullQualifiedName or <b>null</b> if nothing is found.
   * @param termName the name of the Term
   * @return {@link Term} or null
   * @throws ODataException
   */
  public Term getTerm(final FullQualifiedName termName) throws ODataException {
    return null;
  }

  /**
   * This method should return an {@link EntitySet} or <b>null</b> if nothing is found
   * 
   * @param entityContainer this EntitySet is contained in
   * @param entitySetName
   * @return {@link EntitySet} for the given container and entityset name
   * @throws ODataException
   */
  public EntitySet getEntitySet(final FullQualifiedName entityContainer, final String entitySetName)
      throws ODataException {
    return null;
  }

  /**
   * This method should return an {@link Singleton} or <b>null</b> if nothing is found
   * 
   * @param entityContainer this Singleton is contained in
   * @param singletonName
   * @return {@link Singleton} for given container and singleton name
   * @throws ODataException
   */
  public Singleton getSingleton(final FullQualifiedName entityContainer, final String singletonName)
      throws ODataException {
    return null;
  }

  /**
   * This method should return an {@link ActionImport} or <b>null</b> if nothing is found
   * 
   * @param entityContainer this ActionImport is contained in
   * @param actionImportName
   * @return {@link ActionImport} for the given container and ActionImport name
   * @throws ODataException
   */
  public ActionImport getActionImport(final FullQualifiedName entityContainer, final String actionImportName)
      throws ODataException {
    return null;
  }

  /**
   * This method should return a {@link FunctionImport} or <b>null</b> if nothing is found
   * 
   * @param entityContainer this FunctionImport is contained in
   * @param functionImportName
   * @return {@link FunctionImport} for the given container name and function import name
   * @throws ODataException
   */
  public FunctionImport getFunctionImport(final FullQualifiedName entityContainer, final String functionImportName)
      throws ODataException {
    return null;
  }

  /**
   * This method should return an {@link EntityContainerInfo} or <b>null</b> if nothing is found
   * 
   * @param entityContainerName (null for default container)
   * @return {@link EntityContainerInfo} for the given name
   * @throws ODataException
   */
  public EntityContainerInfo getEntityContainerInfo(final FullQualifiedName entityContainerName) throws ODataException {
    return null;
  }

  /**
   * This method should return a list of all namespaces which have an alias
   * 
   * @return List of alias info
   * @throws ODataException
   */
  public List<AliasInfo> getAliasInfos() throws ODataException {
    return null;
  }

  /**
   * This method should return a collection of all {@link Schema}
   * 
   * @return List<{@link Schema}>
   * @throws ODataException
   */
  public List<Schema> getSchemas() throws ODataException {
    return null;
  }

  /**
   * Returns the entity container of this edm
   * @return {@link EntityContainer} of this edm
   */
  public EntityContainer getEntityContainer() throws ODataException {
    return null;
  }
  
  public List<Reference> getReferences() throws ODataException {
	  return null;
  }
}
