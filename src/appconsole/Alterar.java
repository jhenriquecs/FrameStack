/**
 * POO - Projeto1 (2a etapa) - FrameStack
 * 
 * Alterar.java - remove um relacionamento existente entre dois objetos do banco.
 * Neste caso, remove o relacionamento N:N entre o Vídeo 'Matrix' e o Gênero 'Ficção Científica'.
 */

package appconsole;

import java.util.List;
import java.util.stream.Collectors;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Genero;
import modelo.Video;
import util.Util;

public class Alterar {

	public static void main(String[] args) {
		Util.conectar();
		ObjectContainer manager = Util.getManager();

		System.out.println("=== EXECUTANDO ALTERAÇÃO (REMOÇÃO DE RELACIONAMENTO) ===");

		// Localizar o vídeo 'Matrix' no banco via consulta SODA
		Query consultaVideo = manager.query();
		consultaVideo.constrain(Video.class);
		consultaVideo.descend("titulo").constrain("Matrix");
		List<Video> videos = consultaVideo.execute();

		if (videos.isEmpty()) {
			System.out.println("Vídeo 'Matrix' não encontrado no banco.");
			Util.desconectar();
			System.out.println("fim do programa");
			return;
		}

		Video matrix = videos.get(0);

		// Localizar o gênero 'Ficção Científica' dentro dos gêneros associados ao vídeo
		Genero ficcao = matrix.localizarGenero("Ficção Científica");

		if (ficcao == null) {
			System.out.println("O relacionamento entre 'Matrix' e 'Ficção Científica' não foi encontrado (já pode ter sido removido).");
			System.out.println("Gêneros atuais de Matrix: " + 
					matrix.getListaDeGeneros().stream().map(Genero::getNome).collect(Collectors.joining(", ")));
			Util.desconectar();
			System.out.println("fim do programa");
			return;
		}

		// Exibir estado antes da remoção
		System.out.println("Antes da alteração:");
		System.out.println("  Vídeo: " + matrix.getTitulo() + " -> Gêneros: " + 
				matrix.getListaDeGeneros().stream().map(Genero::getNome).collect(Collectors.joining(", ")));
		System.out.println("  Gênero: " + ficcao.getNome() + " -> Vídeos: " + 
				ficcao.getListaDeVideos().stream().map(Video::getTitulo).collect(Collectors.joining(", ")));

		// Remover relacionamento bidirecional entre o vídeo e o gênero
		System.out.println("\nRemovendo o relacionamento entre 'Matrix' e 'Ficção Científica'...");
		matrix.removerGenero(ficcao);

		// Atualizar e persistir ambos os objetos modificados no banco
		manager.store(matrix);
		manager.store(ficcao);
		manager.commit();

		// Exibir estado após a remoção
		System.out.println("\nRelacionamento removido com sucesso!");
		System.out.println("Depois da alteração:");
		System.out.println("  Vídeo: " + matrix.getTitulo() + " -> Gêneros: " + 
				(matrix.getListaDeGeneros().isEmpty() ? "nenhum" : 
					matrix.getListaDeGeneros().stream().map(Genero::getNome).collect(Collectors.joining(", "))));
		System.out.println("  Gênero: " + ficcao.getNome() + " -> Vídeos: " + 
				(ficcao.getListaDeVideos().isEmpty() ? "nenhum" : 
					ficcao.getListaDeVideos().stream().map(Video::getTitulo).collect(Collectors.joining(", "))));

		Util.desconectar();
		System.out.println("\nfim do programa");
	}

}
