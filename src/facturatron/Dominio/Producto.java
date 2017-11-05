/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package facturatron.Dominio;

import facturatron.mvc.Model;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 *
 * @author jach
 */
public class Producto extends Model implements Serializable {
    private Integer     id  = -1;
    private String      clave   = "";
    private String      nombre  = "";
    private BigDecimal  precio;
    private Boolean     activo  = true;
    private String      notas   = "";
    
    public Producto() {
        setPrecio(new BigDecimal("0"));
    }
    
    public  BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        precio.setScale(2);
        this.precio = precio;
    }
   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public String toString() {
        return clave;
    }
}
