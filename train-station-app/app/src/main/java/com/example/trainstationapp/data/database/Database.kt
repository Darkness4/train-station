package com.example.trainstationapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trainstationapp.data.database.converters.ListConverters
import com.example.trainstationapp.data.models.RemoteKeys
import com.example.trainstationapp.data.models.StationModel

@Database(entities = [StationModel::class, RemoteKeys::class], version = 1)
@TypeConverters(ListConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
