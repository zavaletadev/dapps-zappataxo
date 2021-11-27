package mx.edu.uteq.dapps.zappataxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

import mx.edu.uteq.dapps.zappataxo.databinding.ActivityInicioBinding;

public class InicioActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityInicioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pub_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_carrito) {
            NavController navController = Navigation.findNavController(
                    InicioActivity.this,
                    R.id.nav_host_fragment_content_inicio
            );
            navController.navigateUp();
            navController.navigate(R.id.CarritoFragment);
            getSupportActionBar().setTitle("Carrito de compras");
        }

        if (id == R.id.action_registro) {
            NavController navController = Navigation.findNavController(
                    InicioActivity.this,
                    R.id.nav_host_fragment_content_inicio
            );
            navController.navigateUp();
            navController.navigate(R.id.RegistroFragment);
            getSupportActionBar().setTitle("Registro");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public void clickDetalle(View v) {
        NavController navController = Navigation.findNavController(
                InicioActivity.this,
                R.id.nav_host_fragment_content_inicio
        );
        navController.navigateUp();
        navController.navigate(R.id.DetalleFragment);
        getSupportActionBar().setTitle("");
    }

    /*
    Clic del detalle de producto que envia al carrito
     */
    public void clickCarrito(View v) {
        NavController navController = Navigation.findNavController(
                InicioActivity.this,
                R.id.nav_host_fragment_content_inicio
        );
        navController.navigateUp();
        navController.navigate(R.id.CarritoFragment);
        getSupportActionBar().setTitle("Carrito de compras");
    }

    public void clickHome(View v) {
        startActivity(
                new Intent(
                        InicioActivity.this,
                        MainActivity.class
                )
        );
    }



}
