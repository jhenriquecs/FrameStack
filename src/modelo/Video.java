package modelo;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private int id;
    private String titulo;
    private String data; // dd/MM/yyyy
    private String linkSite;
    private double nota; // nota de 0 a 10
    private List<Genero> listaDeGeneros;

    public Video() {
        this.listaDeGeneros = new ArrayList<>();
    }

    public Video(String titulo, String data, String linkSite, double nota){
        this.titulo = titulo;
        this.data = data;
        this.linkSite = linkSite;
        this.nota = nota;
        this.listaDeGeneros = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLinkSite() {
        return linkSite;
    }

    public void setLinkSite(String linkSite) {
        this.linkSite = linkSite;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public List<Genero> getListaDeGeneros() {
        return listaDeGeneros;
    }

    public void setListaDeGeneros(List<Genero> listaDeGeneros) {
        this.listaDeGeneros = listaDeGeneros;
    }

    public void adicionarGenero(Genero genero){
        this.listaDeGeneros.add(genero);
        genero.adicionarVideo(this);
    }

    public boolean removerGenero(Genero genero){
        if (this.listaDeGeneros.remove(genero)){
            genero.removerVideo(this);
            return true;
        }
        return false;
    }

    public int totalDeGeneros(){
        return this.listaDeGeneros.size();
    }
    
    @Override
    public String toString(){
        List<String> nomes = new ArrayList<>();
        for (Genero g : listaDeGeneros){
            nomes.add(g.getNome());
        }
        return "Video{" +
                "id=" + id +
                ",titulo='" + titulo + '\'' +
                ",data=" + data +
                ",linkSite='" + linkSite + '\'' +
                ",nota=" + nota +
                ",listaDeGeneros=" + nomes +
                '}';
    }
}
