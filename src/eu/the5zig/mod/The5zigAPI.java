/*
 *    Copyright 2016 5zig
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.the5zig.mod;

import org.apache.logging.log4j.Logger;

/**
 * Main class that holds the API instance.
 */
public class The5zigAPI {

	/**
	 * Set at runtime by the 5zig mod core.
	 */
	static ModAPI apiInstance;

	/**
	 * Set at runtime by the 5zig mod core.
	 */
	static Logger loggerInstance;

	/**
	 * @return the API instance.
	 */
	public static ModAPI getAPI() {
		return apiInstance;
	}

	/**
	 * @return a logger.
	 */
	public static Logger getLogger() {
		return loggerInstance;
	}

}
