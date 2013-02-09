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
package org.sonar.plugins.javascript.coverage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses JsTestDriver file coverage report files (generated by
 * http://code.google.com/p/js-test-driver/source/browse/trunk/JsTestDriver/src/com/google/jstestdriver/coverage/LcovWriter.java
 *
 * @author Eriks.Nukis
 *
 */
public final class LCOVParser {

  private static final Logger LOG = LoggerFactory.getLogger(LCOVParser.class);

  public List<JavaScriptFileCoverage> parseFile(File file) {
    List<String> lines = new LinkedList<String>();
    try {
      lines = FileUtils.readLines(file);
    } catch (IOException e) {
      LOG.debug("Cound not read content from file: {}", file.getAbsolutePath(), e);
    }

    List<JavaScriptFileCoverage> coveredFiles = new LinkedList<JavaScriptFileCoverage>();

    JavaScriptFileCoverage fileCoverage = new JavaScriptFileCoverage();

    for (String line : lines) {
      if (line.startsWith("SF:")) {
        fileCoverage = new JavaScriptFileCoverage();
        String filePath = line.substring(line.indexOf("SF:") + 3);

        fileCoverage.setFilePath(filePath);

      } else if (line.startsWith("DA:")) {
        List<String> tokens = splitLine(line,"DA:");
        String executionCount = tokens.get(1);
        String lineNumber = tokens.get(0);

        fileCoverage.addLine(Integer.valueOf(lineNumber), Integer.valueOf(executionCount));

      } else if (line.startsWith("BRDA:")) {
        List<String> tokens = splitLine(line,"BRDA:");
          final int lineNumber = Integer.valueOf(tokens.get(0));
          final int hits = Integer.valueOf(tokens.get(3));
        fileCoverage.addBranch(lineNumber,hits);
      }else if (line.startsWith("end_of_record")) {
        coveredFiles.add(fileCoverage);
      }
    }
    return coveredFiles;
  }

    private List<String> splitLine(final String line, final String prefix) {
        final String sub = line.substring(prefix.length());
        final StringTokenizer tokenizer = new StringTokenizer(sub,",");
        final List<String> res = new ArrayList<String>();
        while(tokenizer.hasMoreTokens()) {
            res.add(tokenizer.nextToken());
        }
        return res;
    }
}
