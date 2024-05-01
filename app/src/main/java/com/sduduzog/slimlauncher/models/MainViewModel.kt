package com.sduduzog.slimlauncher.models

import android.appwidget.AppWidgetHost
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sduduzog.slimlauncher.data.model.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _baseRepository: Repository
) : ViewModel() {

    private var _apps: LiveData<List<HomeApp>> = _baseRepository.apps
    private var _widgets: List<Widget> = emptyList()

    init {
        _baseRepository.widgets.observeForever({ _widgets = it })
    }

    private val _installedApps = mutableListOf<HomeApp>()

    fun setInstalledApps(apps: List<App>) {
        this._installedApps.clear()
        this._installedApps.addAll(
                apps.map { app -> HomeApp.from(app) }
        )
    }

    fun removeWidget(widgetId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _baseRepository.removeWidget(_widgets.find { it.id == widgetId }!!);
        }
    }

    val apps: LiveData<List<HomeApp>>
        get() = _apps

    val widgets: LiveData<List<Widget>>
        get() = _baseRepository.widgets

    val installedApps: List<HomeApp>
        get() = _installedApps
}