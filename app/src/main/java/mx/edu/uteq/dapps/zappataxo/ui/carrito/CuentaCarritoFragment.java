package mx.edu.uteq.dapps.zappataxo.ui.carrito;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentCuentaCarritoBinding;

public class CuentaCarritoFragment extends Fragment {

    private FragmentCuentaCarritoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCuentaCarritoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}