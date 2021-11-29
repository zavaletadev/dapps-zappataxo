package mx.edu.uteq.dapps.zappataxo.ui.catalogo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentCatalogoremotoBinding;

public class CatalogoremotoFragment extends Fragment {

    private FragmentCatalogoremotoBinding binding;

    /*
    Webservice
     */
    private RequestQueue conexionServ;
    private StringRequest peticionServ;

    /*
    Mostrar ListView
     */
    EjemploRemotoAdapter adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCatalogoremotoBinding.inflate(inflater, container, false);

        /*
        Indicamos el estado de cargando a nuestro swiperefresh
         */
        binding.srlPersoRemota.post(new Runnable() {
            @Override
            public void run() {
                binding.srlPersoRemota.setRefreshing(true);
                cargaProductos();
            }
        });

        /*
        Programamos el evento Swipr (arrastrar y soltar)
         */
        binding.srlPersoRemota.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargaProductos();
            }
        });

        conexionServ = Volley.newRequestQueue(getActivity());


        return binding.getRoot();
    }

    /*
    Método para consultar los productos de la base de datos
    por medio de un servicio
     */
    public void cargaProductos() {
        peticionServ = new StringRequest(
                Request.Method.GET,
                "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/productos/lista",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Serv prod", response);

                        try {
                            //Creamos el objeto json de la respuesta
                            JSONObject objRespuesta = new JSONObject(response);

                            //Verificamos que el servicio responda con un estatus 200
                            if (objRespuesta.getInt("code") == 200) {
                                /*
                                Tomamos el arreglo de productos desde la clave
                                'data'
                                 */
                                JSONArray productos = objRespuesta.getJSONArray("data");

                                /*
                                Inicializamos el adaptador pasandole el arreglo json de productos
                                 */
                                adaptador = new EjemploRemotoAdapter(getActivity(), productos);

                                /*
                                Vincular el adaptador con el ListView
                                 */
                                binding.lvPersoRemota.setAdapter(adaptador);

                                /*
                                Actualizamos el contenido del adaptador
                                 */
                                adaptador.notifyDataSetChanged();

                                /*
                                Quitamos el loader del SwipeRefresh
                                 */
                                binding.srlPersoRemota.setRefreshing(false);
                            }

                        }

                        catch (Exception e) {
                            Log.e("catch", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error volley", error.getMessage());
                    }
                }
        );
        conexionServ.add(peticionServ);
    }

}