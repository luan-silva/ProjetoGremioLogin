package com.example.projetogremiologin;

public class Usuario {
    String nome;
    String sobrenome;
    String telefone;
    String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public Usuario(String nome, String sobrenome, String telefone, String ID) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.ID= ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
