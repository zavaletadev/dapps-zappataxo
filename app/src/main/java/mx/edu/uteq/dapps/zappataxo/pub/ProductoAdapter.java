package mx.edu.uteq.dapps.zappataxo.pub;

import android.content.Context;
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

public class ProductoAdapter extends BaseAdapter {

    private Context contexto;
    private JSONArray datos;
    private LayoutInflater inflater;

    public ProductoAdapter(Context contexto, JSONArray datos) {
        this.contexto = contexto;
        this.datos = datos;
        inflater = LayoutInflater.from(contexto);
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_producto, null);
        }

        try {
            JSONObject producto = datos.getJSONObject(position);

            ImageView ivImg1 = convertView.findViewById(R.id.iv_img1);
            Picasso.get().load(producto.getString("img1")).into(ivImg1);

            TextView tvNombreProd = convertView.findViewById(
                    R.id.tv_nombre_prod
            );
            tvNombreProd.setText(
                    producto.getString("nombre_prod")
            );

            TextView tvPrecioProd = convertView.findViewById(
                    R.id.tv_precio_prod
            );
            tvPrecioProd.setText(
                    "$" +
                    producto.getString("precio_prod") +
                    " MXN"
            );

            TextView tvNumTallas = convertView.findViewById(
                    R.id.tv_num_tallas
            );
            tvNumTallas.setText(
                    producto.getJSONArray("tallas").length() +
                    " tallas disponibles"
            );


            ImageView ivMujer = convertView.findViewById(
                    R.id.iv_cat_mujer
            );
            ImageView ivHombre = convertView.findViewById(
                    R.id.iv_cat_hombre
            );
            ivMujer.setImageResource(R.drawable.transparente);
            ivHombre.setImageResource(R.drawable.transparente);

            int categoria = producto.getInt("categoria");
            if (categoria == 1) {
                ivMujer.setImageResource(R.drawable.ic_woman);
                ivHombre.setImageResource(R.drawable.transparente);
            }

            else if (categoria == 2) {
                ivMujer.setImageResource(R.drawable.transparente);
                ivHombre.setImageResource(R.drawable.ic_man);
            }

            else if (categoria == 3) {
                ivMujer.setImageResource(R.drawable.ic_woman);
                ivHombre.setImageResource(R.drawable.ic_man);
            }
        }
        catch(Exception e) {
            e.getMessage();
        }

        return convertView;
    }
}
