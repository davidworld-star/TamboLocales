package com.tecsup.tambolocales.data.repository

import androidx.lifecycle.LiveData
import com.tecsup.tambolocales.data.local.LocalDao
import com.tecsup.tambolocales.data.local.LocalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepository(private val localDao: LocalDao) {

    val allLocales: LiveData<List<LocalEntity>> = localDao.getAll()

    fun getLocalById(id: Long): LiveData<LocalEntity> {
        return localDao.getById(id)
    }

    suspend fun insert(local: LocalEntity): Long = withContext(Dispatchers.IO) {
        localDao.insert(local)
    }
}
