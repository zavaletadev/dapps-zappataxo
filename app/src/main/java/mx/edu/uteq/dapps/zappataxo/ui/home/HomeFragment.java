package mx.edu.uteq.dapps.zappataxo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    /*
    Agregamos una refrefencia entre la vista y el controlador
    por medio de un bindig
     */
    private FragmentHomeBinding binding;

    /*
    Servicio web
     */
    private RequestQueue conexionServ;
    private StringRequest peticionServ;

    /*
    ListView
     */
    private List<String> datos;
    private ArrayAdapter<String> adatador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        conexionServ = Volley.newRequestQueue(getActivity());

        datos = new ArrayList<>();

        adatador = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                datos
        );
        binding.lvEjemploSimpleRemoto.setAdapter(adatador);

        /*
        Indicamos que el swipe está cargando contenido
        Dentro de un hilo para asegurarnos que el contenido se carga
        cuando el Swipe está inicializado
         */
        binding.srlEjemploSimpleRemoto.post(new Runnable() {
            @Override
            public void run() {
                /*
                Creamos un método para cargar los datos del adaptador
                al listview
                 */
                binding.srlEjemploSimpleRemoto.setRefreshing(true);
                cargarDatosRemotos();
            }
        });

        /*
        En el evento swipe tambien carga a datos remotos
         */
        binding.srlEjemploSimpleRemoto.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarDatosRemotos();
            }
        });

        return binding.getRoot();
    }

    /*
    Creamos un método que regrese la lista de productos
    desde una consulta a la base de datos
     */
    public void cargarDatosRemotos() {
        peticionServ = new StringRequest(
            Request.Method.GET,
            "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/productos/lista",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Equivalente a un print en consola
                    //Log.d("Resp servicio", response);
                    try {
                        JSONObject objRespuesta = new JSONObject(response);

                        /*
                        Si la respuesta contiene una clave llamada
                        'code' con un valor de 200
                         */
                        if (objRespuesta.getInt("code") == 200) {
                            /*
                            Tomamos el arreglo de prodcutos de la clave
                            'data'
                             */
                            JSONArray productos = objRespuesta.getJSONArray("data");

                            //Eliminamos los elementos anteriores
                            datos.clear();

                            /*
                            Recorrer el arreglo de productos y pasar solo el nombre al
                            arraylist 'datos'
                             */
                            for (int i = 0; i < productos.length(); i++) {
                                //Tomar cada producto del arreglo
                                JSONObject prod = productos.getJSONObject(i);
                                //Agregamos solo el nombre del producto al arraylist
                                datos.add(prod.getString("nombre_prod"));
                            }

                            /*
                            Actualizar el adaptador
                             */
                            adatador.notifyDataSetChanged();

                            //Quitamos el modo loader del SwipeRefresh
                            binding.srlEjemploSimpleRemoto.setRefreshing(false);
                        }
                    }

                    catch(Exception e) {
                        Log.e("error catch", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Mostramos en consola el error
                    Log.e("Error en el servicio", error.toString());
                }
            }
        );
        conexionServ.add(peticionServ);
    }
}