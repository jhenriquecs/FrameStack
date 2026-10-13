import modelo.Genero;
import modelo.Video;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== INICIANDO TESTES DO SISTEMA DE VIDEOTECA ===\n");

        // 1. Criando os Gêneros
        System.out.println("--- Criando Gêneros ---");
        Genero acao = new Genero(1, "Ação");
        Genero ficcao = new Genero(2, "Ficção Científica");
        Genero comedia = new Genero(3, "Comédia");

        System.out.println(acao);
        System.out.println(ficcao);
        System.out.println(comedia);
        System.out.println();

        // 2. Criando os Vídeos
        System.out.println("--- Criando Vídeos ---");
        Video v1 = new Video(101, "Matrix", LocalDate.of(1999, 3, 31), "www.matrix.com", "14 Anos");
        Video v2 = new Video(102, "De Volta para o Futuro", LocalDate.of(1985, 7, 3), "www.bttf.com", "Livre");
        Video v3 = new Video(103, "O Exterminador do Futuro 2", LocalDate.of(1991, 7, 3), "www.t2.com", "16 Anos");

        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);
        System.out.println();

        // 3. Testando as Associações (Adicionando Gêneros aos Vídeos)
        System.out.println("--- Adicionando Gêneros aos Vídeos ---");

        // Matrix é Ação e Ficção
        v1.adicionarGenero(acao);
        v1.adicionarGenero(ficcao);

        // De Volta para o Futuro é Ficção e Comédia
        v2.adicionarGenero(ficcao);
        v2.adicionarGenero(comedia);

        // T2 é Ação e Ficção
        v3.adicionarGenero(acao);
        v3.adicionarGenero(ficcao);

        System.out.println("Status do vídeo 1 (Matrix): " + v1.totalDeGeneros() + " gêneros -> " + v1);
        System.out.println("Status do vídeo 2 (BTTF): " + v2.totalDeGeneros() + " gêneros -> " + v2);
        System.out.println("Status do vídeo 3 (T2): " + v3.totalDeGeneros() + " gêneros -> " + v3);
        System.out.println();

        // 4. Verificando a Associação Bidirecional (Os gêneros receberam os vídeos?)
        System.out.println("--- Verificando a Lista de Vídeos dentro dos Gêneros ---");
        System.out.println("Gênero Ação tem " + acao.totalDeVideos() + " vídeos: ");
        for(Video v : acao.getListaDeVideos()){
            System.out.println("  - " + v.getTitulo());
        }

        System.out.println("\nGênero Ficção Científica tem " + ficcao.totalDeVideos() + " vídeos: ");
        for(Video v : ficcao.getListaDeVideos()){
            System.out.println("  - " + v.getTitulo());
        }

        System.out.println("\nGênero Comédia tem " + comedia.totalDeVideos() + " vídeos: ");
        for(Video v : comedia.getListaDeVideos()){
            System.out.println("  - " + v.getTitulo());
        }
        System.out.println();

        // 5. Testando a Remoção e Atualização Bidirecional
        System.out.println("--- Testando Remoção de Gênero ---");
        System.out.println("Removendo 'Ficção Científica' do filme 'Matrix'...");

        boolean removeu = v1.removerGenero(ficcao);
        System.out.println("Remoção com sucesso? " + removeu);

        System.out.println("Novos Gêneros de Matrix: " + v1);

        System.out.println("Verificando se Matrix sumiu da lista do Gênero Ficção Científica:");
        System.out.println("Gênero Ficção Científica agora tem " + ficcao.totalDeVideos() + " vídeos:");
        for(Video v : ficcao.getListaDeVideos()){
            System.out.println("  - " + v.getTitulo());
        }
        System.out.println();

        // 6. Testando Duplicação (Não deve deixar adicionar o mesmo gênero duas vezes)
        System.out.println("--- Testando Adição Duplicada ---");
        System.out.println("Tentando adicionar 'Ação' novamente ao filme 'T2'...");
        int totalAntes = v3.totalDeGeneros();
        v3.adicionarGenero(acao);
        int totalDepois = v3.totalDeGeneros();

        if(totalAntes == totalDepois) {
            System.out.println("Sucesso: O sistema ignorou a duplicação.");
        } else {
            System.out.println("Falha: O sistema adicionou o gênero duplicado.");
        }
        System.out.println(v3);
    }
}