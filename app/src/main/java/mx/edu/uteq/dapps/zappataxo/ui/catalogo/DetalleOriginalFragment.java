package mx.edu.uteq.dapps.zappataxo.ui.catalogo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentDetalleBinding;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentDetalleOriginalBinding;

public class DetalleOriginalFragment extends Fragment {

    private FragmentDetalleOriginalBinding binding;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;
    private String strPrecioProd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDetalleOriginalBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        Bundle datos = this.getArguments();
        String strProductoId = datos.getString("id", "");
        Log.d("strProductoId", strProductoId);


        conexionServ = Volley.newRequestQueue(getActivity());
        peticionServ = new StringRequest(
                Request.Method.POST,
                "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/productos/detalle/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objRespuesta = new JSONObject(response);

                            if (objRespuesta.getInt("code") == 200) {
                                JSONObject producto = objRespuesta.getJSONObject("data");

                                strPrecioProd = producto.getString("precio_prod");

                                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                                        producto.getString("nombre_prod")
                                );

                                Picasso.get().load(producto.getString("img1")).into(
                                        binding.ivDetalleProdImg1
                                );

                                binding.tvDetalleProdDesc.setText(
                                        producto.getString("desc_prod")
                                );

                                Picasso.get().load(producto.getString("img2")).into(
                                        binding.ivDetalleProdImg2
                                );

                                Picasso.get().load(producto.getString("img3")).into(
                                        binding.ivDetalleProdImg3
                                );

                                binding.tvDetalleProdPrecio.setText(
                                        "$" +
                                                producto.getString("precio_prod") +
                                                " MXN"
                                );

                                int categoria = producto.getInt("categoria");

                                if (categoria == 1) {
                                    binding.ivDetalleProdMujer.setImageResource(
                                            R.drawable.ic_woman
                                    );
                                    binding.ivDetalleProdHombre.setImageResource(
                                            R.drawable.transparente
                                    );
                                }

                                else if (categoria == 2) {
                                    binding.ivDetalleProdMujer.setImageResource(
                                            R.drawable.transparente
                                    );
                                    binding.ivDetalleProdHombre.setImageResource(
                                            R.drawable.ic_man
                                    );
                                }

                                else if (categoria == 3) {
                                    binding.ivDetalleProdMujer.setImageResource(
                                            R.drawable.ic_woman
                                    );
                                    binding.ivDetalleProdHombre.setImageResource(
                                            R.drawable.ic_man
                                    );
                                }

                                else {
                                    binding.ivDetalleProdMujer.setImageResource(
                                            R.drawable.transparente
                                    );
                                    binding.ivDetalleProdHombre.setImageResource(
                                            R.drawable.transparente
                                    );
                                }

                                binding.llLoaderProducto.setVisibility(View.GONE);
                                binding.llContenidoProd.setVisibility(View.VISIBLE);

                                JSONArray tallas = producto.getJSONArray("tallas");
                                List<String> listaTallas = new ArrayList<>();

                                listaTallas.add("Selecciona una talla");
                                for (int i = 0; i < tallas.length(); i++) {
                                    listaTallas.add(tallas.getString(i));
                                }

                                ArrayAdapter<String> adaptadorTallas = new ArrayAdapter<>(
                                        getActivity(),
                                        android.R.layout.simple_spinner_dropdown_item,
                                        listaTallas
                                );
                                binding.spDetalleProdTallas.setAdapter(adaptadorTallas);

                            }

                            else {
                                NavController navController = Navigation.findNavController(
                                        getActivity(),
                                        R.id.nav_host_fragment_content_inicio
                                );
                                navController.navigateUp();
                                navController.navigate(R.id.InicioFragment);
                            }

                        }

                        catch(Exception e) {
                            Log.e("Error detalle producto", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<>();

                parametros.put("producto_id", strProductoId);

                return parametros;
            }
        };
        conexionServ.add(peticionServ);

        /*
        Click del FAB agregar al carrito
         */
        binding.fabAgregaCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Si el usuario no ha seleccionado una Talla
                la pos defecto del spinner es 0
                 */
                if (binding.spDetalleProdTallas.getSelectedItemPosition() == 0) {
                    Toast.makeText(
                            getActivity(),
                            "Selecciona una talla",
                            Toast.LENGTH_SHORT
                    ).show();

                    //Finalizamos el evento click
                    return;
                }


                /*
                Tomamos los valores para agregar al servicio
                id (id_encriptado del usuario) [SharedPreferences]
                producto_id
                cantidad (por defecto agregamos siempre 1)
                talla_prod
                precio_prod
                 */
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                        "zappataxo",
                        Context.MODE_PRIVATE
                );
                String md5IdUsuario = sharedPreferences.getString("id", null);

                /*
                Tomamos la talla seleccionada del producto desde el Spinner
                 */
                String strTallaSel = binding.spDetalleProdTallas.getSelectedItem().toString();

                /*
                Imprimimos en consola las variables para verificar
                que tenemos todos los datos
                 */
                Log.d("id", md5IdUsuario);
                Log.d("producto_id", strProductoId);
                Log.d("cantidad", "por defecto 1");
                Log.d("talla", strTallaSel);
                Log.d("precio_prod", strPrecioProd);

                ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle("Agregando al carrito");
                progress.setMessage("Por favor espera...");
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();

                /*
                Usamos el servicios para agregar datos al carrito
                 */
                peticionServ = new StringRequest(
                        Request.Method.POST,
                        "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/carrito/agregar",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progress.hide();
                                try {
                                    JSONObject objRespuesta = new JSONObject(response);
                                    /*
                                    Si el servicio retorna 200
                                    */
                                    if (objRespuesta.getInt("code") == 200) {
                                        Toast.makeText(getActivity(), "Poducto agregado",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    else {
                                        Toast.makeText(getActivity(), response,
                                                Toast.LENGTH_SHORT).show();
                                        Log.e("resp. serv. + carrito", response);
                                    }
                                }

                                catch(Exception e) {
                                    Log.e("Err. ser. add. carrito", e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.hide();
                                Log.e("Err. volley add carrito", error.toString());
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> parametros = new HashMap<>();
                        /*
                        Agregamos las variables post que el servicio nos indica
                         */
                        parametros.put("id", md5IdUsuario);
                        parametros.put("producto_id", strProductoId);
                        parametros.put("cantidad", "1");
                        parametros.put("talla_prod", strTallaSel);
                        parametros.put("precio_prod", strPrecioProd);

                        return parametros;
                    }
                };
                //ejecutamos el servicio
                conexionServ.add(peticionServ);
            }
        });

        return root;
    }
}
