package mx.edu.uteq.dapps.zappataxo.pub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

import mx.edu.uteq.dapps.zappataxo.InicioActivity;
import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentDetalleBinding;

public class DetalleFragment extends Fragment {

    private FragmentDetalleBinding binding;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDetalleBinding.inflate(inflater, container, false);

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

        return root;
    }
}