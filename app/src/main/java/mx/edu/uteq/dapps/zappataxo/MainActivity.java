package mx.edu.uteq.dapps.zappataxo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import mx.edu.uteq.dapps.zappataxo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        sharedPreferences = getSharedPreferences("zappataxo", MODE_PRIVATE);
        spEditor = sharedPreferences.edit();

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_catalogo,
                R.id.nav_catalogo_remoto,
                R.id.nav_pedidos,
                R.id.nav_listadeseos,
                R.id.nav_cuenta
        ).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        NavController navController = Navigation.findNavController(
                MainActivity.this,
                R.id.nav_host_fragment_content_main
        );
        navController.navigateUp();
        navController.navigate(R.id.nav_cuenta_carrito);

        if (id == R.id.action_salir) {
            salir();
        }

        if (id == R.id.action_carrito) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        salir();
    }

    public void salir() {


        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);

        alerta.setTitle("Salir de Zappataxo")
                .setMessage("¿Realmente deseas salir?")
                .setIcon(R.drawable.alerta)
                .setNegativeButton("Quedarme", null)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Usamos finish para quitar esta actividad
                        //del stack y no regresar sin pasar al login
                        spEditor.remove("id");
                        spEditor.remove(("user_key"));
                        spEditor.commit();
                        finish();
                        startActivity(
                                new Intent(
                                        MainActivity.this,
                                        InicioActivity.class
                                )
                        );
                    }
                })
                .setCancelable(true)
                .show();
    }

    /*
    Click genérico para mi ListView
     */
    public void clickItem(View v) {
        NavController navController = Navigation.findNavController(
                MainActivity.this,
                R.id.nav_host_fragment_content_main
        );
        navController.navigateUp();

        /*
        Tomamos el valor del elemento seleccionado
        para pasarlo al siguiente fragmento
         */
        TextView tvProductoId = v.findViewById(R.id.tv_ejemplo_prod_id);
        TextView tvNombreProd = v.findViewById(R.id.tv_ejemplo_nombre_prod);

        /*
        Pasamos los datos al siguinete fragmento usando un bundle
         */
        Bundle datos = new Bundle();
        datos.putInt(
                "pos",
                Integer.parseInt(tvProductoId.getText().toString())
        );
        datos.putString(
                "texto",
                tvNombreProd.getText().toString()
        );
        navController.navigate(R.id.nav_ejemplodetalle, datos);
    }


    public void clickDetalleOriginal(View v) {

        TextView tvProductoId = v.findViewById(R.id.tv_producto_id);
        Log.d("tvProductoId", tvProductoId.getText().toString());
        NavController navController = Navigation.findNavController(
                MainActivity.this,
                R.id.nav_host_fragment_content_main
        );
        navController.navigateUp();

        Bundle datos = new Bundle();
        datos.putString("id", tvProductoId.getText().toString());

        Log.d("aqui", "en la versión original");

        navController.navigate(R.id.nav_detalle_original, datos);
    }

}