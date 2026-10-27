/**
 * POO - Projeto1 (2a etapa) - FrameStack
 */

package appconsole;

import com.db4o.ObjectContainer;

import modelo.Genero;
import modelo.Video;
import util.Util;

public class Cadastrar {

	public static void main(String[] args) {
		Util.conectar();
		ObjectContainer manager = Util.getManager(); // gerenciador da persistencia de objetos

		// Criando generos
		// *************************************************
		Genero acao = new Genero("Ação");
		Genero ficcao = new Genero("Ficção Científica");
		Genero comedia = new Genero("Comédia");
		Genero drama = new Genero("Drama");

		// Criando videos
		// *************************************************
		Video matrix = new Video("Matrix", "31/03/1999", "www.matrix.com", 8.7);
		Video bttf = new Video("De Volta para o Futuro", "03/07/1985", "www.bttf.com", 8.5);
		Video t2 = new Video("O Exterminador do Futuro 2", "03/07/1991", "www.t2.com", 8.6);
		Video forrest = new Video("Forrest Gump", "06/07/1994", "www.forrestgump.com", 8.8);

		// relacionando videos e generos (associacao bidirecional)
		// *************************************************
		matrix.adicionarGenero(acao);
		matrix.adicionarGenero(ficcao);
		bttf.adicionarGenero(ficcao);
		bttf.adicionarGenero(comedia);
		t2.adicionarGenero(acao);
		t2.adicionarGenero(ficcao);
		forrest.adicionarGenero(drama);
		forrest.adicionarGenero(comedia);

		// *************************************************
		// Persistir objetos no banco de dados
		// (os generos sao gravados junto, por estarem relacionados)
		// *************************************************
		manager.store(matrix);
		manager.commit();
		manager.store(bttf);
		manager.commit();
		manager.store(t2);
		manager.commit();
		manager.store(forrest);
		manager.commit();

		Util.desconectar();
		System.out.println("cadastrou.");
		System.out.println("fim do programa");
	}

}
