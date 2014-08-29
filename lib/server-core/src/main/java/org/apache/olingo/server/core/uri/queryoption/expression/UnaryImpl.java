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
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Unary;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

public class UnaryImpl extends ExpressionImpl implements Unary {

  private UnaryOperatorKind operator;
  private ExpressionImpl expression;

  @Override
  public UnaryOperatorKind getOperator() {
    return operator;
  }

  public void setOperator(final UnaryOperatorKind operator) {
    this.operator = operator;
  }

  @Override
  public Expression getOperand() {
    return expression;
  }

  public void setOperand(final ExpressionImpl expression) {
    this.expression = expression;
  }

  @Override
  public void accept(final ExpressionVisitor visitor) throws ExpressionVisitException, ODataApplicationException {
    visitor.visit(this);
  }

}
