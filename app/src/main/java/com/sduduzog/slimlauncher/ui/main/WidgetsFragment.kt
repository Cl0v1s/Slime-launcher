package com.sduduzog.slimlauncher.ui.main

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sduduzog.slimlauncher.App
import com.sduduzog.slimlauncher.MainActivity
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.databinding.WidgetsFragmentBinding
import com.sduduzog.slimlauncher.models.MainViewModel
import com.sduduzog.slimlauncher.models.Widget
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WidgetsFragment: Fragment(R.layout.widgets_fragment) {
    private var _binding: WidgetsFragmentBinding? = null
    private var _host: AppWidgetHost? = null;
    private var _manager: AppWidgetManager? = null;

    private  val _viewModel: MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = WidgetsFragmentBinding.inflate(inflater, container, false)
        _host = ((context!!.applicationContext as App).currentActivity as MainActivity).widgetHost
        _manager = ((context!!.applicationContext as App).currentActivity as MainActivity).widgetManager
        return _binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.widgets.observeForever(Observer<List<Widget>> {
            _binding!!.widgetsFragment.removeAllViews()
            it.forEach({ widget -> spawnWidget(widget.load()!!) })
        })
    }

    fun spawnWidget(extras: Bundle) {
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        try {
            val appWidgetInfo: AppWidgetProviderInfo = _manager!!.getAppWidgetInfo(appWidgetId)
            val hostView: AppWidgetHostView = _host!!.createView(context!!.applicationContext, appWidgetId, appWidgetInfo)
            hostView.setAppWidget(appWidgetId, appWidgetInfo)
            _binding!!.widgetsFragment.addView(hostView)
            hostView.layoutParams.height = if(appWidgetInfo.minHeight < 250) 250 else appWidgetInfo.minHeight
            _binding!!.widgetsFragment.invalidate();
        } catch(e: NullPointerException) {
            System.out.println(e.message)
            _viewModel.removeWidget(appWidgetId)
        }
    }
}