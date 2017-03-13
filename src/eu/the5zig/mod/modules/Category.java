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

package eu.the5zig.mod.modules;

/**
 * All categories that a module can have.
 */
public enum Category {

	GENERAL("GENERAL"), EQUIP("EQUIP"), SERVER_GENERAL("SERVER_GENERAL"), SERVER_TIMOLIA("SERVER_TIMOLIA"), SERVER_GOMMEHD("SERVER_GOMMEHD"), SERVER_PLAYMINITY("SERVER_PLAYMINITY"),
	SERVER_BERGWERK("SERVER_BERGWERK"), SERVER_HYPIXEL("SERVER_HYPIXEL"), SERVER_VENICRAFT("SERVER_VENICRAFT"), SERVER_CYTOOXIEN("SERVER_CYTOOXIEN"), SERVER_SIMPLEHG("SERVER_SIMPLEHG"),
	SYSTEM("SYSTEM"), OTHER("OTHER");

	private String name;

	Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
