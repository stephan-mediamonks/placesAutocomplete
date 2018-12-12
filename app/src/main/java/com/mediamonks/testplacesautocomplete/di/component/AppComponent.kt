package com.mediamonks.testplacesautocomplete.di.component

import android.content.Context
import com.mediamonks.testplacesautocomplete.TestPlacesApp
import com.mediamonks.testplacesautocomplete.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created on 05/12/2018.
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        DataModule::class,
        AndroidInjectionModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(application: TestPlacesApp)
}