/**
 * POO - Projeto1 (2a etapa) - FrameStack
 * 
 * Apagar.java - Apaga um objeto que possui relacionamento(s) no banco.
 * Neste caso, apaga o Vídeo 'Forrest Gump', que possui relacionamentos com os gêneros 'Drama' e 'Comédia'.
 * 
 * Como cascadeOnDelete é falso (configurado no Util), as referências bidirecionais
 * nos gêneros relacionados são desfeitas antes da exclusão do vídeo, garantindo
 * a integridade referencial do banco de dados sem apagar os gêneros em cascata.
 */

package appconsole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Genero;
import modelo.Video;
import util.Util;

public class Apagar {

	public static void main(String[] args) {
		Util.conectar();
		ObjectContainer manager = Util.getManager();

		System.out.println("=== EXECUTANDO APAGAR (EXCLUSÃO DE OBJETO COM RELACIONAMENTOS) ===");

		// Localizar o vídeo 'Forrest Gump' via consulta SODA
		Query consulta = manager.query();
		consulta.constrain(Video.class);
		consulta.descend("titulo").constrain("Forrest Gump");
		List<Video> videos = consulta.execute();

		if (videos.isEmpty()) {
			System.out.println("O vídeo 'Forrest Gump' não foi encontrado no banco (já foi apagado anteriormente).");
			Util.desconectar();
			System.out.println("fim do programa");
			return;
		}

		Video forrest = videos.get(0);

		// Exibir informações do objeto antes de apagar
		System.out.println("Objeto localizado para exclusão:");
		System.out.println("  ID: " + forrest.getId());
		System.out.println("  Título: " + forrest.getTitulo());
		System.out.println("  Data de Lançamento: " + forrest.getData());
		System.out.println("  Nota: " + forrest.getNota());
		System.out.println("  Gêneros associados (" + forrest.totalDeGeneros() + "): " + 
				forrest.getListaDeGeneros().stream().map(Genero::getNome).collect(Collectors.joining(", ")));

		// -------------------------------------------------------------------
		// Desfazer os relacionamentos nos objetos associados (Gêneros)
		// Como cascadeOnDelete = false, devemos remover a referência deste vídeo
		// em cada gênero associado e atualizar o gênero no banco com manager.store()
		// -------------------------------------------------------------------
		System.out.println("\nDesfazendo relacionamentos com os gêneros...");
		List<Genero> generosAssociados = new ArrayList<>(forrest.getListaDeGeneros());

		for (Genero g : generosAssociados) {
			System.out.println("  Removendo referência de 'Forrest Gump' do gênero '" + g.getNome() + "'...");
			g.removerVideo(forrest); // remove de ambos os lados da associação
			manager.store(g);        // persiste a lista atualizada do gênero no banco
		}

		// Limpar a lista do próprio vídeo
		forrest.getListaDeGeneros().clear();

		// Apagar o objeto Vídeo do banco de dados
		System.out.println("Apagando o vídeo '" + forrest.getTitulo() + "' do banco...");
		manager.delete(forrest);
		manager.commit();

		System.out.println("\nVídeo apagado com sucesso!");

		// -------------------------------------------------------------------
		// Verificação pós-exclusão
		// -------------------------------------------------------------------
		System.out.println("\n--- Verificação pós-exclusão ---");
		Query checagem = manager.query();
		checagem.constrain(Video.class);
		checagem.descend("titulo").constrain("Forrest Gump");
		List<Video> resultadoChecagem = checagem.execute();

		if (resultadoChecagem.isEmpty()) {
			System.out.println("Confirmação: O vídeo 'Forrest Gump' não existe mais no banco.");
		} else {
			System.out.println("Atenção: O vídeo ainda consta no banco.");
		}

		// Verificar o estado atual dos gêneros afetados
		System.out.println("Estado dos gêneros afetados:");
		for (Genero g : generosAssociados) {
			String titulos = g.getListaDeVideos().stream()
					.map(Video::getTitulo)
					.collect(Collectors.joining(", "));
			System.out.println("  Gênero '" + g.getNome() + "' agora possui " + g.totalDeVideos() + 
					" vídeo(s): " + (titulos.isEmpty() ? "nenhum" : titulos));
		}

		Util.desconectar();
		System.out.println("\nfim do programa");
	}

}
