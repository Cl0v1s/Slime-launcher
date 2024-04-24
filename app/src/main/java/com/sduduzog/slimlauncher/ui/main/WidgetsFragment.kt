package com.sduduzog.slimlauncher.ui.main

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.fragment.app.Fragment
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.databinding.WidgetsFragmentBinding


class WidgetsFragment: Fragment(R.layout.widgets_fragment) {

    private var _host: AppWidgetHost? = null;
    private var _manager: AppWidgetManager? = null;
    private var _binding: WidgetsFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = WidgetsFragmentBinding.inflate(inflater, container, false)

        return _binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _host = AppWidgetHost(context, R.id.widgetHostId)
        _manager = AppWidgetManager.getInstance(context)

        selectWidget();
    }

    fun selectWidget() {
        val appWidgetId: Int = _host!!.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        addEmptyData(pickIntent)
        startActivityForResult(pickIntent, R.id.RequestPickAppWidget)
    }

    fun addEmptyData(pickIntent: Intent) {
        val customInfo = ArrayList<AppWidgetProviderInfo>()
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
        val customExtras = ArrayList<Bundle>()
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
    };

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                            data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == R.id.RequestPickAppWidget) {
                configureWidget(data)
            } else if (requestCode == R.id.RequestCreateAppWidget) {
                createWidget(data!!)
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            if (appWidgetId != -1) {
                _host!!.deleteAppWidgetId(appWidgetId)
            }
        }
    }

    private fun configureWidget(data: Intent?) {
        val extras = data!!.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo: AppWidgetProviderInfo = _manager!!.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.setComponent(appWidgetInfo.configure)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            startActivityForResult(intent, R.id.RequestCreateAppWidget)
        } else {
            createWidget(data)
        }
    }

    fun createWidget(data: Intent) {
        val extras = data.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo: AppWidgetProviderInfo = _manager!!.getAppWidgetInfo(appWidgetId)
        val hostView: AppWidgetHostView = _host!!.createView(context!!.applicationContext, appWidgetId, appWidgetInfo)
        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        _binding!!.widgetsFragment.addView(hostView)
        _binding!!.widgetsFragment.invalidate();
    }

    override fun onStart() {
        super.onStart()
        _host!!.startListening();
    }

    override fun onStop() {
        super.onStop()
        _host!!.stopListening()
    }
}