package ipvc.estg.smartcity

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfo(context: Context) : GoogleMap.InfoWindowAdapter{

    var mContext = context
    var mapaWindow = (context as Activity).layoutInflater.inflate(R.layout.activity_marker_info, null)


    private fun janela(marker: Marker, view: View){
        val titulo = view.findViewById<TextView>(R.id.tituloMarker)
        val descricao = view.findViewById<TextView>(R.id.descMarker)
        val apagar = view.findViewById<Button>(R.id.apagar)
        val data = marker.snippet.split("+").toTypedArray()


        titulo.text = marker.title.take(20)+"..."
        descricao.text = data[0]




//        Picasso.get().load(data[5]).into(imagem);
//        imagem.getLayoutParams().height = 200;
//        imagem.getLayoutParams().width = 100;
//        imagem.requestLayout();
//
//        if(data[2].toInt() == data[3].toInt()){
//            user.text = data[4]
//            user.visibility = (View.VISIBLE)
//            layout.visibility = (View.VISIBLE)
//        }
//        else{
//                user.visibility = (View.GONE)
//                layout.visibility = (View.GONE) }
      }
    override fun getInfoWindow(marker: Marker): View {
        janela(marker, mapaWindow)
        return mapaWindow
    }
     override fun getInfoContents(marker: Marker): View? {
        janela(marker, mapaWindow)
        return mapaWindow
    }

   



}