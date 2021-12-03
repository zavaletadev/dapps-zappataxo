package mx.edu.uteq.dapps.zappataxo.ui.catalogo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentCatalogooriginalBinding;
import mx.edu.uteq.dapps.zappataxo.pub.ProductoAdapter;

public class CatalogooriginalFragment extends Fragment {

    private FragmentCatalogooriginalBinding binding;
    private RequestQueue conexionServ;
    private StringRequest peticionProdServ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCatalogooriginalBinding.inflate(inflater, container, false);

        conexionServ = Volley.newRequestQueue(getActivity());

        cargaProductos();

        binding.srlListaProductos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargaProductos();
            }
        });

        return binding.getRoot();
    }

    public void cargaProductos() {
        /*
        Petición de productos
         */
        peticionProdServ = new StringRequest(
                Request.Method.GET,
                "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/productos/lista",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objRespuesta = new JSONObject(response);

                            if (objRespuesta.getInt("code") == 200) {
                                JSONArray productos = objRespuesta.getJSONArray("data");

                                ProductoOriginalAdapter adaptadorProductos = new ProductoOriginalAdapter(
                                        getActivity(),
                                        productos
                                );

                                binding.llLoaderIni.setVisibility(View.GONE);
                                binding.llHomeInfo.setVisibility(View.VISIBLE);

                                binding.lvProductos.setAdapter(adaptadorProductos);
                                binding.llLoaderProds.setVisibility(View.GONE);
                                adaptadorProductos.notifyDataSetChanged();
                            }

                            binding.srlListaProductos.setRefreshing(false);
                        }

                        catch(Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            binding.srlListaProductos.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
        );
        conexionServ.add(peticionProdServ);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}