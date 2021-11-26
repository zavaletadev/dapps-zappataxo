package mx.edu.uteq.dapps.zappataxo.pub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.edu.uteq.dapps.zappataxo.InicioActivity;
import mx.edu.uteq.dapps.zappataxo.MainActivity;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInicioBinding.inflate(inflater, container, false);

        conexionServ = Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("zappataxo", Context.MODE_PRIVATE);

        final String spId = sharedPreferences.getString("id", "");
        final String spUserKey = sharedPreferences.getString("user_key", "");

        peticionServ = new StringRequest(
                Request.Method.POST,
                "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/auth/auto_login_movil",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objRespuesta = new JSONObject(response);

                            int codigo = objRespuesta.getInt("code");

                            if (codigo == 200) {
                                startActivity(
                                        new Intent(
                                                getActivity(),
                                                MainActivity.class
                                        )
                                );
                            }

                            else {
                                binding.llLoaderIni.setVisibility(View.GONE);
                                binding.llHomeInfo.setVisibility(View.VISIBLE);
                            }
                        }

                        catch(Exception e) {
                            binding.llLoaderIni.setVisibility(View.GONE);
                            binding.llHomeInfo.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(),
                        Toast.LENGTH_SHORT).show();

                        binding.llLoaderIni.setVisibility(View.GONE);
                        binding.llHomeInfo.setVisibility(View.VISIBLE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap();
                parametros.put("id", spId);
                parametros.put("user_key", spUserKey);

                return parametros;
            }
        };
        conexionServ.add(peticionServ);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}