package com.example.myapplication.models;

public class Jarat {
    public String nev;
    public String indulas;
    public String erkezes;
    public String datum;
    public int ar;

    public Jarat() {} // Firestore-nak kell az üres konstruktor

    public Jarat(String nev, String indulas, String erkezes, String datum, int ar) {
        this.nev = nev;
        this.indulas = indulas;
        this.erkezes = erkezes;
        this.datum = datum;
        this.ar = ar;
    }
}
