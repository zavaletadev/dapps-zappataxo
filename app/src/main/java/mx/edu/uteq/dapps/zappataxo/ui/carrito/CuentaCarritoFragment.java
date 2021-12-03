package mx.edu.uteq.dapps.zappataxo.ui.carrito;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentCuentaCarritoBinding;

public class CuentaCarritoFragment extends Fragment {

    private FragmentCuentaCarritoBinding binding;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;
    private SharedPreferences sharedPreferences;
    private String md5IdUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCuentaCarritoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        conexionServ = Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(
                "zappataxo",
                Context.MODE_PRIVATE
        );
        md5IdUsuario = sharedPreferences.getString("id", null);

        //Cargamos al carrito al inciar el swipe
        binding.srlCuentaCarrito.post(new Runnable() {
            @Override
            public void run() {
                binding.srlCuentaCarrito.setRefreshing(true);
                cargaCarrito();
            }
        });

        /*
        Evento swipe
         */
        binding.srlCuentaCarrito.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargaCarrito();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void cargaCarrito() {
        peticionServ = new StringRequest(
                Request.Method.POST,
                "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/carrito/lista",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        binding.srlCuentaCarrito.setRefreshing(false);
                        try {
                            JSONObject objRespuesta = new JSONObject(response);

                            /*
                            Si el servicio contesta con un 200
                             */
                            if (objRespuesta.getInt("code") == 200) {
                                /*
                                Creamos un arreglo con los datos del carrito
                                 */
                                JSONArray productosCarrito = objRespuesta.getJSONArray("data");

                                /*
                                Inicializamos el adaptador de productos del carrito
                                 */
                                CarritoAdapter carritoAdapter = new CarritoAdapter(
                                        getActivity(),
                                        productosCarrito,
                                        objRespuesta.getString("total"),
                                        md5IdUsuario
                                );

                                /*
                                Indicamos el adaptador del ListView
                                 */
                                binding.lvCuentaCarrito.setAdapter(carritoAdapter);

                                /*
                                Actualizamos el contenido del adaptador
                                 */
                                carritoAdapter.notifyDataSetChanged();
                            }

                            else {
                                Log.e("code != 200", response);
                            }
                        }

                        catch(Exception e) {
                            Log.e("Err. serv lista carrito", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.srlCuentaCarrito.setRefreshing(false);
                        Log.e("Err volley list carrito", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", md5IdUsuario);

                return parametros;
            }
        };
        //Ejecutamos al servicio
        conexionServ.add(peticionServ);
    }
}