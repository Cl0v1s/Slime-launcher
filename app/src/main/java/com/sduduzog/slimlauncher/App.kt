package com.sduduzog.slimlauncher

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    public var currentActivity: Activity? = null;

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecyleListener())

    }

    inner class ActivityLifecyleListener: ActivityLifecycleCallbacks {
        override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            currentActivity = p0;
        }

        override fun onActivityStarted(p0: Activity) {
            currentActivity = p0;
        }

        override fun onActivityResumed(p0: Activity) {
            currentActivity = p0;
        }

        override fun onActivityPaused(p0: Activity) {
        }

        override fun onActivityStopped(p0: Activity) {
        }

        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        }

        override fun onActivityDestroyed(p0: Activity) {
        }

    }
}