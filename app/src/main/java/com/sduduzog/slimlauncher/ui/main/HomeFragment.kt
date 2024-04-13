package com.sduduzog.slimlauncher.ui.main

import android.content.*
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.Process
import android.os.UserManager
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.sduduzog.slimlauncher.BuildConfig
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.adapters.HomeAdapter
import com.sduduzog.slimlauncher.data.model.App
import com.sduduzog.slimlauncher.databinding.HomeFragmentBinding
import com.sduduzog.slimlauncher.models.HomeApp
import com.sduduzog.slimlauncher.models.MainViewModel
import com.sduduzog.slimlauncher.utils.BaseFragment
import com.sduduzog.slimlauncher.utils.OnLaunchAppListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment(), OnLaunchAppListener {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var receiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setInstalledApps(getInstalledApps());

        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val adapter1 = HomeAdapter(this)
        val adapter2 = HomeAdapter(this)
        binding!!.homeFragmentList.adapter = adapter1
        binding!!.homeFragmentListExp.adapter = adapter2

        viewModel.apps.observe(viewLifecycleOwner) { list ->
            list?.let { apps ->
                adapter1.setItems(apps)
            }
        }

        adapter2.setItems(viewModel.installedApps);

        setEventListeners()
        binding!!.homeFragmentOptions.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_optionsFragment))
        return binding?.root
    }

    private fun getInstalledApps(): List<App> {
        val list = mutableListOf<App>()

        val manager = requireContext().getSystemService(Context.USER_SERVICE) as UserManager
        val launcher = requireContext().getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        val myUserHandle = Process.myUserHandle()

        for (profile in manager.userProfiles) {
            val prefix = if (profile.equals(myUserHandle)) "" else "\uD83C\uDD46 " //Unicode for boxed w
            val profileSerial = manager.getSerialNumberForUser(profile)

            for (activityInfo in launcher.getActivityList(null, profile)) {
                val app = App(
                        appName = prefix + activityInfo.label.toString(),
                        packageName = activityInfo.applicationInfo.packageName,
                        activityName = activityInfo.name,
                        userSerial = profileSerial
                )
                list.add(app)
            }
        }

        list.sortBy{it.appName}

        val filter = mutableListOf<String>()
        filter.add(BuildConfig.APPLICATION_ID)
        return list.filterNot { filter.contains(it.packageName) }
    }

    override fun onStart() {
        super.onStart()
        receiver = ClockReceiver()
        activity?.registerReceiver(receiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun getFragmentView(): ViewGroup = binding!!.root

    override fun onResume() {
        super.onResume()
        updateClock()
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(receiver)
    }

    private fun setEventListeners() {

        binding!!.homeFragmentTime.setOnClickListener {
            try {
                val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                launchActivity(it, intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                // Do nothing, we've failed :(
            }
        }

        binding!!.homeFragmentDate.setOnClickListener {
            try {
                val builder = CalendarContract.CONTENT_URI.buildUpon().appendPath("time")
                val intent = Intent(Intent.ACTION_VIEW, builder.build())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                launchActivity(it, intent)
            } catch (e: ActivityNotFoundException) {
                // Do nothing, we've failed :(
            }
        }

        binding!!.homeFragmentCall .setOnClickListener { view ->
            try {
                val pm = context?.packageManager!!
                val intent = Intent(Intent.ACTION_DIAL)
                val componentName = intent.resolveActivity(pm)
                if (componentName == null) launchActivity(view, intent) else
                    pm.getLaunchIntentForPackage(componentName.packageName)?.let {
                        launchActivity(view, it)
                    } ?: run { launchActivity(view, intent) }
            } catch (e: Exception) {
                // Do nothing
            }
        }

        binding!!.homeFragmentCamera .setOnClickListener {
            try {
                val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
                launchActivity(it, intent)
            } catch (e: Exception) {
                // Do nothing
            }
        }
    }

    fun updateClock() {
        val active =
            context?.getSharedPreferences(getString(R.string.prefs_settings), Context.MODE_PRIVATE)
                ?.getInt(getString(R.string.prefs_settings_key_time_format), 0)
        val date = Date()

        val fWatchTime = when (active) {
            1 -> SimpleDateFormat("H:mm", Locale.ROOT)
            2 -> SimpleDateFormat("h:mm aa", Locale.ROOT)
            else -> DateFormat.getTimeInstance(DateFormat.SHORT)
        }
        binding!!.homeFragmentTime .text = fWatchTime.format(date)


        val fWatchDate = SimpleDateFormat("EEE, MMM dd", Locale.ROOT)
        binding!!.homeFragmentDate .text = fWatchDate.format(date)
    }

    override fun onLaunch(app: HomeApp, view: View) {
        try {
            val manager = requireContext().getSystemService(Context.USER_SERVICE) as UserManager
            val launcher =
                requireContext().getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

            val componentName = ComponentName(app.packageName, app.activityName)
            val userHandle = manager.getUserForSerialNumber(app.userSerial)

            launcher.startMainActivity(componentName, userHandle, view.clipBounds, null)
        } catch (e: Exception) {
            // Do no shit yet
        }
    }

    override fun onBack(): Boolean {
        binding!!.root.transitionToStart()
        return true
    }

    override fun onHome() {
        binding!!.root.transitionToEnd()
    }

    inner class ClockReceiver : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            updateClock()
        }
    }
}

