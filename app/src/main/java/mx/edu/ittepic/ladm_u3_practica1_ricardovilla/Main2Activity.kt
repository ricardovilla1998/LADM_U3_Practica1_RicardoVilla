package mx.edu.ittepic.ladm_u3_practica1_ricardovilla

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    val nombreBaseDatos = "Tareas"
    var listaID = java.util.ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setTitle("BUSCAR")

       cargarLista()


        button.setOnClickListener {

          cargarLista(edit_buscar.text.toString())
        }

        button2.setOnClickListener {
            cargarLista()
        }
    }

    override fun onStart() {
        super.onStart()
        cargarLista()
    }

    fun cargarLista(){
        listaID = ArrayList<String>()
        try {
            var baseDatos = BaseDatos(this,nombreBaseDatos,null,1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM ACTIVIDADES"
            var SQL2 = "SELECT * FROM EVIDENCIAS"


            var cursor = select.rawQuery(SQL,null)
            var cursor2 = select.rawQuery(SQL2,null)
            if(cursor.count>0){
                var arreglo = ArrayList<String>()
                this.listaID = ArrayList<String>()
                cursor.moveToFirst()
                cursor2.moveToFirst()
                var cantidad = cursor.count-1

                (0..cantidad).forEach {
                    var data = "Id: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}\nFecha entrega: ${cursor.getString(3)}"
                    arreglo.add(data)
                    listaID.add(cursor.getString(0))
                    cursor.moveToNext()
                    cursor2.moveToNext()
                }

                lista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arreglo)
                lista.setOnItemClickListener { parent, view, position, id ->
                    AlertDialog.Builder(this)
                        .setTitle("ATENCION")
                        .setMessage("¿Que deseas hacer con ese ITEM?")
                        .setPositiveButton("Verlo a detalle"){d,i->
                            cargarEnOtroActivity(listaID[position].toInt())
                        }

                        .setNegativeButton("Cancelar"){d,i->}
                        .show()
                }

            }

            select.close()
            baseDatos.close()



        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun cargarLista(parametro:String){
        listaID = ArrayList<String>()
        try {
            var baseDatos = BaseDatos(this,nombreBaseDatos,null,1)
            var select = baseDatos.readableDatabase
            var SQL = ""

            if(radioButton_entrega.isChecked){
                SQL = "SELECT * FROM ACTIVIDADES WHERE FechaEntrega = ?"
            }
            if(radioButton_captura.isChecked){
                SQL = "SELECT * FROM ACTIVIDADES WHERE FechaCaptura = ?"
            }

            if(radioButton_desc.isChecked){
                SQL = "SELECT * FROM ACTIVIDADES WHERE Descripcion = ?"
            }

            if(radioButton_id.isChecked){
                SQL = "SELECT * FROM ACTIVIDADES WHERE Id_actividad = ?"
            }
            //var SQL2 = "SELECT * FROM EVIDENCIAS INNER JOIN ACTIVIDADES ON ACTIVIDADES.Id_actividad = EVIDENCIAS.id_actividad WHERE ACTIVIDADES.fechaentrega = '20/04/2'"

            var parametros = arrayOf(parametro)
            //var parametros2 = arrayOf(fechaEntrega)
            var cursor = select.rawQuery(SQL,parametros)
            // var cursor2 = select.rawQuery(SQL2,null)
            if(cursor.count>0){
                var arreglo = ArrayList<String>()
                this.listaID = ArrayList<String>()
                cursor.moveToFirst()
                // cursor2.moveToFirst()
                var cantidad = cursor.count-1

                (0..cantidad).forEach {
                    var data = "Descripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}\nFecha entrega: ${cursor.getString(3)}"
                    arreglo.add(data)
                    listaID.add(cursor.getString(0))
                    cursor.moveToNext()
                    //cursor2.moveToNext()
                }

                lista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arreglo)
                lista.setOnItemClickListener { parent, view, position, id ->
                    AlertDialog.Builder(this)
                        .setTitle("ATENCION")
                        .setMessage("¿Que deseas hacer con ese ITEM?")
                        .setPositiveButton("Verlo a detalle"){d,i->
                            cargarEnOtroActivity(listaID[position].toInt())
                        }

                        .setNegativeButton("Cancelar"){d,i->}
                        .show()
                }

            }
            else{
                mensaje("NO SE ENCONTRO COINCIDENCIA")
                cargarLista()

                return

            }

            select.close()
            baseDatos.close()



        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }
    }


    fun cargarListaFechas(fechaEntrega:String){

    }


    fun cargarListaFechasCap(fechaEntrega:String){
        listaID = ArrayList<String>()

    }

    private fun cargarEnOtroActivity(id: Int) {

        var otroActivity = Intent(this,Main3Activity::class.java)
        var conexion = Actividad("","","")
        conexion.asignarPuntero(this)
        var actividadEncontrado = conexion.buscar(id.toString())
        otroActivity.putExtra("id",id.toString())
        otroActivity.putExtra("Descripcion",actividadEncontrado.descripcion)
        otroActivity.putExtra("FechaCaptura",actividadEncontrado.fechaCaptura)
        otroActivity.putExtra("FechaEntrega",actividadEncontrado.fechaEntrega)
        startActivity(otroActivity)
    }
    fun mensaje(mensaje:String){
        AlertDialog.Builder(this)
            .setMessage(mensaje)
            .show()
    }
}
