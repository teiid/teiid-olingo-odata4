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
package org.apache.olingo.server.api.uri.queryoption.expression;

import org.apache.olingo.server.api.ODataApplicationException;

/**
 * Generic interface to define expression visitors with arbitrary return types.
 */
public interface ExpressionVisitor {

  /**
   * Called for each traversed {@link Binary} expression
   * @param obj
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
	void visit(Binary obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link Unary} expression
   * @param obj
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
	void visit(Unary obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link Method} expression
   * @param obj
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
	void visit(Method obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link LambdaRef} expression
   * @param obj
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
	void visit(LambdaRef obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link Literal} expression
   * @param obj
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
	void visit(Literal obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link Member} expression
   * @param member 
   * (this includes for example the usage of $root and $it inside the URI)
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
	void visit(Member obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link Alias} expression
   * @param obj 
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
	void visit(Alias obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link TypeLiteral} expression
   * @param obj
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
    void visit(TypeLiteral obj) throws ExpressionVisitException, ODataApplicationException;

  /**
   * Called for each traversed {@link Enumeration} expression
   * @param obj
   * @throws ExpressionVisitException Thrown if an exception while traversing occured
   * @throws ODataApplicationException Thrown by the application
   */
    void visit(Enumeration obj) throws ExpressionVisitException, ODataApplicationException;
}
