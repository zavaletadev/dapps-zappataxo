package mx.edu.uteq.dapps.zappataxo.ui.catalogo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.edu.uteq.dapps.zappataxo.R;

public class EjemploRemotoAdapter extends BaseAdapter {

    /*
    Adaptador para recibir datos en formato
    JsonArray
     */
    private Context contexto;
    private JSONArray datos;
    private LayoutInflater inflater;

    public EjemploRemotoAdapter(Context context, JSONArray datos) {
        this.contexto = context;
        this.datos = datos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datos.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        Indicamod que por cada elemento del Arreglo json
        mostramos el layout del item
         */
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_ejemplo, null);
        }

        try {
            /*
            Por cada elementos del arreglo de productos tomamos al objeto
            json del producto actual
             */
            JSONObject producto = datos.getJSONObject(position);

            TextView tvEjemploNombreProd = convertView.findViewById(
                    R.id.tv_ejemplo_nombre_prod
            );
            tvEjemploNombreProd.setText(
                    producto.getString("nombre_prod")
            );

            TextView tvEjemploPrecioProd = convertView.findViewById(
                    R.id.tv_ejemplo_precio_prod
            );
            tvEjemploPrecioProd.setText(
                    "$"+
                    producto.getString("precio_prod") +
                    " MXN"
            );

            ImageView ivEjemploImg = convertView.findViewById(
                    R.id.iv_ejemplo_lista
            );
            Picasso.get().load(producto.getString("img1"))
                    .into(ivEjemploImg);

            TextView tvEjemploProductoId = convertView.findViewById(
                    R.id.tv_ejemplo_prod_id
            );
            tvEjemploProductoId.setText(
                    producto.getString("producto_id")
            );
        }

        catch(Exception e) {
            Log.e("Error remoto adapter", e.getMessage());
        }

        return convertView;
    }
}
