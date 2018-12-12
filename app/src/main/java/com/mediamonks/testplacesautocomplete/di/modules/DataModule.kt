package com.mediamonks.testplacesautocomplete.di.modules

import com.mediamonks.testplacesautocomplete.core.repository.StoreSearchRepository
import com.mediamonks.testplacesautocomplete.core.usecase.GetAutocompletePredictionsUseCase
import com.mediamonks.testplacesautocomplete.core.usecase.GetPredictedPlaceLocationUseCase
import com.mediamonks.testplacesautocomplete.core.usecase.StoredPredictedPlacesUseCase
import com.mediamonks.testplacesautocomplete.dataimpl.repository.StoreSearchRepositoryImpl
import com.mediamonks.testplacesautocomplete.dataimpl.usecase.GetAutocompletePredictionsUseCaseImpl
import com.mediamonks.testplacesautocomplete.dataimpl.usecase.GetPredictedPlaceLocationUseCaseImpl
import com.mediamonks.testplacesautocomplete.dataimpl.usecase.StoredPredictionUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created on 07/12/2018.
 */
@Module
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideStoreSearchRepository(impl: StoreSearchRepositoryImpl): StoreSearchRepository

    @Binds
    abstract fun provideStoredPredictionUseCase(impl: StoredPredictionUseCaseImpl): StoredPredictedPlacesUseCase

    @Binds
    abstract fun provideGetAutocompletePredictionsUseCase(impl: GetAutocompletePredictionsUseCaseImpl): GetAutocompletePredictionsUseCase

    @Binds
    abstract fun provideGetPredictedPlaceUseCase(impl: GetPredictedPlaceLocationUseCaseImpl): GetPredictedPlaceLocationUseCase
}