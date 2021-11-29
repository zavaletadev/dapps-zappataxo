package mx.edu.uteq.dapps.zappataxo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentEjemplodetalleBinding;

public class EjemplodetalleFragment extends Fragment {

    private FragmentEjemplodetalleBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEjemplodetalleBinding.inflate(inflater, container, false);

        /*
        Creamos un bundle para evaluar si el fragmento anterior nos envió
        datos
         */
        Bundle datos = this.getArguments();
        int pos = datos.getInt("pos");
        String texto = datos.getString("texto");

        /*
        Mostramos los valores en la vista (layout)
        dentro de sus respectivos 'TextView'
         */
        binding.tvPosSeleccionada.setText(String.valueOf(pos));
        binding.tvTextoSeleccionado.setText(texto);

        return binding.getRoot();
    }
}