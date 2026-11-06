package com.tecsup.tambolocales.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LocalEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun localDao(): LocalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tambo_locales_db"
                )
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database.localDao())
                }
            }
        }

        private suspend fun populateInitialData(dao: LocalDao) {
            dao.insert(
                LocalEntity(
                    nombre = "TAMBO YUPANQUI - JESUS MARIA",
                    direccion = "Jirón Lloque Yupanqui 1912, Jesús María, Perú",
                    gerente = "Jose Lopez Garcia",
                    diasAtencion = "Lunes a domingo",
                    horario = "09:00 - 22:00",
                    latitud = -12.0705,
                    longitud = -77.0510
                )
            )
            dao.insert(
                LocalEntity(
                    nombre = "TAMBO AREQUIPA - LINCE",
                    direccion = "Av. Arequipa 1950, Lince, Perú",
                    gerente = "Maria Fernandez Silva",
                    diasAtencion = "Lunes a domingo",
                    horario = "07:00 - 23:00",
                    latitud = -12.0812,
                    longitud = -77.0345
                )
            )
            dao.insert(
                LocalEntity(
                    nombre = "TAMBO LARCO - MIRAFLORES",
                    direccion = "Av. José Larco 610, Miraflores, Perú",
                    gerente = "Carlos Mendoza Ruiz",
                    diasAtencion = "24 horas",
                    horario = "00:00 - 23:59",
                    latitud = -12.1220,
                    longitud = -77.0305
                )
            )
        }
    }
}
