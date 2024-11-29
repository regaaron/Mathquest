package com.example.proyecto

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context,"progeso.db",null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val ordenCrear = "CREATE TABLE progreso (" +
                "id INTEGER PRIMARY KEY," +  // El id será manual, no AUTOINCREMENT
                "lvl1 INTEGER," +
                "lvl2 INTEGER," +
                "lvl3 INTEGER," +
                "lvl4 INTEGER," +
                "lvl5 INTEGER)"
        db!!.execSQL(ordenCrear)

        // Inserción de registros iniciales para cada mundo
        //1 = SUMA, 2 = RESTA, 3 = MULTIPLICACIÓN, 4 = DIVISIÓN
        val insercion1 = "INSERT INTO progreso (id, lvl1, lvl2, lvl3, lvl4, lvl5) VALUES (1, 0, NULL, NULL, NULL, NULL)" // Montaña
        val insercion2 = "INSERT INTO progreso (id, lvl1, lvl2, lvl3, lvl4, lvl5) VALUES (2, 0, NULL, NULL, NULL, NULL)" // Desierto
        val insercion3 = "INSERT INTO progreso (id, lvl1, lvl2, lvl3, lvl4, lvl5) VALUES (3, 0, NULL, NULL, NULL, NULL)" // Volcán
        val insercion4 = "INSERT INTO progreso (id, lvl1, lvl2, lvl3, lvl4, lvl5) VALUES (4, 0, NULL, NULL, NULL, NULL)" // Templo

        // Ejecutar las inserciones
                db.execSQL(insercion1)
                db.execSQL(insercion2)
                db.execSQL(insercion3)
                db.execSQL(insercion4)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val ordenEliminacion = "DROP TABLE IF EXISTS progreso"
        db!!.execSQL(ordenEliminacion)
        onCreate(db)
    }

    fun modificarNivel(mundo: Int, nivel: Int, puntaje: Int) {
        println("Estoy en Modifica nivel")
        println("mundo: ${mundo} , nivel: ${nivel}, puntaje: ${puntaje}")
        val db = this.writableDatabase
        val contentValues = ContentValues()

        // Actualizar puntaje del nivel
        contentValues.put("lvl$nivel", puntaje)
        db.update("progreso", contentValues, "id = ?", arrayOf(mundo.toString()))
        db.close() // Importante cerrar la conexión
    }


    fun desbloquearNivel(id: Int, nivel: Int){
        Log.d("desbloquearNivel", "Entre a desbloquearNivel: ")
        Log.d("desbloquearNivel", "id: $id, nivel: $nivel")

       val db = this.writableDatabase
        val orderDesbloquear = "UPDATE progreso SET lvl$nivel = 0 WHERE id = $id"
        db.execSQL(orderDesbloquear)
        db.close()
    }

    fun obtenerPuntajeNivel(mundo: Int, nivel: Int): Int? {
        println("Estoy en obtenerPuntajeNivel")
        println("mundo: ${mundo} , nivel: ${nivel}")
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT lvl$nivel FROM progreso WHERE id = ?", arrayOf(mundo.toString()))
        var resultado: Int? = null

        if (cursor.moveToFirst()) {
            resultado = if (cursor.isNull(0)) null else cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return resultado
    }



}