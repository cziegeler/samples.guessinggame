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
package org.osoco.software.samples.guessinggame.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osoco.software.samples.guessinggame.HighscoreService;
import org.osoco.software.samples.guessinggame.Level;
import org.osoco.software.samples.guessinggame.Score;

@Component(service = HighscoreService.class)
public class HighscoreServiceImpl implements HighscoreService {

    public @interface Config {
        int maxEntries() default 10;
    }

    private Config configuration;

    private final Map<Level, List<Score>> table = new HashMap<Level, List<Score>>();

    @Activate
    protected void activate(final Config config) {
        this.configuration = config;
    }

    @Override
    public List<Score> getHighscores(final Level level) {
        synchronized (table) {
            return this.table.get(level);
        }
    }

    @Override
    public int addScore(final Level level, final Score score) {
        synchronized (table) {
            List<Score> highscores = table.get(level);
            if (highscores == null) {
                highscores = new ArrayList<Score>();
            } else {
                highscores = new ArrayList<Score>(highscores);
            }
            highscores.add(score);
            Collections.sort(highscores);
            if (highscores.size() > configuration.maxEntries()) {
                highscores.remove(configuration.maxEntries());
            }
            table.put(level, Collections.unmodifiableList(highscores));
            return highscores.indexOf(score);
        }
    }
}
