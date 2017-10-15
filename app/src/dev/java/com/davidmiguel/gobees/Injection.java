package com.davidmiguel.gobees;

import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.davidmiguel.gobees.data.source.local.GoBeesLocalDataSource;
import com.davidmiguel.gobees.data.source.network.WeatherDataSource;

/**
 * Enables injection of production implementations for GoBeesDataSource at compile time.
 */
public class Injection {

    private Injection() {
    }

    public static GoBeesRepository provideApiariesRepository() {
        return GoBeesRepository.getInstance(GoBeesLocalDataSource.getInstance(),
                WeatherDataSource.getInstance());
    }
}