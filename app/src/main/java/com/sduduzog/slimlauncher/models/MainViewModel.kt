package com.sduduzog.slimlauncher.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sduduzog.slimlauncher.data.model.App
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    _baseRepository: Repository
) : ViewModel() {

    private var _apps: LiveData<List<HomeApp>> = _baseRepository.apps

    private val _installedApps = mutableListOf<HomeApp>()

    fun setInstalledApps(apps: List<App>) {
        this._installedApps.clear()
        this._installedApps.addAll(
                apps.map { app -> HomeApp.from(app) }
        )
    }

    val apps: LiveData<List<HomeApp>>
        get() = _apps

    val installedApps: List<HomeApp>
        get() = _installedApps
}