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

package eu.the5zig.mod.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method is listening on a specific event.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {

	/**
	 * The priority of the event handler. {@link Priority#HIGHEST} will be executed first, {@link Priority#LOWEST}
	 * will be executed last.
	 *
	 * @return the event priority.
	 */
	Priority priority() default Priority.NORMAL;

	/**
	 * @return true, if the event handler should not be called if the event has been cancelled before.
	 */
	boolean ignoreCancelled() default false;

	enum Priority {
		LOWEST, LOW, NORMAL, HIGH, HIGHEST
	}

}
