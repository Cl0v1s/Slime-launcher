package com.sduduzog.slimlauncher.models

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.os.Parcel
import android.util.Base64
import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.ByteArrayOutputStream
import java.io.IOException

@Entity(tableName = "widgets", primaryKeys = ["id"])
data class Widget(
    @field:ColumnInfo(name = "id")
    val id: Int,
    @field:ColumnInfo(name = "extras")
    val extras: String
) {

    fun load(): Bundle? {
        val parcel = Parcel.obtain()
        try {
            val data = Base64.decode(extras, 0)
            parcel.unmarshall(data, 0, data.size)
            parcel.setDataPosition(0)
            return parcel.readBundle()
        } finally {
            parcel.recycle()
        }
    }

    companion object {
        fun from(bundle: Bundle): Widget {
            val id = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            val parcel = Parcel.obtain()
            val serialized: String?
            try {
                bundle.writeToParcel(parcel, 0)
                val bos = ByteArrayOutputStream()
                bos.write(parcel.marshall())
                serialized = Base64.encodeToString(bos.toByteArray(), 0)
            } finally {
                parcel.recycle()
            }
            if(serialized != null) {
                return Widget(id, serialized)
            }
            throw IOException("Unable to serialize Widget")
        }
    }
}