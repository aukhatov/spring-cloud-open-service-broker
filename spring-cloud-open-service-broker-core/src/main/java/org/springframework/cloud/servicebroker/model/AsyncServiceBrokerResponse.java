/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.servicebroker.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Details of a response that support asynchronous behavior.
 *
 * @author Scott Frederick
 * @author Roy Clarkson
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsyncServiceBrokerResponse {
	@JsonIgnore //not sent on the wire as json payload, but as http status instead
	protected final boolean async;

	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	protected final String operation;

	protected AsyncServiceBrokerResponse(boolean async, String operation) {
		this.async = async;
		this.operation = operation;
	}

	/**
	 * Get a boolean value indicating whether the requested operation is being performed synchronously or
	 * asynchronously.
	 *
	 * @return the boolean value
	 */
	public boolean isAsync() {
		return this.async;
	}

	/**
	 * Get a description of the operation being performed in support of an asynchronous response.
	 *
	 * @return the operation description
	 */
	public String getOperation() {
		return this.operation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AsyncServiceBrokerResponse)) return false;
		AsyncServiceBrokerResponse that = (AsyncServiceBrokerResponse) o;
		return that.canEqual(this) &&
				async == that.async &&
				Objects.equals(operation, that.operation);
	}

	public boolean canEqual(Object other) {
		return (other instanceof AsyncServiceBrokerResponse);
	}

	@Override
	public int hashCode() {
		return Objects.hash(async, operation);
	}

	@Override
	public String toString() {
		return "AsyncServiceInstanceResponse{" +
				"async=" + async +
				", operation='" + operation + '\'' +
				'}';
	}

}
