package com.davidmiguel.gobees;

import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.data.source.local.GoBeesLocalDataSource;

/**
 * Enables injection of production implementations for TasksDataSource at compile time.
 */
public class Injection {

    public static GoBeesRepository provideApiariesRepository() {
        return GoBeesRepository.getInstance(GoBeesLocalDataSource.getInstance());
    }
}