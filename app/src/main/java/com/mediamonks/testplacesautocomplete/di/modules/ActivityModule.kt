package com.mediamonks.testplacesautocomplete.di.modules

import com.mediamonks.testplacesautocomplete.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created on 05/12/2018.
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}