package mx.edu.uteq.dapps.zappataxo.ui.pedidos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentPedidosBinding;

public class PedidosFragment extends Fragment {


    private FragmentPedidosBinding binding;
    private ArrayAdapter<String> adaptador;
    private List<String> datos;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;
    private SharedPreferences sharedPreferences;
    private String md5Id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPedidosBinding.inflate(inflater, container, false);

        /*
        Invocamos el archivo de datos de la aplicacion
        con el nombre del espacio de trabajo
         */
        sharedPreferences = getActivity().getSharedPreferences(
                "zappataxo",
                Context.MODE_PRIVATE
        );
        md5Id = sharedPreferences.getString("id", null);

        /*Inicializamos la lista de datos VACÍA*/
        datos = new ArrayList<>();

        /*Inicializamos el adaptador por defecto con el diseño de Android*/
        adaptador = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                datos
        );

        /*Indicamos el adaptador para el ListView*/
        binding.lvPedidos.setAdapter(adaptador);

        conexionServ = Volley.newRequestQueue(getActivity());

        //Al cargar el fragmento ponemos a cargar el Swipe
        binding.srlPedidos.post(new Runnable() {
            @Override
            public void run() {
                binding.srlPedidos.setRefreshing(true);
                cargaCompras();
            }
        });

        //Evento Swipe
        binding.srlPedidos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargaCompras();
            }
        });

        /*
        Evento click de los items del listview
         */
        binding.lvPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.d("posElem", position+"");

                /*
                Tomar el id de la venta par amostrar otra ventana
                con su detalle
                 */

            }
        });

        return binding.getRoot();
    }

    public void cargaCompras() {
        peticionServ = new StringRequest(
                Request.Method.POST,
                "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/compra/mis_compras",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsePEdidos", response);

                        try {
                            JSONObject objRespuesta = new JSONObject(response);

                            if (objRespuesta.getInt("code") == 200) {
                                /*
                                Tomamos la lista de compras y la agregamos a la lista
                                de datos
                                 */
                                JSONArray comprasJSON = objRespuesta.getJSONArray("data");

                                //limpiamos la lista de datos
                                datos.clear();
                                //Recorremos el arreglo de compras en JSON
                                for(int i = 0; i < comprasJSON.length(); i++) {
                                    JSONObject compra = comprasJSON.getJSONObject(i);
                                    datos.add(
                                            compra.getString("fecha_venta") + " " +
                                            "  $" + compra.getString("total_venta") + " MXN" +
                                            "  " + compra.getString("numero_prod") + " productos"
                                    );
                                }

                                //Actualizamos los datos de la lista
                                adaptador.notifyDataSetChanged();
                                binding.srlPedidos.setRefreshing(false);
                            }

                            else {
                                binding.srlPedidos.setRefreshing(false);
                            }
                        }

                        catch(Exception e) {
                            binding.srlPedidos.setRefreshing(false);
                            Log.e("excPedidos", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.srlPedidos.setRefreshing(false);
                        Log.e("errorPedidos", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", md5Id);
                return parametros;
            }
        };
        conexionServ.add(peticionServ);
    }
}