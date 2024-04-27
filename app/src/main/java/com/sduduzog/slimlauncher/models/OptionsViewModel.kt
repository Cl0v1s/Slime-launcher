package com.sduduzog.slimlauncher.models

import android.appwidget.AppWidgetHost
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(
        private val repository: Repository
) : ViewModel() {

    val widgets: LiveData<List<Widget>>;

    var _widgets: List<Widget> = emptyList()

    init {
       widgets = repository.widgets;
        widgets.observeForever{
            _widgets = it;
        }
    }

    fun addWidget(widget: Widget) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWidget(widget)
        }
    }

    fun purgeWidgets(host: AppWidgetHost) {
        _widgets.forEach { widget -> host!!.deleteAppWidgetId(widget.id) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.purgeWidgets()
        }
    }

}