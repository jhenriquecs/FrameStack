/**
 * Eng.Soft/POO - Visualizador do banco db4o (somente leitura)
 *
 * Mostra o conteudo do banco em arvore, parecido com o OME do Eclipse:
 *   classe -> objetos -> campos -> objetos referenciados -> ...
 * Duplo clique (ou a seta) expande o no. Os nos sao carregados sob demanda.
 */

package appconsole;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.db4o.ObjectContainer;
import com.db4o.ext.StoredClass;

import util.Util;

public class Visualizar {
	private static final String CARREGANDO = "carregando...";

	private JFrame frame;
	private JTree arvore;
	private DefaultTreeModel modelo;
	private JTextArea detalhes;

	public static void main(String[] args) {
		Util.conectar();
		SwingUtilities.invokeLater(() -> new Visualizar().mostrar());
	}

	private void mostrar() {
		modelo = new DefaultTreeModel(criarRaiz(Util.getManager()));
		arvore = new JTree(modelo);
		arvore.setRootVisible(false);
		arvore.setShowsRootHandles(true);
		arvore.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent e) {
				DefaultMutableTreeNode no = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (expandir(no))
					modelo.nodeStructureChanged(no);
			}

			public void treeWillCollapse(TreeExpansionEvent e) {
			}
		});
		arvore.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode no = (DefaultMutableTreeNode) arvore.getLastSelectedPathComponent();
			boolean temItem = no != null && no.getUserObject() instanceof Item;
			detalhes.setText(temItem ? descrever((Item) no.getUserObject()) : "");
			detalhes.setCaretPosition(0);
		});

		detalhes = new JTextArea();
		detalhes.setEditable(false);
		detalhes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

		JButton recarregar = new JButton("Recarregar");
		recarregar.addActionListener(e -> {
			modelo.setRoot(criarRaiz(Util.getManager()));
			detalhes.setText("");
		});
		JPanel topo = new JPanel(new BorderLayout());
		topo.add(recarregar, BorderLayout.WEST);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(arvore),
				new JScrollPane(detalhes));
		split.setResizeWeight(0.5);

		frame = new JFrame("Visualizador db4o - framestack.db4o");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				Util.desconectar(); // libera o arquivo do banco
				System.out.println("fim do programa");
			}
		});
		frame.add(topo, BorderLayout.NORTH);
		frame.add(split, BorderLayout.CENTER);
		frame.setSize(1000, 650);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	// ---------------------------------------------------------------
	// montagem da arvore
	// ---------------------------------------------------------------

	/** raiz com um no para cada classe da aplicacao gravada no banco */
	static DefaultMutableTreeNode criarRaiz(ObjectContainer manager) {
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(new Item("banco", null, Tipo.RAIZ));
		for (StoredClass sc : manager.ext().storedClasses()) {
			String nome = sc.getName();
			if (classeInterna(nome))
				continue;
			long[] ids = sc.getIDs();
			List<Object> objetos = new ArrayList<>();
			for (long id : ids) {
				Object obj = manager.ext().getByID(id);
				if (obj == null)
					continue;
				manager.ext().activate(obj, Integer.MAX_VALUE);
				// getIDs() tambem retorna subclasses; mostra so os da propria classe
				if (obj.getClass().getName().equals(nome))
					objetos.add(obj);
			}
			String rotulo = nome + "  (" + objetos.size() + ")";
			raiz.add(comFilhosPendentes(new Item(rotulo, objetos, Tipo.CLASSE)));
		}
		return raiz;
	}

	/** carrega os filhos de um no na primeira expansao; retorna true se carregou */
	static boolean expandir(DefaultMutableTreeNode no) {
		if (no.getChildCount() != 1)
			return false;
		DefaultMutableTreeNode primeiro = (DefaultMutableTreeNode) no.getFirstChild();
		if (!CARREGANDO.equals(primeiro.getUserObject()))
			return false;

		no.removeAllChildren();
		Item item = (Item) no.getUserObject();
		switch (item.tipo) {
		case CLASSE:
			for (Object obj : (List<?>) item.valor)
				no.add(noObjeto(null, obj, no));
			break;
		case OBJETO:
			for (Field f : campos(item.valor.getClass())) {
				try {
					f.setAccessible(true);
					no.add(noObjeto(f.getName(), f.get(item.valor), no));
				} catch (Exception e) {
					no.add(new DefaultMutableTreeNode(new Item(f.getName() + " = <erro: " + e.getMessage() + ">",
							null, Tipo.VALOR)));
				}
			}
			break;
		case COLECAO:
			int i = 0;
			for (Object elemento : elementos(item.valor))
				no.add(noObjeto("[" + (i++) + "]", elemento, no));
			break;
		default:
			break;
		}
		return true;
	}

	/** cria o no de um valor; objetos e colecoes ficam expansiveis */
	private static DefaultMutableTreeNode noObjeto(String nome, Object valor, DefaultMutableTreeNode pai) {
		String prefixo = nome == null ? "" : nome + " = ";

		if (valor == null || simples(valor.getClass())) {
			String texto = valor instanceof String ? "\"" + valor + "\"" : String.valueOf(valor);
			return new DefaultMutableTreeNode(new Item(prefixo + texto, valor, Tipo.VALOR));
		}

		if (colecao(valor)) {
			String rotulo = (nome == null ? "" : nome + " ") + qtd(elementos(valor).size());
			Item item = new Item(rotulo, valor, Tipo.COLECAO);
			return elementos(valor).isEmpty() ? new DefaultMutableTreeNode(item) : comFilhosPendentes(item);
		}

		String rotulo = prefixo + resumo(valor);
		if (ancestral(pai, valor))
			return new DefaultMutableTreeNode(new Item(rotulo + "  (ciclo)", valor, Tipo.VALOR));
		return comFilhosPendentes(new Item(rotulo, valor, Tipo.OBJETO));
	}

	private static DefaultMutableTreeNode comFilhosPendentes(Item item) {
		DefaultMutableTreeNode no = new DefaultMutableTreeNode(item);
		no.add(new DefaultMutableTreeNode(CARREGANDO));
		return no;
	}

	/** o objeto ja aparece mais acima no caminho? (evita expansao infinita) */
	private static boolean ancestral(DefaultMutableTreeNode pai, Object valor) {
		for (TreeNode n = pai; n != null; n = n.getParent()) {
			Object uo = ((DefaultMutableTreeNode) n).getUserObject();
			if (uo instanceof Item && ((Item) uo).valor == valor)
				return true;
		}
		return false;
	}

	/** texto do painel da direita */
	static String descrever(Item item) {
		if (item.tipo != Tipo.OBJETO)
			return item.rotulo;

		Object obj = item.valor;
		StringBuilder sb = new StringBuilder(obj.getClass().getName()).append("\n\n");
		for (Field f : campos(obj.getClass())) {
			try {
				f.setAccessible(true);
				Object v = f.get(obj);
				String texto;
				if (v == null || simples(v.getClass()))
					texto = String.valueOf(v);
				else if (colecao(v))
					texto = qtd(elementos(v).size());
				else
					texto = resumo(v);
				sb.append(f.getName()).append(" = ").append(texto).append("\n");
			} catch (Exception e) {
				sb.append(f.getName()).append(" = <erro>\n");
			}
		}
		sb.append("\ntoString():\n").append(obj);
		return sb.toString();
	}

	/** "Classe: toString()" em uma linha so, limitado a 120 caracteres */
	private static String resumo(Object obj) {
		String texto = obj.getClass().getSimpleName() + ": " + String.valueOf(obj).trim().replaceAll("\\s+", " ");
		return texto.length() > 120 ? texto.substring(0, 117) + "..." : texto;
	}

	private static String qtd(int n) {
		return "(" + n + (n == 1 ? " item)" : " itens)");
	}

	// ---------------------------------------------------------------
	// reflexao
	// ---------------------------------------------------------------

	private static boolean classeInterna(String nome) {
		return nome.startsWith("com.db4o.") || nome.startsWith("java.") || nome.startsWith("javax.")
				|| nome.startsWith("jdk.") || nome.startsWith("sun.");
	}

	private static boolean simples(Class<?> c) {
		return c.isPrimitive() || c.isEnum() || c == String.class || c == Character.class
				|| c == Boolean.class || Number.class.isAssignableFrom(c) || c.getName().startsWith("java.time.")
				|| java.util.Date.class.isAssignableFrom(c);
	}

	private static boolean colecao(Object v) {
		return v instanceof Collection || v instanceof Map || v.getClass().isArray();
	}

	private static List<Object> elementos(Object v) {
		List<Object> lista = new ArrayList<>();
		if (v instanceof Collection)
			lista.addAll((Collection<?>) v);
		else if (v instanceof Map)
			lista.addAll(((Map<?, ?>) v).entrySet());
		else
			for (int i = 0; i < Array.getLength(v); i++)
				lista.add(Array.get(v, i));
		return lista;
	}

	/** campos de instancia da classe e das superclasses */
	private static List<Field> campos(Class<?> c) {
		List<Field> lista = new ArrayList<>();
		for (; c != null && c != Object.class; c = c.getSuperclass())
			for (Field f : c.getDeclaredFields())
				if (!Modifier.isStatic(f.getModifiers()) && !f.isSynthetic())
					lista.add(f);
		return lista;
	}

	// ---------------------------------------------------------------

	enum Tipo {
		RAIZ, CLASSE, OBJETO, COLECAO, VALOR
	}

	static class Item {
		final String rotulo;
		final Object valor;
		final Tipo tipo;

		Item(String rotulo, Object valor, Tipo tipo) {
			this.rotulo = rotulo;
			this.valor = valor;
			this.tipo = tipo;
		}

		@Override
		public String toString() {
			return rotulo;
		}
	}
}
