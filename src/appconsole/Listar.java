/**
 * POO - Projeto1 (2a etapa) - FrameStack
 */

package appconsole;

import java.util.List;
import java.util.stream.Collectors;

import com.db4o.ObjectContainer;

import modelo.Genero;
import modelo.Short;
import modelo.Video;
import util.Util;

public class Listar {

	public static void main(String[] args) {
		Util.conectar();
		ObjectContainer manager = Util.getManager();

		// a consulta por Video.class retorna videos e shorts (polimorfismo)
		System.out.println("------------ VIDEOS ------------");
		List<Video> resultados1 = manager.query(Video.class);
		if (resultados1.isEmpty()) {
			System.out.println("Nenhum vídeo cadastrado no banco.");
		} else {
			for (Video v : resultados1) {
				System.out.println(v);
			}
		}

		// a consulta por Short.class retorna somente os objetos da subclasse
		System.out.println("\n------------ SHORTS ------------");
		List<Short> resultados3 = manager.query(Short.class);
		if (resultados3.isEmpty()) {
			System.out.println("Nenhum short cadastrado no banco.");
		} else {
			for (Short s : resultados3) {
				System.out.println(s);
			}
		}

		System.out.println("\n------------ GENEROS ------------");
		List<Genero> resultados2 = manager.query(Genero.class);
		if (resultados2.isEmpty()) {
			System.out.println("Nenhum gênero cadastrado no banco.");
		} else {
			for (Genero g : resultados2) {
				String videos = g.getListaDeVideos().stream()
						.map(Video::getTitulo)
						.collect(Collectors.joining(", "));
				System.out.println(g + "\n     videos=" + (videos.isEmpty() ? "nenhum" : videos));
			}
		}

		Util.desconectar();
		System.out.println("fim do programa");
	}

}
