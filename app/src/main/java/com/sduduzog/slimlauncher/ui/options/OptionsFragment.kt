package com.sduduzog.slimlauncher.ui.options

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.sduduzog.slimlauncher.App
import com.sduduzog.slimlauncher.MainActivity
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.databinding.OptionsFragmentBinding
import com.sduduzog.slimlauncher.models.Widget
import com.sduduzog.slimlauncher.models.OptionsViewModel
import com.sduduzog.slimlauncher.ui.dialogs.ChangeThemeDialog
import com.sduduzog.slimlauncher.ui.dialogs.ChooseTimeFormatDialog
import com.sduduzog.slimlauncher.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import android.widget.Toast

@AndroidEntryPoint
class OptionsFragment : BaseFragment() {

    private var _binding: OptionsFragmentBinding? = null
    private  val _viewModel: OptionsViewModel by viewModels()
    private var _host: AppWidgetHost? = null;
    private var _manager: AppWidgetManager? = null;

    private val binding get() = _binding
    override fun getFragmentView(): ViewGroup = binding!!.root

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OptionsFragmentBinding.inflate(inflater, container, false)

        _host = ((context!!.applicationContext as App).currentActivity as MainActivity).widgetHost
        _manager = ((context!!.applicationContext as App).currentActivity as MainActivity).widgetManager

        binding!!.optionsFragmentAddWidget.setOnClickListener {
            selectWidget()
        }

        binding!!.optionsFragmentPurgeWidgets.setOnClickListener {
            purgeWidgets()
        }

        binding!!.optionsFragmentDeviceSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_SETTINGS)
            launchActivity(it, intent)
        }
        binding!!.optionsFragmentDeviceSettings.setOnLongClickListener {
            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
            launchActivity(it, intent)
            true
        }
        binding!!.optionsFragmentChangeTheme.setOnClickListener {
            val changeThemeDialog = ChangeThemeDialog.getThemeChooser()
            changeThemeDialog.showNow(childFragmentManager, "THEME_CHOOSER")
        }
        binding!!.optionsFragmentChooseTimeFormat.setOnClickListener {
            val chooseTimeFormatDialog = ChooseTimeFormatDialog.getInstance()
            chooseTimeFormatDialog.showNow(childFragmentManager, "TIME_FORMAT_CHOOSER")
        }
        binding!!.optionsFragmentToggleStatusBar.setOnClickListener {
            val settings = requireContext().getSharedPreferences(
                getString(R.string.prefs_settings),
                MODE_PRIVATE
            )
            val isHidden =
                settings.getBoolean(getString(R.string.prefs_settings_key_toggle_status_bar), false)
            settings.edit {
                putBoolean(getString(R.string.prefs_settings_key_toggle_status_bar), !isHidden)
            }
        }
        binding!!.optionsFragmentCustomiseApps.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_optionsFragment_to_customiseAppsFragment
            )
        )
        return binding?.root
    }

    fun purgeWidgets() {
        _viewModel.purgeWidgets(_host!!)
        val toast = Toast.makeText(context, R.string.options_fragment_purged_widgets, Toast.LENGTH_SHORT)
        toast.show()
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == R.id.RequestPickAppWidget) {
                configureWidget(data)
            } else if (requestCode == R.id.RequestCreateAppWidget) {
                createWidget(data!!)
            }
        } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
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
        _viewModel.addWidget(Widget.from(extras!!))
        val toast = Toast.makeText(context, R.string.options_fragment_added_widget, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}