/*
 * This file is licensed to you under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.  You may obtain a copy of the 
 * License at
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
package org.osoco.software.samples.guessinggame;


public class Score implements Comparable<Score>{

	private final String name;
	
	private final int attempts;
	
	private final long date;
		
	public Score(final String name, final int attempts) {
		this.name = name;
		this.attempts = attempts;
		this.date = System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}
	
	public int getAttempts() {
		return attempts;
	}
	
	public long getDate() {
		return date;
	}

	@Override
	public int compareTo(final Score o) {
		if ( this.attempts < o.attempts ) {
			return -1;
		} else if ( this.attempts > o.attempts ) {
			return 1;
		} else if ( this.date < o.date ) {
			return -1;
		} else if ( this.date > o.date ) {
			return 1;
		}
		return this.name.compareTo(o.name);
	}
}
