package com.tobiasandre.goestetica.database.model;

import android.graphics.Bitmap;

/**
 * Created by TobiasAndre on 24/06/2017.
 */

public class Contact {

    private int id;
    private String name;
    private String nrPhone;
    private String email;
    private Bitmap foto;
    private Boolean imported;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if(name==null){
            name="";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNrPhone() {
        if(nrPhone==null){
            nrPhone="";
        }
        return nrPhone;
    }

    public void setNrPhone(String nrPhone) {
        this.nrPhone = nrPhone;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getEmail() {
        if(email==null){
            email="";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getImported() {
        return imported;
    }

    public void setImported(Boolean imported) {
        this.imported = imported;
    }
}
