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
package org.apache.olingo.server.core.uri.testutil;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceLambdaAll;
import org.apache.olingo.server.api.uri.UriResourceLambdaAny;
import org.apache.olingo.server.api.uri.UriResourcePartTyped;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Alias;
import org.apache.olingo.server.api.uri.queryoption.expression.Binary;
import org.apache.olingo.server.api.uri.queryoption.expression.Enumeration;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.LambdaRef;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.Method;
import org.apache.olingo.server.api.uri.queryoption.expression.TypeLiteral;
import org.apache.olingo.server.api.uri.queryoption.expression.Unary;

@SuppressWarnings("nls")
public class FilterTreeToText implements ExpressionVisitor {
  StringBuilder sb = new StringBuilder();
  
  public String getText() {
	  return sb.toString();
  }
  
  public static String Serialize(final FilterOption filter)
      throws ExpressionVisitException, ODataApplicationException {
	FilterTreeToText visitor = new FilterTreeToText();
    filter.getExpression().accept(visitor);
    return visitor.getText();
  }

  public static String Serialize(final Expression expression)
      throws ExpressionVisitException, ODataApplicationException {
	FilterTreeToText visitor = new FilterTreeToText();
    expression.accept(visitor);
    return visitor.getText();
  }
  
	private void visitNode(Expression expr) throws ExpressionVisitException,
			ODataApplicationException {
	  if (expr != null) {
		  expr.accept(this);
	  }
  }

  @Override
  public void visit(final Binary expr)
      throws ExpressionVisitException, ODataApplicationException {
	sb.append("<");
	visitNode(expr.getLeftOperand());
	sb.append(" ").append(expr.getOperator().toString()).append(" ");
	visitNode(expr.getRightOperand());
	sb.append(">");
  }

  @Override
  public void visit(final Unary expr)
      throws ExpressionVisitException, ODataApplicationException {

	sb.append("<");
	sb.append(expr.getOperator().toString());
	sb.append(" ");
	visitNode(expr.getOperand());
	sb.append(">");	  
  }

  @Override
  public void visit(Method expr)
      throws ExpressionVisitException, ODataApplicationException {
	sb.append("<");
	sb.append(expr.getMethod()).append("(");

    int i = 0;
    while (i < expr.getParameters().size()) {      
      if (i > 0) {
        sb.append(",");
      }
      visitNode(expr.getParameters().get(i));
      i++;
    }
    sb.append(")>");
  }

  @Override
  public void visit(final Literal literal) throws ExpressionVisitException, ODataApplicationException {
	sb.append("<");
	sb.append(literal.getText());
	sb.append(">");
  }

  @Override
  public void visit(final Member expr) throws ExpressionVisitException, ODataApplicationException {
	sb.append("<");
	
	String ret = "";
	UriInfoResource path = expr.getResourcePath();
    for (UriResource item : path.getUriResourceParts()) {
    	String tmp = "";
        if (item instanceof UriResourceLambdaAll) {
          UriResourceLambdaAll all = (UriResourceLambdaAll) item;
          tmp = visitLambdaExpression("ALL", all.getLambdaVariable(), all.getExpression());
        } else if (item instanceof UriResourceLambdaAny) {
          UriResourceLambdaAny any = (UriResourceLambdaAny) item;
          tmp = visitLambdaExpression("ANY", any.getLamdaVariable(), any.getExpression());
        } else if (item instanceof UriResourcePartTyped) {
          UriResourcePartTyped typed = (UriResourcePartTyped) item;
          tmp = typed.toString(true);
        }
		if (ret.length() != 0) {
			ret += "/";
		}
		ret += tmp;
    }
    sb.append(ret);
	sb.append(">");    
  }

  @Override
  public void visit(final Alias expr) throws ExpressionVisitException, ODataApplicationException {
	sb.append("<");
	sb.append(expr.getParameterName());
	sb.append(">");
  }

  public String visitLambdaExpression(final String functionText, final String string, final Expression expression)
      throws ExpressionVisitException, ODataApplicationException {
	
	FilterTreeToText visitor = new FilterTreeToText();
	StringBuilder txt = new StringBuilder();
	txt.append("<");
	txt.append(functionText).append(";");
	if (expression != null) {
		expression.accept(visitor);
	}
	txt.append(visitor.getText());
	txt.append(">");
	return txt.toString();
  }

  @Override
  public void visit(final TypeLiteral type) {
	sb.append("<");
	sb.append(type.getType().getNamespace()).append(".").append(type.getType().getName());
	sb.append(">");
  }

  @Override
  public void visit(final LambdaRef expr) {
	sb.append("<");
	sb.append(expr.getVariableName());
	sb.append(">");
  }

  @Override
  public void visit(final Enumeration expr)
      throws ExpressionVisitException, ODataApplicationException {
    String tmp = "";

    for (String item : expr.getValues()) {
      if (tmp.length() > 0) {
        tmp += ",";
      }
      tmp += item;
    }

	sb.append("<");
	sb.append(expr.getType().getNamespace()).append(".").append(expr.getType().getName());
	sb.append("<").append(tmp).append(">");
	sb.append(">");    
  }

}
