package com.davidmiguel.gobees;

import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.data.source.local.FakeGoBeesLocalDataSource;

/**
 * Enables injection of mock implementations for GoBeesDataSource at compile time.
 * This is useful for testing, since it allows us to use a fake instance of the class
 * to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static GoBeesRepository provideApiariesRepository() {
        return GoBeesRepository.getInstance(FakeGoBeesLocalDataSource.getInstance());
    }
}