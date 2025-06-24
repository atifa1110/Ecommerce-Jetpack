package com.example.core.data.local.room.entity.notification

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.local.room.Constants

import java.io.Serializable

@Entity(tableName = Constants.Tables.NOTIFICATION)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.Fields.ID)
    val id : Int? = null,

    @ColumnInfo(name = Constants.Fields.TITLE)
    val title: String,

    @ColumnInfo(name = Constants.Fields.BODY)
    val body: String,

    @ColumnInfo(name = Constants.Fields.IMAGE)
    val image: String,

    @ColumnInfo(name = Constants.Fields.TYPE)
    val type: String,

    @ColumnInfo(name = Constants.Fields.DATE)
    val date: String,

    @ColumnInfo(name = Constants.Fields.TIME)
    val time: String,

    @ColumnInfo(name = Constants.Fields.READ)
    val isRead : Boolean
) : Serializable

