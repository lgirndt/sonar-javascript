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

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.javascript.api.EcmaScriptGrammar;

@Rule(
  key = "ConditionalComment",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class ConditionalCommentCheck extends SquidCheck<EcmaScriptGrammar> implements AstAndTokenVisitor {

  public void visitToken(Token token) {
    for (Trivia trivia : token.getTrivia()) {
      if (trivia.isComment()) {
        String comment = trivia.getToken().getValue();
        if (comment.startsWith("/*@cc_on") || comment.startsWith("//@cc_on")) {
          getContext().createLineViolation(this, "Refactor your code to avoid using Internet Explorer's conditional comments.", trivia.getToken());
        }
      }
    }
  }

}
