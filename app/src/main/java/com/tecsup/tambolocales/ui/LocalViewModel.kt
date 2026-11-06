package com.tecsup.tambolocales.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tecsup.tambolocales.data.local.AppDatabase
import com.tecsup.tambolocales.data.local.LocalEntity
import com.tecsup.tambolocales.data.repository.LocalRepository
import kotlinx.coroutines.launch

class LocalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocalRepository
    val allLocales: LiveData<List<LocalEntity>>

    init {
        val localDao = AppDatabase.getDatabase(application).localDao()
        repository = LocalRepository(localDao)
        allLocales = repository.allLocales
    }

    fun getLocalById(id: Long): LiveData<LocalEntity> {
        return repository.getLocalById(id)
    }

    fun insert(local: LocalEntity) = viewModelScope.launch {
        repository.insert(local)
    }
}
