package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Video {
    private int id;
    private String titulo;
    private LocalDate data;
    private String linkSite;
    private String classificacao;
    private List<Genero> listaDeGeneros;

    public Video() {
        this.listaDeGeneros = new ArrayList<>();

    }

    public Video(int id, String titulo, LocalDate data, String linkSite, String classificacao){
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.linkSite = linkSite;
        this.classificacao = classificacao;
        this.listaDeGeneros = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLinkSite() {
        return linkSite;
    }

    public void setLinkSite(String linkSite) {
        this.linkSite = linkSite;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public List<Genero> getListaDeGeneros() {
        return listaDeGeneros;
    }

    public void setListaDeGeneros(List<Genero> listaDeGeneros) {
        this.listaDeGeneros = listaDeGeneros;
    }

    public void adicionarGenero(Genero genero){
        if (genero != null && !this.listaDeGeneros.contains(genero)){
            this.listaDeGeneros.add(genero);
            genero.adicionarVideo(this);
        }
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
                ",classificacao='" + classificacao + '\'' +
                ",listaDeGeneros=" + nomes +
                '}';
    }
}
