package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um Gênero na videoteca.
 * Cada gênero possui um identificador, um nome e uma lista de vídeos associados.
 *
 * @author Ian
 */
public class Genero {

    private int id;
    private String nome;
    private List<Video> listaDeVideos;

    /**
     * Construtor padrão.
     * Inicializa a lista de vídeos como uma lista vazia.
     */
    public Genero() {
        this.listaDeVideos = new ArrayList<>();
    }

    /**
     * Construtor com parâmetros.
     * O id é gerado automaticamente pelo GeradorID ao gravar no banco.
     *
     * @param nome Nome do gênero (ex: Ação, Comédia, Drama)
     */
    public Genero(String nome) {
        this.nome = nome;
        this.listaDeVideos = new ArrayList<>();
    }

    // ========================
    //    Métodos de Acesso
    // ========================

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Video> getListaDeVideos() {
        return listaDeVideos;
    }

    public void setListaDeVideos(List<Video> listaDeVideos) {
        this.listaDeVideos = listaDeVideos;
    }

    // ========================
    //   Métodos Utilitários
    // ========================

    /**
     * Adiciona um vídeo à lista de vídeos deste gênero.
     *
     * @param video Vídeo a ser adicionado
     */
    public void adicionarVideo(Video video) {
        this.listaDeVideos.add(video);
    }

    /**
     * Remove um vídeo da lista de vídeos deste gênero.
     *
     * @param video Vídeo a ser removido
     * @return true se o vídeo foi removido, false caso contrário
     */
    public boolean removerVideo(Video video) {
        return this.listaDeVideos.remove(video);
    }

    /**
     * Retorna a quantidade de vídeos associados a este gênero.
     *
     * @return Número de vídeos na lista
     */
    public int totalDeVideos() {
        return this.listaDeVideos.size();
    }

    @Override
    public String toString() {
        return "Genero{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", totalDeVideos=" + listaDeVideos.size() +
                '}';
    }
}
