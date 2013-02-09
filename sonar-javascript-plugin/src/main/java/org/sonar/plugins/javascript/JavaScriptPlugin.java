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
package org.sonar.plugins.javascript;

import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.javascript.colorizer.JavaScriptColorizerFormat;
import org.sonar.plugins.javascript.core.JavaScript;
import org.sonar.plugins.javascript.core.JavaScriptSourceImporter;
import org.sonar.plugins.javascript.cpd.JavaScriptCpdMapping;
import org.sonar.plugins.javascript.jstest.JsTestCoverageSensor;
import org.sonar.plugins.javascript.jstest.JsTestMavenInitializer;
import org.sonar.plugins.javascript.jstest.JsTestMavenPluginHandler;
import org.sonar.plugins.javascript.jstest.JsTestSurefireSensor;
import org.sonar.plugins.javascript.jstestdriver.JsTestDriverCoverageSensor;
import org.sonar.plugins.javascript.jstestdriver.JsTestDriverSurefireSensor;
import org.sonar.plugins.javascript.testacular.TestacularCoverageSensor;
import org.sonar.plugins.javascript.testacular.TestacularUnitTestSensor;

import com.google.common.collect.ImmutableList;

@Properties({
  // Global JavaScript settings
  @Property(
    key = JavaScriptPlugin.FILE_SUFFIXES_KEY,
    defaultValue = JavaScriptPlugin.FILE_SUFFIXES_DEFVALUE,
    name = "File suffixes",
    description = "Comma-separated list of suffixes for files to analyze.",
    global = true,
    project = true),
  @Property(
    key = JavaScriptPlugin.TEST_FRAMEWORK_KEY,
    defaultValue = JavaScriptPlugin.TEST_FRAMEWORK_DEFAULT,
    name = "JavaScript testing framework to use",
    description = "Testing framework to use (jstest or jstestdriver).",
    global = true,
    project = true),

  // JsTestDriver (http://code.google.com/p/js-test-driver/)
  @Property(
    key = JavaScriptPlugin.JSTESTDRIVER_FOLDER_KEY,
    defaultValue = JavaScriptPlugin.JSTESTDRIVER_DEFAULT_FOLDER,
    name = "JSTestDriver output folder",
    description = "Folder where JsTestDriver unit test and code coverage reports are located.",
    global = true,
    project = true,
    category = "JSTestDriver"),
  @Property(
    key = JavaScriptPlugin.JSTESTDRIVER_COVERAGE_FILE_KEY,
    defaultValue = JavaScriptPlugin.JSTESTDRIVER_COVERAGE_REPORT_FILENAME,
    name = "JSTestDriver coverage filename",
    description = "Filename where JsTestDriver generates coverage data.",
    global = true,
    project = true,
    category = "JSTestDriver"),

  // JsTest (https://github.com/awired/jstest-maven-plugin)
  @Property(
    key = JavaScriptPlugin.JSTEST_FOLDER_KEY,
    defaultValue = JavaScriptPlugin.JSTEST_DEFAULT_FOLDER,
    name = "JSTest output folder",
    description = "Folder where JsTest unit test and code coverage reports are located.",
    global = true,
    project = true,
    category = "JSTest"),
  @Property(
    key = JavaScriptPlugin.JSTEST_COVERAGE_FILE_KEY,
    defaultValue = JavaScriptPlugin.JSTEST_COVERAGE_REPORT_FILENAME,
    name = "JSTest coverage filename",
    description = "Filename where JsTest generates coverage data.",
    global = true,
    project = true,
    category = "JSTest"),

  @Property(
    key = JavaScriptPlugin.TESTACULAR_FOLDER_KEY,
    defaultValue = JavaScriptPlugin.TESTACULAR_FOLDER,
    name = "Testacular Report Base Folder",
    description = "Base Folder where Testacular will output the coverage and test files",
    global = true,
    project = true,
    category = "Testacular"
  ),

  @Property(
    key = JavaScriptPlugin.TESTACULAR_COVERAGE_FILE_KEY,
    defaultValue = JavaScriptPlugin.TESTACULAR_COVERAGE_FILE,
    name = "Testacular coverage file",
    description = "Path to the lcov coverage file.",
    global =  true,
    project = true,
    category = "Testacular"
  ),

  @Property(
    key = JavaScriptPlugin.TESTACULAR_SRC_FOLDER_KEY,
      defaultValue = JavaScriptPlugin.TESTACULAR_SRC_FOLDER,
      name = "Javascript relative source folder",
      description = "Prefix of the file name in the lcov file",
      global = true,
      project = true,
      category = "Testacular"
  )
})
public class JavaScriptPlugin extends SonarPlugin {

  public List<Class<? extends Extension>> getExtensions() {
    return ImmutableList.of(
        JavaScript.class,
        JavaScriptSourceImporter.class,
        JavaScriptColorizerFormat.class,
        JavaScriptCpdMapping.class,

        JavaScriptSquidSensor.class,
        JavaScriptRuleRepository.class,
        JavaScriptProfile.class,

        JavaScriptCommonRulesEngineProvider.class,

        JsTestDriverSurefireSensor.class,
        JsTestDriverCoverageSensor.class,

        JsTestMavenInitializer.class,
        JsTestMavenPluginHandler.class,
        JsTestCoverageSensor.class,
        JsTestSurefireSensor.class,

        TestacularCoverageSensor.class,
        TestacularUnitTestSensor.class);
  }

  // Global JavaScript constants
  public static final String FALSE = "false";

  public static final String FILE_SUFFIXES_KEY = "sonar.javascript.file.suffixes";
  public static final String FILE_SUFFIXES_DEFVALUE = "js";

  public static final String PROPERTY_PREFIX = "sonar.javascript";

  public static final String TEST_FRAMEWORK_KEY = PROPERTY_PREFIX + ".testframework";
  public static final String TEST_FRAMEWORK_DEFAULT = "";

  // JSTestDriver
  public static final String JSTESTDRIVER_FOLDER_KEY = PROPERTY_PREFIX + ".jstestdriver.reportsfolder";
  public static final String JSTESTDRIVER_DEFAULT_FOLDER = "target/jstestdriver";
  public static final String JSTESTDRIVER_COVERAGE_FILE_KEY = PROPERTY_PREFIX + ".jstestdriver.coveragefile";
  public static final String JSTESTDRIVER_COVERAGE_REPORT_FILENAME = "jsTestDriver.conf-coverage.dat";

  // JSTest
  public static final String JSTEST_FOLDER_KEY = PROPERTY_PREFIX + ".jstest.reportsfolder";
  public static final String JSTEST_DEFAULT_FOLDER = "target/jstest/report";
  public static final String JSTEST_COVERAGE_FILE_KEY = PROPERTY_PREFIX + ".jstest.coveragefile";
  public static final String JSTEST_COVERAGE_REPORT_FILENAME = "coverage.dat";

  // Testacular
  public static final String TESTACULAR_FOLDER_KEY = PROPERTY_PREFIX + ".testacular.reportsfolder";
  public static final String TESTACULAR_FOLDER =  "target";
  public static final String TESTACULAR_COVERAGE_FILE_KEY = PROPERTY_PREFIX + ".testacular.coveragefile";
  public static final String TESTACULAR_COVERAGE_FILE = "lcov.info";
  public static final String TESTACULAR_SRC_FOLDER_KEY = PROPERTY_PREFIX + ".testacular.srcfolder";
  public static final String TESTACULAR_SRC_FOLDER = ".";
}
