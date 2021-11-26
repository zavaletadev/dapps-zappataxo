package mx.edu.uteq.dapps.zappataxo.ui.cuenta;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.edu.uteq.dapps.zappataxo.R;
import mx.edu.uteq.dapps.zappataxo.databinding.FragmentCuentaBinding;

public class CuentaFragment extends Fragment {

    private FragmentCuentaBinding binding;
    private CambiapassFragment cambiapassFragment;
    private char miVar;
    private char[] miVar2;
    private char miVar3[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCuentaBinding.inflate(inflater, container, false);

        binding.btnModalCambiaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiapassFragment = new CambiapassFragment();
                cambiapassFragment.setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
                cambiapassFragment.show(
                        getActivity().getSupportFragmentManager(),
                        "cambia_pass_fragment"
                );
            }
        });

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}