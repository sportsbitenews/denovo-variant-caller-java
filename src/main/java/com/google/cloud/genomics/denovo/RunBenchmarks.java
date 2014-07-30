/*
 *Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.genomics.denovo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * Performs benchmarking for readstore and variantstore search requests
 */
public class RunBenchmarks {

  public static void main(String[] args) throws FileNotFoundException {
    Benchmarking benchmarking ; 
    
    File benchmarksDir = new File(System.getProperty("user.home") + "/.benchmarks");
    DenovoUtil.helperCreateDirectory(benchmarksDir);
    
    // Readstore benchmarking
    benchmarking = new Benchmarking.Builder("readstore", 
        new PrintStream(benchmarksDir.getAbsolutePath() + "/readstore")).
        maxReadstoreRequests(100).
        numRepeatExperiment(10).
        build();
    benchmarking.execute();
    
    // varstore benchmarking
    for(long maxResults : Arrays.asList(1000L,5000L,10000L,20000L,30000L,40000L,50000L)) {

      String logFileString = benchmarksDir.getAbsolutePath() + "/varstore" +
          String.valueOf(maxResults/1000) + "K";
      benchmarking = new Benchmarking.Builder("varstore", new PrintStream(logFileString)).
          maxVariantResults(maxResults).
          maxVarstoreRequests(50).
          numRepeatExperiment(10).
          build();
      benchmarking.execute();
    }
  }
}
