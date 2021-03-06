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

@Rule(
  key = "NonEmptyCaseWithoutBreak",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class NonEmptyCaseWithoutBreakCheck extends SquidCheck<EcmaScriptGrammar> {

  @Override
  public void init() {
    subscribeTo(getContext().getGrammar().caseClause, getContext().getGrammar().defaultClause);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.getNextAstNode().is(getContext().getGrammar().caseClause, getContext().getGrammar().defaultClause, getContext().getGrammar().caseClauses)) {
      EcmaScriptGrammar grammar = getContext().getGrammar();
      AstNode statementList = astNode.getFirstChild(getContext().getGrammar().statementList);
      if (statementList != null && statementList.getLastChild().getFirstChild().isNot(grammar.breakStatement, grammar.returnStatement, grammar.throwStatement)) {
        getContext().createLineViolation(this, "Last statement in this switch-clause should be an unconditional break.", astNode);
      }
    }
  }

}
