package mx.edu.uteq.dapps.zappataxo.ui.catalogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.edu.uteq.dapps.zappataxo.R;

/*
Los adaptadores de un ListView deben heredar de una clase
denominada BaseAdapter que nos provee los métodos necesarios
para crear nuestro propio modelo de Listview
 */
public class EjemploAdapter extends BaseAdapter {

    /*
    Para inicializar un ListView con un diseño personalizado
    necesitamos:
    1.- Contexto
    2.- Colección de valores
    3.- Diseño de cada elemento de la colección
     */
    private Context contexto;
    private List<Producto> datos;
    private LayoutInflater inflater; //lifar el layout de cada elemento

    /*
    Inicializamos el constructor de la clase con los atributos
     */
    public EjemploAdapter(Context contexto, List<Producto> datos) {
        //Pasamos los aprámetros a los atributos
        this.contexto = contexto;
        this.datos = datos;
        inflater = LayoutInflater.from(contexto);
    }

    //Regresar el número de elementos del adaptador
    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
    Este método se está ejecutando por cada elemento de la colección
    de valores
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        Por cada elemento del arraylist mostramos el layout de cada
        item
         */

        //Si ya cargamos el layout, no lo volvemos a cargar
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_ejemplo, null);
        }

        /*
        Creamos un componente por cada elemento visual que vamos a modificar
         */
        TextView tvEjemploNombreProd = convertView.findViewById(
                R.id.tv_ejemplo_nombre_prod
        );
        //Mostramos el nombre del producto del ArrayList
        tvEjemploNombreProd.setText(
                datos.get(position).getNombreProd()
        );

        TextView tvEjemploPrecioProd = convertView.findViewById(
                R.id.tv_ejemplo_precio_prod
        );
        tvEjemploPrecioProd.setText(
                "$" +
                datos.get(position).getPrecioProd() +
                " MXN"
        );

        /*
        Para manejar imagenes de internet neceistamos una
        librería llamada 'Picasso'
        **no olvides configurarla en gradle:app
         */

        /*
        Tomamos la url de la imagen desde la nube y la mostramos
        en el imageView
         */
        ImageView ivEjemploLista = convertView.findViewById(
                R.id.iv_ejemplo_lista
        );

        Picasso.get().load(datos.get(position).getUrlImagenProd())
                .into(ivEjemploLista);

        /*
        Mostramos el id de cada elemento de la colección
         */
        TextView tvEjemploProdId = convertView.findViewById(
                R.id.tv_ejemplo_prod_id
        );
        tvEjemploProdId.setText(
                String.valueOf(
                        datos.get(position).getProductoId()
                )
        );



        return convertView;
    }
}
