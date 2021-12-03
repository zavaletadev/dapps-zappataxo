package mx.edu.uteq.dapps.zappataxo.ui.catalogo;

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

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentCatalogoBinding;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentDetalleBinding;

public class CatalogoFragment extends Fragment {

    private FragmentCatalogoBinding binding;

    /*
    Para generar un ListView con un diseño personalizado (con datos locales),
    primero necesitamos:
    1.- El diseño de cada item (elemento) de la lista
    2.- Contar con un POJO para guardar los elementos de la colección
        (Plain Old Java Object, una clase con la estructura de los
        datos a guardar para poder crear una lista de este tipo
    3.- Crear un adaptador personalizado con el tipo de dato de los
    elementos de mi colección
    */
    private EjemploAdapter adaptador;
    private List<Producto> datos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCatalogoBinding.inflate(inflater, container, false);

        datos = new ArrayList<>();
        for (int i = 1; i<= 15; i++) {
            //Creamos un producto con datos genéricos
            Producto prod = new Producto();
            prod.setProductoId(i);
            prod.setNombreProd("Producto" + i);
            prod.setPrecioProd(i * 3);
            prod.setUrlImagenProd("https://zavaletazea.dev/img/productos/prod"+i+"_1.jpeg");
            datos.add(prod);
        }

        /*
        Inicializamos el adaptador con sus dos parámetros
        1.- Contexto
        2.- Datos (Colección)
         */
        adaptador = new EjemploAdapter(getActivity(), datos);

        //Vincular el adaptador con el ListView
        binding.lvListapersoLocal.setAdapter(adaptador);

        return binding.getRoot();
    }
}