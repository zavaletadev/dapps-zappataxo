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
    private String totalNumerico;
    private String md5IdUsuario;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;
    private int cantidadProd;

    public CarritoAdapter(Context contexto, JSONArray datos, String total, String totalNumerico, String md5IdUsuario) {
        this.contexto = contexto;
        this.datos = datos;
        this.total = total;
        this.totalNumerico = totalNumerico;
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
            LinearLayout llCarritoTotalProd = convertView.findViewById(R.id.ll_carrito_total_prod);
            LinearLayout llCarritoPagar = convertView.findViewById(R.id.ll_carrito_pagar);

            TextView tvCarritoTotalProd = convertView.findViewById(
                    R.id.tv_carrito_total_prod
            );

            //Mostramos el total y el boton de pago solo al final, en el último elemento
            // de la lista
            if (datos.length() - 1 == position) {
                llCarritoTotalProd.setVisibility(View.VISIBLE);
                llCarritoPagar.setVisibility(View.VISIBLE);
                tvCarritoTotalProd.setText("$" + total + " MXN");
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



                                            //Actualizamos el total
                                            total = objRespuesta.getString("total");
                                            tvCarritoTotalProd.setText(
                                                    "Total: $" +
                                                    total + "MXN"
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
            }); //Click Listener


            Button btnPagar = convertView.findViewById(R.id.btn_pagar);

            btnPagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog progress = new ProgressDialog(contexto);
                    progress.setTitle("Finalizando compra");
                    progress.setMessage("Por favor espera...");
                    progress.setCancelable(false);
                    progress.setIndeterminate(true);
                    progress.show();

                    /*
                    Datos para enviar al servicio
                    id (md5)
                    total_venta
                     */
                    try {
                        Log.d("id en md5", md5IdUsuario);
                        Log.d("total_venta", total);
                        /*Lista de productos del carrito*/
                        Log.d("arr_productos", datos.toString());

                        conexionServ = Volley.newRequestQueue(contexto);
                        peticionServ = new StringRequest(
                                Request.Method.POST,
                                "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/compra/realiza_compra",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progress.dismiss();
                                        Log.d("respCompra", response);
                                    }
                                }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progress.dismiss();
                                            Log.d("respCompra", error.toString());
                                        }
                                }
                        )
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parametros = new HashMap<>();
                                parametros.put("id", md5IdUsuario);
                                parametros.put("total_venta", totalNumerico);
                                parametros.put("arr_productos", datos.toString());

                                return parametros;
                            }
                        };
                        conexionServ.add(peticionServ);
                    }

                    catch(Exception e) {
                        Log.e("errorArrProd", e.getMessage());
                    }

                }
            });


        } // try

        catch(Exception e) {
            Log.e("Err. Adaptador carrito", e.getMessage());
        }

        return convertView;
    }
}
