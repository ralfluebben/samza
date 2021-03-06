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
package org.apache.samza.checkpoint.kafka;

import org.apache.samza.container.TaskName;
import org.apache.samza.container.grouper.stream.GroupByPartitionFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TestKafkaCheckpointLogKeySerde {

  @Test
  public void testBinaryCompatibility() {
    KafkaCheckpointLogKey logKey1 = new KafkaCheckpointLogKey(KafkaCheckpointLogKey.CHECKPOINT_V1_KEY_TYPE,
        new TaskName("Partition 0"), GroupByPartitionFactory.class.getCanonicalName());
    KafkaCheckpointLogKeySerde checkpointSerde = new KafkaCheckpointLogKeySerde();

    byte[] bytes = ("{\"systemstreampartition-grouper-factory\"" +
        ":\"org.apache.samza.container.grouper.stream.GroupByPartitionFactory\",\"taskName\":\"Partition 0\"," +
        "\"type\":\"checkpoint\"}").getBytes();

    // test that the checkpoints returned by the Serde are byte-wise identical to an actual checkpoint in Kafka
    Assert.assertEquals(true, Arrays.equals(bytes, checkpointSerde.toBytes(logKey1)));
  }

  @Test
  public void testSerde() {
    KafkaCheckpointLogKey key = new KafkaCheckpointLogKey(KafkaCheckpointLogKey.CHECKPOINT_V1_KEY_TYPE,
        new TaskName("Partition 0"), GroupByPartitionFactory.class.getCanonicalName());
    KafkaCheckpointLogKeySerde checkpointSerde = new KafkaCheckpointLogKeySerde();

    // test that deserialize(serialize(k)) == k
    Assert.assertEquals(key, checkpointSerde.fromBytes(checkpointSerde.toBytes(key)));
  }

  @Test
  public void testCheckpointTypeV2() {
    KafkaCheckpointLogKey keyV2 = new KafkaCheckpointLogKey(KafkaCheckpointLogKey.CHECKPOINT_V2_KEY_TYPE, new TaskName("Partition 0"),
        GroupByPartitionFactory.class.getCanonicalName());
    KafkaCheckpointLogKeySerde checkpointKeySerde = new KafkaCheckpointLogKeySerde();

    // test that deserialize(serialize(k)) == k
    Assert.assertEquals(keyV2, checkpointKeySerde.fromBytes(checkpointKeySerde.toBytes(keyV2)));
  }

  @Test
  public void testForwardsCompatibility() {
    // Set the key to another value, this is for the future if we want to support multiple checkpoint keys
    // we do not want to throw in the Serdes layer, but must be validated in the CheckpointManager
    KafkaCheckpointLogKey key = new KafkaCheckpointLogKey("checkpoint-v2",
        new TaskName("Partition 0"), GroupByPartitionFactory.class.getCanonicalName());
    KafkaCheckpointLogKeySerde checkpointSerde = new KafkaCheckpointLogKeySerde();

    // test that deserialize(serialize(k)) == k
    Assert.assertEquals(key, checkpointSerde.fromBytes(checkpointSerde.toBytes(key)));
  }
}
