package com.staygrateful.poi_test.di

import com.staygrateful.poi_test.domain.interactor.HomepageInteractor
import com.staygrateful.poi_test.domain.usecase.HomepageUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {

    @Binds
    abstract fun bindHomeUseCase(interactor: HomepageInteractor): HomepageUseCase
}