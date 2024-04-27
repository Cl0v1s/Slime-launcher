package com.sduduzog.slimlauncher.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sduduzog.slimlauncher.models.Widget

@Dao
interface WidgetDao {

    @get:Query("SELECT * FROM widgets ORDER BY id ASC")
    val widgets: LiveData<List<Widget>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(widget: Widget)

    @Update
    fun update(widget: Widget)

    @Delete
    fun remove(widget:Widget)

    @Query("DELETE FROM widgets")
    fun purge();
}