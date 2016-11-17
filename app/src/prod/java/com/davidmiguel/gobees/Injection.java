package com.davidmiguel.gobees;

import android.content.Context;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.source.cache.ApiariesRepository;
import com.davidmiguel.gobees.data.source.local.ApiariesLocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for TasksDataSource at compile time.
 */
public class Injection {

    public static ApiariesRepository provideApiariesRepository(@NonNull Context context) {
        checkNotNull(context);
        return ApiariesRepository.getInstance(ApiariesLocalDataSource.getInstance(context));
    }
}