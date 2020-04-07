package mx.edu.ittepic.ladm_u3_practica1_ricardovilla

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE ACTIVIDADES(Id_actividad INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Descripcion VARCHAR(200), FechaCaptura DATE, FechaEntrega DATE)")

        db?.execSQL("CREATE TABLE EVIDENCIAS(Id_evidencia INTEGER " +
                "PRIMARY KEY AUTOINCREMENT, Id_actividad INTEGER, " +
                "Foto BLOB, FOREIGN KEY(Id_actividad) REFERENCES ACTIVIDADES (Id_actividad))")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}