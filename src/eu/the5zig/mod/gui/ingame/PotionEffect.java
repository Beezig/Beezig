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

package eu.the5zig.mod.gui.ingame;

public interface PotionEffect extends Comparable<PotionEffect> {

	/**
	 * @return the name of the potion effect.
	 */
	String getName();

	/**
	 * @return the remaining time of the potion effect in ticks.
	 */
	int getTime();

	/**
	 * @return the formatted time string of the potion effect.
	 */
	String getTimeString();

	/**
	 * @return the amplifier of the potion effect, starting from 0.
	 */
	int getAmplifier();

	/**
	 * @return the icon index of the potion effect or -1, if it has no item.
	 */
	int getIconIndex();

	/**
	 * @return true, if the potion effect is good.
	 */
	boolean isGood();

	/**
	 * @return true, if the potion effect emits particles.
	 */
	boolean hasParticles();

	/**
	 * @return the color of the liquid for this potion.
	 */
	int getLiquidColor();

}
