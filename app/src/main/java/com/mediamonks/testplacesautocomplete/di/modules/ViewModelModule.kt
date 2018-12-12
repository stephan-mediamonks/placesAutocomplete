package com.mediamonks.testplacesautocomplete.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mediamonks.testplacesautocomplete.di.viewmodel.ViewModelFactory
import com.mediamonks.testplacesautocomplete.di.viewmodel.ViewModelKey
import com.mediamonks.testplacesautocomplete.ui.viewmodel.PlacePredictionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created on 05/12/2018.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlacePredictionViewModel::class)
    abstract fun bindPhotoScreenViewModel(viewModel: PlacePredictionViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}