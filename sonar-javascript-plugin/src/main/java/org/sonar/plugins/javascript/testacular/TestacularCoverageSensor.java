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

package org.sonar.plugins.javascript.testacular;

import java.util.List;

import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.plugins.javascript.JavaScriptPlugin;
import org.sonar.plugins.javascript.core.JavaScript;
import org.sonar.plugins.javascript.coverage.JavaScriptFileCoverage;
import org.sonar.plugins.javascript.jstestdriver.JsTestDriverCoverageSensor;

public class TestacularCoverageSensor extends JsTestDriverCoverageSensor {


  public TestacularCoverageSensor(JavaScript javascript) {
    super(javascript);
  }

  public boolean shouldExecuteOnProject(Project project) {
        return javascript.equals(project.getLanguage())
                && "testacular".equals(javascript.getSettings().getString(JavaScriptPlugin.TEST_FRAMEWORK_KEY));
    }

    protected JavaScriptFileCoverage getFileCoverage(InputFile input, List<JavaScriptFileCoverage> coverages) {
        for (JavaScriptFileCoverage file : coverages) {
          String baseDir = javascript.getSettings().getString(JavaScriptPlugin.TESTACULAR_SRC_FOLDER_KEY);
          String inputFile = baseDir + "/" + input.getRelativePath();
          if (file.getFilePath().equals(inputFile)) {
              return file;
          }
        }
        return null;
    }

    protected String getTestReportsFolder() {
        return javascript.getSettings().getString(JavaScriptPlugin.TESTACULAR_FOLDER_KEY);
    }

    protected String getTestCoverageFileName() {
        return javascript.getSettings().getString(JavaScriptPlugin.TESTACULAR_COVERAGE_FILE_KEY);
    }
}
