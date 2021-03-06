/*
 * Sonar JavaScript Plugin
 * Copyright (C) 2011 Eriks Nukis and SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.javascript.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.javascript.api.EcmaScriptGrammar;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

@Rule(
  key = "RedeclaredFunction",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class RedeclaredFunctionCheck extends SquidCheck<EcmaScriptGrammar> {

  private Stack<Set<String>> stack;

  @Override
  public void init() {
    subscribeTo(getContext().getGrammar().functionDeclaration, getContext().getGrammar().functionExpression);
  }

  @Override
  public void visitFile(AstNode astNode) {
    stack = new Stack<Set<String>>();
    stack.add(new HashSet<String>());
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(getContext().getGrammar().functionDeclaration)) {
      Set<String> currentScope = stack.peek();
      String functionName = astNode.getFirstChild(getContext().getGrammar().identifier).getTokenValue();
      if (currentScope.contains(functionName)) {
        getContext().createLineViolation(this, "Rename function '" + functionName + "' as this name is already used.", astNode);
      } else {
        currentScope.add(functionName);
      }
    }
    stack.add(new HashSet<String>());
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(getContext().getGrammar().functionDeclaration, getContext().getGrammar().functionExpression)) {
      stack.pop();
    }
  }

  @Override
  public void leaveFile(AstNode astNode) {
    stack = null;
  }

}
