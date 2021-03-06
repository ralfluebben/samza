/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.samza.serializers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.samza.system.SystemStreamPartition;


/**
 * A mix-in Jackson class to convert {@link org.apache.samza.checkpoint.kafka.KafkaStateCheckpointMarker} to/from JSON
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract public class KafkaStateCheckpointMarkerMixin {
  @JsonCreator
  public KafkaStateCheckpointMarkerMixin(
      @JsonProperty("version") short version,
      @JsonProperty("changelog-ssp") SystemStreamPartition changelogSSP,
      @JsonProperty("changelog-offset") String changelogOffset) {
  }

  @JsonProperty("version")
  abstract short getVersion();

  @JsonProperty("changelog-ssp")
  abstract SystemStreamPartition getChangelogSSP();

  @JsonProperty("changelog-offset")
  abstract String getChangelogOffset();
}
