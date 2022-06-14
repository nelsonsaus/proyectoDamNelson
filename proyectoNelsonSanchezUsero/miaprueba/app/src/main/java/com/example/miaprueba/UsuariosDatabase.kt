package com.example.miaprueba

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuarios::class], version = 1)
abstract class UsuariosDatabase : RoomDatabase() {
    abstract fun usuariosDao(): UsuariosDao

    companion object {
        @Volatile
        private var INSTANCE: UsuariosDatabase? = null
        fun getInstance(context: Context): UsuariosDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UsuariosDatabase::class.java,
                        "libros"
                    ).build()
                }
                return instance
            }
        }
    }
}