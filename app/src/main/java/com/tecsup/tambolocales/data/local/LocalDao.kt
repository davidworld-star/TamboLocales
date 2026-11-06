package com.tecsup.tambolocales.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(local: LocalEntity): Long

    @Query("SELECT * FROM locales ORDER BY id DESC")
    fun getAll(): LiveData<List<LocalEntity>>

    @Query("SELECT * FROM locales WHERE id = :id")
    fun getById(id: Long): LiveData<LocalEntity>
}
