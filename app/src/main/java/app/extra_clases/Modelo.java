package app.extra_clases;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Modelo{

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("confirm_password")
    @Expose
    private String cpassword;


    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("entradas")
    @Expose
    private String entradas;

    @SerializedName("estatus")
    @Expose
    private String estatus;

    @SerializedName("idInscritos")
    @Expose
    private String idInscritos;

    @SerializedName("idCreador")
    @Expose
    private String idCreador;

    @SerializedName("costo")
    @Expose
    private String costo;

    @SerializedName("data")
    @Expose
    private ArrayList<Modelo> data;






    public Modelo() {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEntradas() {
        return entradas;
    }

    public void setEntradas(String entradas) {
        this.entradas = entradas;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getIdInscritos() {
        return idInscritos;
    }

    public void setIdInscritos(String idInscritos) {
        this.idInscritos = idInscritos;
    }

    public String getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(String idCreador) {
        this.idCreador = idCreador;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public ArrayList<Modelo> getData() {
        return data;
    }

    public void setData(ArrayList<Modelo> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
