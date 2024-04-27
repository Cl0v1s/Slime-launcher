package com.sduduzog.slimlauncher.di

import com.sduduzog.slimlauncher.data.AppDao
import com.sduduzog.slimlauncher.data.WidgetDao
import com.sduduzog.slimlauncher.models.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun providesRepository(appDao: AppDao, widgetDao: WidgetDao): Repository {
        System.out.println("COUCOU")
        return Repository(appDao, widgetDao)
    }
}