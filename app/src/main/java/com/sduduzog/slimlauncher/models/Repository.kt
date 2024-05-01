package com.sduduzog.slimlauncher.models

import androidx.lifecycle.LiveData
import com.sduduzog.slimlauncher.data.AppDao
import com.sduduzog.slimlauncher.data.WidgetDao

class Repository(private val appDao: AppDao, private val widgetDao: WidgetDao) {

    private val _apps = appDao.apps

    private val _widgets = widgetDao.widgets

    val apps: LiveData<List<HomeApp>>
        get() = _apps

    val widgets: LiveData<List<Widget>>
        get() = _widgets


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



    fun addWidget(widget: Widget) {
        widgetDao.add(widget)
    }

    fun updateWidget(widget: Widget) {
        widgetDao.update(widget)
    }

    fun removeWidget(widget: Widget) {
        widgetDao.remove(widget)
    }

    fun purgeWidgets() {
        widgetDao.purge()
    }
}
