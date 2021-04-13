package com.example.projetogremiologin;

public class Item {
    String pathFoto;
    Double preco = 0.0, boughtPrice = 0.0;
    String category = "", description = "";

    public Item (){

    }
    public Item(String pathFoto, Double preco, Double boughtPrice, String category, String description) {
        this.pathFoto = pathFoto;
        this.preco = preco;
        this.boughtPrice = boughtPrice;
        this.category = category;
        this.description = description;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(Double boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
