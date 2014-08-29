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
package org.apache.olingo.server.core.uri.queryoption.expression;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.Binary;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;

public class BinaryImpl extends ExpressionImpl implements Binary {

  private BinaryOperatorKind operator;
  private ExpressionImpl left;
  private ExpressionImpl right;

  @Override
  public BinaryOperatorKind getOperator() {
    return operator;
  }

  public Binary setOperator(final BinaryOperatorKind operator) {
    this.operator = operator;
    return this;
  }

  @Override
  public Expression getLeftOperand() {
    return left;
  }

  public void setLeftOperand(final ExpressionImpl operand) {
    left = operand;
  }

  @Override
  public Expression getRightOperand() {
    return right;
  }

  public void setRightOperand(final ExpressionImpl operand) {
    right = operand;

  }

  @Override
  public void accept(final ExpressionVisitor visitor) throws ExpressionVisitException, ODataApplicationException {
    visitor.visit(this);
  }

}
