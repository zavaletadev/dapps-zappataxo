package mx.edu.uteq.dapps.zappataxo.ui.cuenta;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentCambiapassBinding;

public class CambiapassFragment extends BottomSheetDialogFragment {

    private FragmentCambiapassBinding binding;
    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCambiapassBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        progress = new ProgressDialog(getActivity());

        binding.btnGuardarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Snackbar.make(
//                        getDialog().getWindow().getDecorView(),
//                        "Aqui",
//                        Snackbar.LENGTH_LONG
//                ).show();

                progress.setTitle("Cambiando contraseña");
                progress.setMessage("Por favor espera...");
                progress.setIcon(R.drawable.ic_lock_open);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();

            }
        });

        return root;
    }
}