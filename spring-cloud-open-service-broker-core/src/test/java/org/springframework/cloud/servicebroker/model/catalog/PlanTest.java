/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.servicebroker.model.catalog;

import com.jayway.jsonpath.DocumentContext;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.springframework.cloud.servicebroker.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.springframework.cloud.servicebroker.JsonPathAssert.assertThat;

public class PlanTest {

	@Test
	public void planWithDefaultsIsSerializedToJson() {
		Plan plan = Plan.builder()
				.id("plan-id-one")
				.name("plan-one")
				.description("Plan One")
				.build();

		assertThat(plan.getId()).isEqualTo("plan-id-one");
		assertThat(plan.getName()).isEqualTo("plan-one");
		assertThat(plan.getDescription()).isEqualTo("Plan One");
		assertThat(plan.isFree()).isEqualTo(true);
		assertThat(plan.isBindable()).isNull();
		assertThat(plan.getMetadata()).isNull();
		assertThat(plan.getSchemas()).isNull();

		DocumentContext json = JsonUtils.toJsonPath(plan);

		assertThat(json).hasPath("$.id").isEqualTo("plan-id-one");
		assertThat(json).hasPath("$.name").isEqualTo("plan-one");
		assertThat(json).hasPath("$.description").isEqualTo("Plan One");
		assertThat(json).hasPath("$.free").isEqualTo(true);
		assertThat(json).hasNoPath("$.bindable");
		assertThat(json).hasNoPath("$.metadata");
		assertThat(json).hasNoPath("$.schemas");


		Plan deserialized = JsonUtils.fromJson(JsonUtils.toJson(plan), Plan.class);
		assertThat(deserialized.getId()).isEqualTo("plan-id-one");
		assertThat(deserialized.getName()).isEqualTo("plan-one");
		assertThat(deserialized.getDescription()).isEqualTo("Plan One");
		assertThat(deserialized.isFree()).isEqualTo(true);
		assertThat(deserialized.isBindable()).isNull();
		assertThat(deserialized.getMetadata()).isEmpty(); //it's ok to not return null
		assertThat(deserialized.getSchemas()).isNull();
	}

	@Test
	@SuppressWarnings("serial")
	public void planWithAllFieldsIsSerializedToJson() {
		HashMap<String, Object> standardCost = new HashMap<String, Object>() {
			{
				put("unit", "MONTHLY");
				put("amount", new HashMap<String, Object>() {
					{
						put("usd", 649.0d);
					}
				});
			}
		};
		List<HashMap<String, Object>> costs = new ArrayList<>();
		costs.add(standardCost);
		Map<String, Object> metadata = new HashMap<String, Object>() {
			{
				put("field3", "value3");
				put("field4", "value4");
				put("bullets", new String[] { "bullet1", "bullet2" });
				put("costs", costs);
				put("displayName", "sample display name");
			}
		};

		Plan plan = Plan.builder()
				.id("plan-id-one")
				.name("plan-one")
				.description("Plan One")
				.free(false)
				.bindable(true)
				.metadata("field1", "value1")
				.metadata("field2", "value2")
				.metadata(metadata)
				.schemas(Schemas.builder().build())
				.build();

		assertThat(plan.getId()).isEqualTo("plan-id-one");
		assertThat(plan.getName()).isEqualTo("plan-one");
		assertThat(plan.getDescription()).isEqualTo("Plan One");
		assertThat(plan.isFree()).isEqualTo(false);
		assertThat(plan.isBindable()).isEqualTo(true);
		assertThat(plan.getMetadata()).hasSize(7);
		assertThat(plan.getMetadata()).contains(
				entry("field1", "value1"),
				entry("field2", "value2"),
				entry("field3", "value3"),
				entry("field4", "value4"),
				entry("displayName", "sample display name"),
				entry("bullets", new String[]{"bullet1", "bullet2"}),
				entry("costs", costs)
		);
		assertThat(plan.getSchemas()).isNotNull();

		DocumentContext json = JsonUtils.toJsonPath(plan);

		assertThat(json).hasPath("$.id").isEqualTo("plan-id-one");
		assertThat(json).hasPath("$.name").isEqualTo("plan-one");
		assertThat(json).hasPath("$.description").isEqualTo("Plan One");
		assertThat(json).hasPath("$.free").isEqualTo(false);
		assertThat(json).hasPath("$.bindable").isEqualTo(true);
		assertThat(json).hasMapAtPath("$.metadata").contains(
				entry("field1", "value1"),
				entry("field2", "value2"),
				entry("field3", "value3"),
				entry("field4", "value4")
		);
		assertThat(json).hasPath("$.metadata.displayName").isEqualTo("sample display name");
		assertThat(json).hasPath("$.metadata.costs");
		assertThat(json).hasMapAtPath("$.metadata.costs[0]");
		assertThat(json).hasMapAtPath("$.metadata.costs[0].amount").contains(entry("usd", 649.0d));
		assertThat(json).hasPath("$.metadata.costs[0].unit").isEqualTo("MONTHLY");
		assertThat(json).hasListAtPath("$.metadata.bullets").containsOnly("bullet1", "bullet2");
		assertThat(json).hasPath("$.schemas");
	}

	@Test
	public void equalsAndHashCode() {
		EqualsVerifier
				.forClass(Plan.class)
				.verify();
	}
}
