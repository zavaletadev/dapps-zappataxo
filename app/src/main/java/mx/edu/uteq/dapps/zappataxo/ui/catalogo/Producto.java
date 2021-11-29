package mx.edu.uteq.dapps.zappataxo.ui.catalogo;

public class Producto {

    private int productoId;
    private String nombreProd;
    private double precioProd;
    private String urlImagenProd;

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public double getPrecioProd() {
        return precioProd;
    }

    public void setPrecioProd(double precioProd) {
        this.precioProd = precioProd;
    }

    public String getUrlImagenProd() {
        return urlImagenProd;
    }

    public void setUrlImagenProd(String urlImagenProd) {
        this.urlImagenProd = urlImagenProd;
    }
}
