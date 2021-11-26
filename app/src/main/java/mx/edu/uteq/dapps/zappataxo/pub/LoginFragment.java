package mx.edu.uteq.dapps.zappataxo.pub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import mx.edu.uteq.dapps.zappataxo.MainActivity;
import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private RequestQueue conexionServ;
    private StringRequest peticionServ;
    private ProgressDialog progress;
    private AlertDialog.Builder alerta;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        conexionServ = Volley.newRequestQueue(getActivity());

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Autenticando");
        progress.setMessage("Por favor espera...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        alerta = new AlertDialog.Builder(getActivity());

        /*
        Inicializamos el espacio de datos de nuestra app
         */
        sharedPreferences = getActivity().getSharedPreferences(
                "zappataxo",
                Context.MODE_PRIVATE
        );
        /*
        Inicializamos el editor del espacio de preferencias
         */
        spEditor = sharedPreferences.edit();

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnToRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_lf_to_rf);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String strUsuario = binding.tietUsuario.getText().toString();
                final String strPassword = binding.tietPassword.getText().toString();

                progress.show();

                peticionServ = new StringRequest(
                        Request.Method.POST,
                        "https://zavaletazea.dev/labs/awos-dapps-zappataxo/api/auth/login_app_movil",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progress.hide();

                                //Convertimos la respuesta un objeto JSON
                                try {
                                    JSONObject objRespuesta = new JSONObject(response);

                                    int codigo = objRespuesta.getInt("code");

                                    //Verificamos el estatus del usuario
                                    //Si el usuario existe y su contraseña coincide
                                    if (codigo == 200) {

                                        //Tomamos el objeto con los datos del usuario
                                        JSONObject datosUsuario = objRespuesta.getJSONObject("datos_usuario");
                                        int usuarioId = datosUsuario.getInt("usuario_id");
                                        /*
                                        Agregamos a SharedPReferences en nuestro espacio
                                        t196 el id y la contraseña encriptada del usuario
                                        El método put me permite agregar valores
                                         */
                                        //Agregar e id del usuario
                                        //encriptado
                                        spEditor.putString("id", md5(String.valueOf(usuarioId)));

                                        //guardamos la contraseña encriptada
                                        spEditor.putString("user_key", md5(strPassword));

                                        //Guardamos los cambios
                                        spEditor.commit();

                                        /*
                                        Redireccionamos al home de la app
                                         */
                                        startActivity(
                                                new Intent(
                                                        getActivity(),
                                                        MainActivity.class
                                                )
                                        );
                                    }

                                    //Si el usuario o la contraseña no coinciden
                                    else if (codigo == 404) {
                                        alerta = new AlertDialog.Builder(getActivity());
                                        alerta.setTitle("¡Hey!");
                                        alerta.setMessage("Usuario / contraseña incorrectos\nPor favor intenta de nuevo");
                                        alerta.setIcon(R.drawable.noencontrado);
                                        alerta.setPositiveButton("Aceptar", null);
                                        alerta.setCancelable(false);
                                        alerta.show();

                                    }

                                    else if (codigo == 403) {
                                        alerta = new AlertDialog.Builder(getActivity());
                                        alerta.setTitle("Cuenta deshabilitada");
                                        alerta.setMessage("Tu cuenta se encuentra temporalmente deshabilitada.\n\nPor favor contacta con un administrador para más información");
                                        alerta.setIcon(R.drawable.deshabilitado);
                                        alerta.setPositiveButton("Aceptar", null);
                                        alerta.setCancelable(false);
                                        alerta.show();
                                    }
                                }

                                catch(Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.hide();
                                Toast.makeText(getActivity(), error.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        parametros.put("usuario", strUsuario);
                        parametros.put("password", md5(strPassword));

                        return parametros;
                    }
                };
                conexionServ.add(peticionServ);
            }
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String md5(String texto) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(texto));
            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (java.security.NoSuchAlgorithmException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}