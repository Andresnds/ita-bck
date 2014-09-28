package visual;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import model.Backup;
import model.Buffer;
import model.EspacoBackup;

public class ThreadRestaurar extends Thread

{
	private JFileChooser fc;
	private EspacoBackup espaco;
	private JTable tabelaBackups;
	private JanelaPrincipal janela;
	private JanelaProgresso dialog;
	private boolean finished = false;

	public ThreadRestaurar(JFileChooser fc, EspacoBackup espaco,
			JTable tabelaBackups, JanelaPrincipal janela) {
		this.fc = fc;
		this.espaco = espaco;
		this.tabelaBackups = tabelaBackups;
		this.janela = janela;
	}

	public void run() {

		File file = fc.getSelectedFile();
		Backup bck = espaco.getBackups().get(tabelaBackups.getSelectedRow());

		try {
			dialog.iniciarProgressBar(bck);
			Buffer buffer = new Buffer(dialog.getBar(), bck);
			bck.setBuffer(buffer);
			bck.restaurar(file.toPath());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(janela,
					"Erro ao tentar restaurar. Tente outra pasta. ", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}

		finished = true;
		janela.atualizarTabela();
		dialog.dispose();
	}

	public void setDialog(JanelaProgresso dialog) {
		this.dialog = dialog;
	}

	public boolean isFinished() {
		return finished;
	}
}
