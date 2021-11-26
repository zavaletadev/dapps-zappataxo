package mx.edu.uteq.dapps.zappataxo.pub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;

import mx.edu.uteq.dapps.zappataxo.R;

public class ProductoAdapter extends BaseAdapter {

    private Context contexto;
    private JSONArray datos;
    private LayoutInflater inflater;

    public ProductoAdapter(Context contexto, JSONArray datos) {
        this.contexto = contexto;
        this.datos = datos;
        inflater = LayoutInflater.from(contexto);
    }

    @Override
    public int getCount() {
        return datos.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_producto, null);
        }
        return convertView;
    }
}
