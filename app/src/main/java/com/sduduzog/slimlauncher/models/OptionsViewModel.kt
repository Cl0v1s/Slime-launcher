package com.sduduzog.slimlauncher.models

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

    init {
       widgets = repository.widgets;
    }

    fun addWidget(widget: Widget) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWidget(widget)
        }
    }

}