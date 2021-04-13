package com.example.projetogremiologin;

public class Roupa
{
    String foto;
    Double preco;

    public Roupa (){

    }

    public Roupa(String foto, Double preco) {
        this.foto = foto;
        this.preco = preco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
