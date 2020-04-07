package mx.edu.ittepic.ladm_u3_practica1_ricardovilla

import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main3.*


class Main3Activity : AppCompatActivity() {
    val nombreBaseDatos = "Tareas"
    var idAct = ""
    var listaID = ArrayList<String>()
    val arregloImagenes = ArrayList<ImageView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        setTitle("VER A DETALLE O ELIMINAR")
        var extrs = intent.extras
        idAct = extrs!!.getString("id")!!
        edit_desc2.setText(extrs!!.getString("Descripcion")!!)
        edit_fechaCaptura2.setText(extrs!!.getString("FechaCaptura")!!)
        edit_fechaEntrega2.setText(extrs!!.getString("FechaEntrega")!!)

        /*if(obtenerImgID(idAct)!=null) {
            val bitmap = getImage(obtenerImgID(idAct)!!)
            imageView2.setImageBitmap(bitmap)
        }
        else{
            mensaje("IMAGEN NO ENCONTRADA")
        }*/
        cargarImagenes(idAct)


        btn_eliminar.setOnClickListener {
            eliminarPorID(idAct)
        }

        btn_cancelar.setOnClickListener {
            finish()
        }









    }

    fun obtenerImgID(id:String):ByteArray?{

        var base =BaseDatos(this,nombreBaseDatos,null,1)
        var buscar = base.readableDatabase
        var columnas = arrayOf("Foto")
        var cursor = buscar.query("EVIDENCIAS",columnas,"Id_evidencia = ?",arrayOf(id.toString()),null,null,null)
        var result:ByteArray?=null
        if (cursor.moveToFirst()){
            do{
                result=cursor.getBlob(cursor.getColumnIndex("Foto"))
            }while (cursor.moveToNext())
        }//if
        return result


    }

    fun cargarImagenes(id:String){

        listaID = ArrayList<String>()
        try {
            var baseDatos = BaseDatos(this,nombreBaseDatos,null,1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM EVIDENCIAS WHERE Id_actividad = ?"

            var parametros = arrayOf(id)
            var cursor = select.rawQuery(SQL,parametros)
            if(cursor.count>0){
                var bit : Bitmap?=null
                //var arreglo = arrayOf<Bitmap>(getImage(obtenerImgID(idAct)!!))
                var arreglo = ArrayList<Bitmap>()



                this.listaID = ArrayList<String>()
                cursor.moveToFirst()

                var cantidad = cursor.count-1

                (0..cantidad).forEach {



                    arreglo.add(getImage(obtenerImgID(cursor.getString(0))!!))



                    listaID.add(cursor.getString(0))
                    cursor.moveToNext()

                }
                //CONVERTIR ARRAY LIST A ARRAY
                val array = arrayOfNulls<Bitmap>(arreglo.size)
                val miAdaptador = AdaptadorImagenes(this,arreglo.toArray(array))
                listaImagenes.adapter = miAdaptador

                listaImagenes.setOnItemClickListener { parent, view, position, id ->
                    AlertDialog.Builder(this)
                        .setTitle("ATENCION")
                        .setMessage("¿Seguro qué desea eliminar esta EVIDENCIA?")
                        .setPositiveButton("Eliminar"){d,i->
                            eliminarEvidenciaPorID(listaID[position])
                            cargarImagenes(idAct)
                        }

                        .setNegativeButton("Cancelar"){d,i->}
                        .show()
                }

            }

            else{
                mensaje("NO SE ENCONTRO COINCIDENCIA CON IMAGENES")
            }

            select.close()
            baseDatos.close()



        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }
    }



    fun eliminarPorID(id:String){
        try{
            var baseDatos = BaseDatos(this,nombreBaseDatos,null,1)

            var insertar = baseDatos.writableDatabase
            var SQL =""
            SQL = "DELETE FROM EVIDENCIAS WHERE Id_actividad=?"
            var parametros = arrayOf(id)
            insertar.execSQL(SQL,parametros)
            SQL = "DELETE FROM ACTIVIDADES WHERE Id_actividad=?"
            insertar.execSQL(SQL,parametros)


            mensaje("SE ELIMINO CORRECTAMENTE")
            insertar.close()
            baseDatos.close()


        }catch (error:SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun eliminarEvidenciaPorID(id:String){
        try{
            var baseDatos = BaseDatos(this,nombreBaseDatos,null,1)

            var insertar = baseDatos.writableDatabase
            var  SQL = "DELETE FROM EVIDENCIAS WHERE Id_evidencia=?"
            var parametros = arrayOf(id)

            insertar.execSQL(SQL,parametros)


            mensaje("SE ELIMINO CORRECTAMENTE")
            insertar.close()
            baseDatos.close()


        }catch (error:SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(mensaje:String){
        AlertDialog.Builder(this)
            .setMessage(mensaje)
            .show()
    }

    fun getImage(image:ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image,0,image.size)
    }
}
