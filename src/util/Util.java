package util;

import javax.swing.JOptionPane;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

import modelo.Genero;
import modelo.Video;

public class Util {
	private static ObjectContainer manager;
	private static String ip="localhost";

	public static void conectar() {
		if (manager != null)
			return; // ja tem uma conexao
		
		try {
			conectarBancoLocal(); // banco local (pasta do projeto)
			// ativar controle de IDs automáticos
			GeradorID.ativar(manager); // ativa geração de IDs automáticos para as classes com atributo "int id"
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco " + ip + "\n" + e.getMessage());
			System.exit(0);
		}
	}

	private static void conectarBancoLocal() {
		if (manager != null)
			return; // ja tem uma conexao

		// ---------------------------------------------------------------
		// configurar, criar e abrir banco local (pasta do projeto)
		// ---------------------------------------------------------------
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().messageLevel(0); // mensagens na tela 0(desliga),1,2,3...

		// habilitar cascata na alteração, remoção e leitura
		config.common().objectClass(Genero.class).cascadeOnDelete(false);
		config.common().objectClass(Genero.class).cascadeOnUpdate(true);
		config.common().objectClass(Genero.class).cascadeOnActivate(true);
		config.common().objectClass(Video.class).cascadeOnDelete(false);
		config.common().objectClass(Video.class).cascadeOnUpdate(true);
		config.common().objectClass(Video.class).cascadeOnActivate(true);
		config.common().activationDepth(Integer.MAX_VALUE); // profundidade de ativacao para acessar os objetos

		// conexao local
		// --------------
		try {
			manager = Db4oEmbedded.openFile(config, "framestack.db4o");
			System.out.println("banco conectado. ");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco local \n" + e.getMessage());
			System.exit(0);
		}
	}

	public static void desconectar() {
		if (manager != null) {
			manager.close();
			manager = null;
		}
	}

	public static ObjectContainer getManager() {
		return manager;
	}

	public static String getIP() {
		return ip;
	}

}// fim da classe Util
