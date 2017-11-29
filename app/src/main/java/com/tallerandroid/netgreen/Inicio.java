package com.tallerandroid.netgreen;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by yesce on 14/05/2017.
 */

public class  Inicio {
    private String nomUsuario;
    private String fechaPublicacion;
    private String idPublicacion;
    private String nomPublicacion;
    private String Descripcion;
    private String tipo;
    private Bitmap imagen;
    private int idUsuario;
    private String estatus;
    private String verMas;
    private String categoria;
    private String subcategoria;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getVerMas() {
        return verMas;
    }

    public void setVerMas(String verMas) {
        this.verMas = verMas;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getNomPublicacion() {
        return nomPublicacion;
    }

    public void setNomPublicacion(String nomPublicacion) {
        this.nomPublicacion = nomPublicacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}