package com.sduduzog.slimlauncher

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.sduduzog.slimlauncher.data.AppDao
import com.sduduzog.slimlauncher.data.BaseDatabase
import com.sduduzog.slimlauncher.data.WidgetDao
import com.sduduzog.slimlauncher.models.HomeApp
import com.sduduzog.slimlauncher.models.Widget
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@Config(sdk = [Build.VERSION_CODES.Q])
class BaseDatabaseTest {

    private lateinit var db: BaseDatabase
    private lateinit var appDao: AppDao
    private lateinit var widgetDao: WidgetDao

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            BaseDatabase::class.java
        )
            .addMigrations(
                BaseDatabase.MIGRATION_1_2,
                BaseDatabase.MIGRATION_2_3,
                BaseDatabase.MIGRATION_3_4,
                BaseDatabase.MIGRATION_4_5,
                BaseDatabase.MIGRATION_5_6,
                BaseDatabase.MIGRATION_6_7,
                BaseDatabase.MIGRATION_7_8,
                BaseDatabase.MIGRATION_8_9,
            )
            .allowMainThreadQueries()
            .build()
        appDao = db.appDao()
        widgetDao = db.widgetDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadHomeApp() {
        val app = HomeApp(
            "AppName",
            "package.name",
            "ActivityName",
            0,
            null, 12345
        )
        appDao.add(app)
        assertThat(LiveDataTestUtil.getValue(appDao.apps)).contains(app)
    }

    @Test
    fun writeAndReadWidget() {
        val widget = Widget(
                0,
                "coucou"
        )
        widgetDao.add(widget)
        assertThat(LiveDataTestUtil.getValue(widgetDao.widgets)).contains(widget)
    }
}