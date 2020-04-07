package mx.edu.ittepic.ladm_u3_practica1_ricardovilla

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class Actividad(desc:String,fechaC:String,fechaE:String) {
    var descripcion=desc
    var fechaCaptura = fechaC
    var fechaEntrega = fechaE
    var id=0
    var error = -1

    /*
        valores de error
        -----------------

        1 = error en tabla, no se creó o no se conectó base datos
        2 = error no se pudo insertar
        3 = NO SE PUDO REALIZAR CONSULTA / TABLA VACÍA
        4 = No se encontró ID
        5 = NO ACTUALIZO
        6 = NO BORRO
     */

    val nombreBaseDatos = "Tareas"
    var puntero : Context?= null


    fun asignarPuntero(p:Context){
        puntero = p
    }


    fun insertar():Boolean{
        error = -1
        try{
            var base = BaseDatos(puntero,nombreBaseDatos,null,1)
            var insertar = base.writableDatabase
            var datos = ContentValues()

            //OBTENER LOS DATOS DE LA TABLA
            datos.put("Descripcion",descripcion)
            datos.put("FechaCaptura",fechaCaptura)
            datos.put("FechaEntrega",fechaEntrega)

            //EL insert(TABLA A INSERTAR, VALORES QUE ESTARÁN EN NULL, DATOS A INSERTAR
            var respuesta = insertar.insert("ACTIVIDADES","Id_actividad",datos)

            if(respuesta.toInt()==-1){
                error = 2
                return false
            }


        }catch (e: SQLiteException){
            error = 1
            return false
        }


        return true
    }

    fun mostrarTodos():ArrayList<Actividad>{

        error = -1
        var data = ArrayList<Actividad>()

        try{

            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1)
            var select = base.readableDatabase
            //El asterisco es para mostrar todas
            var columnas = arrayOf("*")

            var cursor = select.query("ACTIVIDADES",columnas,null,null,null,null,null)

            if(cursor.moveToFirst()){

                do{
                    var actividadTemporal = Actividad(cursor.getString(1),cursor.getString(2),cursor.getString(3))
                    actividadTemporal.id = cursor.getInt(0)
                    data.add(actividadTemporal)

                }while(cursor.moveToNext())
            }
            else{
                error = 3
            }

        }catch (e:SQLiteException){

            error = 1
        }
        return data
    }

    fun buscar(id:String):Actividad{
        var actividadEncontrada = Actividad("-1","-1","-1")

        error = -1
        try{

            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1)
            var select = base.readableDatabase
            var columnas = arrayOf("*")
            var idBuscar = arrayOf(id)

            var cursor = select.query("ACTIVIDADES",columnas,"Id_actividad = ?",idBuscar,null,null,null)

            if (cursor.moveToFirst()){

                actividadEncontrada.id = id.toInt()
                actividadEncontrada.descripcion = cursor.getString(1)
                actividadEncontrada.fechaCaptura = cursor.getString(2)
                actividadEncontrada.fechaEntrega = cursor.getString(3)

            }
            else{
                error = 4
            }

        }catch (e:SQLiteException){
            error = 1
        }

        return actividadEncontrada
    }


    fun actualizar():Boolean{
        error = -1
        try{
            var base = BaseDatos(puntero,nombreBaseDatos,null,1)
            var actualizar = base.writableDatabase
            var datos = ContentValues()
            var idActualizar = arrayOf(id.toString())

            //OBTENER LOS DATOS DE LA TABLA
            datos.put("Descripcion",descripcion)
            datos.put("FechaCaptura",fechaCaptura)
            datos.put("FechaEntrega",fechaEntrega)

            //REGRESA UN 0 SI NO ACTUALIZO
            var respuesta = actualizar.update("ACTIVIDADES",datos,"Id_actividad = ?",idActualizar)

            if(respuesta.toInt()== 0){
                error = 5
                return false
            }


        }catch (e:SQLiteException){
            error = 1
            return false
        }


        return true
    }
    fun eliminar():Boolean{
        error = -1
        try{
            var base = BaseDatos(puntero,nombreBaseDatos,null,1)
            var eliminar = base.writableDatabase
            var idEliminar = arrayOf(id.toString())

            var respuesta = eliminar.delete("ACTIVIDADES","Id_actividad = ?",idEliminar)


            if(respuesta.toInt()==0){
                error = 6
                return false
            }


        }catch (e:SQLiteException){
            error = 1
            return false
        }


        return true
    }


}