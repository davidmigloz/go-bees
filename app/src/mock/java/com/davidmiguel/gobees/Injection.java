package com.davidmiguel.gobees;

import android.content.Context;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.source.cache.ApiariesRepository;
import com.davidmiguel.gobees.data.source.local.FakeApiariesLocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for ApiariesDataSource at compile time.
 * This is useful for testing, since it allows us to use a fake instance of the class
 * to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static ApiariesRepository provideApiariesRepository(@NonNull Context context) {
        checkNotNull(context);
        return ApiariesRepository.getInstance(FakeApiariesLocalDataSource.getInstance());
    }
}