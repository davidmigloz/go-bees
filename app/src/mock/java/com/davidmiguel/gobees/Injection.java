/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees;

import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.davidmiguel.gobees.data.source.local.GoBeesLocalDataSource;
import com.davidmiguel.gobees.data.source.network.WeatherDataSource;

/**
 * Enables injection of mock implementations for GoBeesDataSource at compile time.
 * This is useful for testing, since it allows us to use a fake instance of the class
 * to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    private Injection() {
    }

    public static GoBeesRepository provideApiariesRepository() {
        return GoBeesRepository.getInstance(GoBeesLocalDataSource.getInstance(),
                WeatherDataSource.getInstance());
    }
}