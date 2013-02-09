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

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class JavaScriptFileCoverage {

  private Map<Integer, Integer> lineCoverageData = new HashMap<Integer, Integer>();
  private Multimap<Integer, Integer> branchCoverageData = ArrayListMultimap.create();
  private String filePath;

  public Map<Integer, Integer> getLineCoverageData() {
    return lineCoverageData;
  }

  public void setLineCoverage(Map<Integer, Integer> lineCoverage) {
    this.lineCoverageData = lineCoverage;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  // Executable Line Count
  public int getLinesToCover() {
    return lineCoverageData.size();
  }

  public int getBranchesToCover() {
      return branchCoverageData.values().size();
  }

  // Covered Executable Line Count
  public int getCoveredLines() {
    int lines = 0;
    for (Map.Entry<Integer, Integer> entry : lineCoverageData.entrySet()) {
      if (entry.getValue() > 0) {
        lines++;
      }
    }
    return lines;
  }

    public int getCoveredBranches() {
        int branches = 0;
        for (Map.Entry<Integer, Integer> entry : branchCoverageData.entries()) {
            if (entry.getValue() > 0) {
                branches++;
            }
        }
        return branches;
    }

  public void addLine(int lineNumber, int executionCount) {
    lineCoverageData.put(lineNumber, executionCount);
  }

    public void addBranch(int lineNumber, int executionCount) {
        branchCoverageData.put(lineNumber, executionCount);
    }

    public Multimap<Integer, Integer> getBranchCoverageData() {
        return branchCoverageData;
    }

  public int getUncoveredLines() {
    return getLinesToCover() - getCoveredLines();
  }

    public int getUncoveredBranches() {
        return getBranchesToCover() - getCoveredBranches();
    }


}
