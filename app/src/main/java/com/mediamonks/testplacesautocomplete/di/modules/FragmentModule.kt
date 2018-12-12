package com.mediamonks.testplacesautocomplete.di.modules

import com.mediamonks.testplacesautocomplete.ui.PlacesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created on 05/12/2018.
 */
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun constributePlacesFragment(): PlacesFragment

}