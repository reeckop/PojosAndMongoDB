package model;

/**
 *
 * @author Ricardo
 */
public class Proveedor {
    private String nombre;
    private String contacto;
    private String pais;

    public Proveedor() {}

    public Proveedor(String nombre, String contacto, String pais) {
        this.nombre = nombre;
        this.contacto = contacto;
        this.pais = pais;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    @Override
    public String toString() {
        return "Proveedor{nombre='" + nombre + "', contacto='" + contacto + "', pais='" + pais + "'}";
    }
}
