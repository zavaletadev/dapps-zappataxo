package mx.edu.uteq.dapps.zappataxo.ui.carrito;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.edu.uteq.dapps.zappataxo.R;

public class CarritoAdapter extends BaseAdapter {

    private Context contexto;
    private LayoutInflater inflater;
    private JSONArray datos;
    private String total;
    private String md5IdUsuario;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;
    private int cantidadProd;

    public CarritoAdapter(Context contexto, JSONArray datos, String total, String md5IdUsuario) {
        this.contexto = contexto;
        this.datos = datos;
        this.total = total;
        this.md5IdUsuario = md5IdUsuario;
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

        convertView = inflater.inflate(R.layout.item_carrito, null);

        try {

            JSONObject productoCarrito = datos.getJSONObject(position);

            TextView tvCarritoId = convertView.findViewById(R.id.tv_carrito_id);
            tvCarritoId.setText(
                    productoCarrito.getString("carrito_id")
            );

            ImageView ivCarritoImagen = convertView.findViewById(R.id.iv_carrito_img_prod);
            Picasso.get().load(productoCarrito.getString("img1")).into(ivCarritoImagen);

            TextView tvCarritoNombreProd = convertView.findViewById(R.id.tv_carrito_nombre_prod);
            tvCarritoNombreProd.setText(
                    productoCarrito.getString("nombre_prod")
            );

            TextView tvCarritoPrecioProd = convertView.findViewById(R.id.tv_carrito_precio_prod);
            tvCarritoPrecioProd.setText(
                    "$" +
                    productoCarrito.getString("precio_prod") +
                    ".00 MXN"
            );

            TextView tvCarritoCantidadProd = convertView.findViewById(R.id.tv_carrito_cantidad_prod);
            tvCarritoCantidadProd.setText(
                    productoCarrito.getString("cantidad")
            );

            TextView tvCarritoTallaProd = convertView.findViewById(R.id.tv_carrito_talla_prod);
            tvCarritoTallaProd.setText(
                    "Talla: " +
                    productoCarrito.getString("talla_prod")
            );


            //*Calculamos el subtotal
            double subtotal = productoCarrito.getInt("cantidad")
                    *
            productoCarrito.getDouble("precio_prod");


            TextView tvCarritoSubtotalProd = convertView.findViewById(R.id.tv_carrito_subtotal_prod);
            tvCarritoSubtotalProd.setText(
                    "Subtotal: $" +
                    String.valueOf(subtotal) +
                    " MXN"
            );

            /*
            Si me encuentro en la ultima posición del arreglo de productos
            a partir del id del carrito
            del carrito, mostramos el total
             */
            Log.d("datos.lenght -1", (datos.length() -1)+"");
            Log.d("position", position+"");
            LinearLayout llCarritoTotalProd = convertView.findViewById(R.id.ll_carrito_total_prod);

            TextView tvCarritoTotalProd = convertView.findViewById(
                    R.id.tv_carrito_total_prod
            );

            if (datos.length() - 1 == position) {
                llCarritoTotalProd.setVisibility(View.VISIBLE);
                tvCarritoTotalProd.setText(total);
            }


            /*
            Click eliminar
             */
            Button btnEliminar = convertView.findViewById(
                    R.id.btn_elimina_carrito
            );
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProgressDialog progress;
                    progress = new ProgressDialog(contexto);
                    progress.setTitle("Eliminando producto");
                    progress.setMessage("Por favor espera...");
                    progress.setCancelable(false);
                    progress.setIndeterminate(true);
                    progress.show();

                    conexionServ = Volley.newRequestQueue(contexto);
                    peticionServ = new StringRequest(
                            Request.Method.POST,
                            "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/carrito/eliminar",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject objRespuesta = new JSONObject(response);

                                        if (objRespuesta.getInt("code") == 200) {
                                            //Si se eliminó, actualizamos el arreglo json
                                            datos = objRespuesta.getJSONArray("data");
                                            //Actualizamos al adaptador
                                            notifyDataSetChanged();

                                            Log.d("TOTAL", objRespuesta.getString("total"));

                                            //Actualizamos el total
                                            total = objRespuesta.getString("total");
                                            tvCarritoTotalProd.setText(
                                                    "Total: " +
                                                    total
                                            );
                                        }

                                    }

                                    catch(Exception e) {
                                        Log.e("ex. adaptador", e.getMessage());
                                    }

                                    progress.hide();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progress.hide();

                                    Log.e("volley update carrito", error.toString());
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parametros = new HashMap<>();

                            parametros.put("id", md5IdUsuario);
                            parametros.put("carrito_id", tvCarritoId.getText().toString());

                            return parametros;
                        }
                    };
                    conexionServ.add(peticionServ);
                } //click
            });

            /*
            Click agregar uno
             */
            Button btnMasCarrito = convertView.findViewById(
                    R.id.btn_mas_carrito
            );
            btnMasCarrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProgressDialog progress;
                    progress = new ProgressDialog(contexto);
                    progress.setTitle("Agregando cantidad");
                    progress.setMessage("Por favor espera...");
                    progress.setCancelable(false);
                    progress.setIndeterminate(true);
                    progress.show();

                    cantidadProd = 0;

                    try {
                        cantidadProd = productoCarrito.getInt("cantidad");
                    }

                    catch(Exception e) { }

                    //Agregamos un mas a la cantidad del producto
                    cantidadProd++;

                    conexionServ = Volley.newRequestQueue(contexto);
                    peticionServ = new StringRequest(
                            Request.Method.POST,
                            "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/carrito/actualizar",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject objRespuesta = new JSONObject(response);

                                        if (objRespuesta.getInt("code") == 200) {
                                            //Si se actualizó el carrito, actualizamos el arreglo json
                                            datos = objRespuesta.getJSONArray("data");
                                            //Actualizamos al adaptador
                                            notifyDataSetChanged();

                                            Log.d("TOTAL", objRespuesta.getString("total"));

                                            //Actualizamos el total
                                            total = objRespuesta.getString("total");
                                            tvCarritoTotalProd.setText(
                                                    "Total: " +
                                                    total
                                            );
                                        }

                                    }

                                    catch(Exception e) {
                                        Log.e("ex. adaptador", e.getMessage());
                                    }

                                    progress.hide();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress.hide();

                            Log.e("volley update carrito", error.toString());
                        }
                    }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parametros = new HashMap<>();

                            parametros.put("id", md5IdUsuario);
                            parametros.put("carrito_id", tvCarritoId.getText().toString());
                            parametros.put("cantidad", String.valueOf(cantidadProd));

                            return parametros;
                        }
                    };
                    conexionServ.add(peticionServ);
                } //click
            });

        } // try

        catch(Exception e) {
            Log.e("Err. Adaptador carrito", e.getMessage());
        }

        return convertView;
    }
}
