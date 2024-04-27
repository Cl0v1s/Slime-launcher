package com.sduduzog.slimlauncher.models

import androidx.lifecycle.LiveData
import com.sduduzog.slimlauncher.data.AppDao
import com.sduduzog.slimlauncher.data.WidgetDao

class Repository(private val appDao: AppDao, private val widgetDao: WidgetDao) {


    // HomeApps
    private val _apps = appDao.apps

    val apps: LiveData<List<HomeApp>>
        get() = _apps

    fun addApp(app: HomeApp) {
        appDao.add(app)
    }

    fun updateApp(vararg list : HomeApp) {
        appDao.update(*list)
    }

    fun removeApp(app: HomeApp) {
        appDao.remove(app)
    }

    fun clearAppTable(){
        appDao.clearTable()
    }

    // Widgets
    private val _widgets = widgetDao.widgets
    val widgets: LiveData<List<Widget>>
        get() = _widgets

    fun addWidget(widget: Widget) {
        widgetDao.add(widget)
    }

    fun updateWidget(widget: Widget) {
        widgetDao.update(widget)
    }

    fun removeWidget(widget: Widget) {
        widgetDao.remove(widget)
    }
}
