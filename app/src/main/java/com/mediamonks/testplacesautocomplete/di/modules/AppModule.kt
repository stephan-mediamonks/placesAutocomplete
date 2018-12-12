package com.mediamonks.testplacesautocomplete.di.modules

import com.mediamonks.testplacesautocomplete.util.permissions.PermissionHelper
import com.mediamonks.testplacesautocomplete.util.permissions.PermissionHelperImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created on 05/12/2018.
 */
@Module
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun providePermissionHelper(helper: PermissionHelperImpl): PermissionHelper
}