package mx.edu.ittepic.ladm_u3_practica1_ricardovilla

import android.R
import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class AdaptadorImagenes(private val context: Activity, private val imgid: Array<Bitmap>)
    : ArrayAdapter<Bitmap>(context, mx.edu.ittepic.ladm_u3_practica1_ricardovilla.R.layout.imagenes,imgid) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(mx.edu.ittepic.ladm_u3_practica1_ricardovilla.R.layout.imagenes, null, true)


        val imageView = rowView.findViewById(mx.edu.ittepic.ladm_u3_practica1_ricardovilla.R.id.imageView4) as ImageView
        imageView.setImageBitmap(imgid[position])
        return rowView
    }
}