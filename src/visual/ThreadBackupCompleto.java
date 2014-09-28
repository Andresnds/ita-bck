package visual;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import model.Backup;
import model.BackupCompleto;
import model.Buffer;
import model.EspacoBackup;

public class ThreadBackupCompleto extends Thread {
	private File[] selectedFiles;
	private String nome;
	private EspacoBackup espaco;
	private JanelaPrincipal janela;
	private JanelaProgresso dialog;
	private boolean finished;

	public ThreadBackupCompleto(File[] selectedFiles, String nome,
			EspacoBackup espaco, JanelaPrincipal janela) {
		this.selectedFiles = selectedFiles;
		this.nome = nome;
		this.espaco = espaco;
		this.janela = janela;
	}

	@Override
	public void run() {
		List<File> source = new ArrayList<File>();
		for (File f : selectedFiles) {
			source.add(f);
		}
		Backup bck = null;
		try {
			bck = new BackupCompleto(nome, espaco, source);
			if (bck.getTamanho() < espaco.getFreeSpace()) {
				dialog.iniciarProgressBar(bck);
				Buffer buffer = new Buffer(dialog.getBar(), bck);
				bck.setBuffer(buffer);
				bck.salvarArquivos();
				espaco.salvarEspaco();
				janela.atualizarTabela();
			} else {
				JOptionPane.showMessageDialog(janela, "Espaço insuficiente!",
						"Erro", JOptionPane.ERROR_MESSAGE);
				if (espaco.getBackups().contains(bck)) {
					espaco.getBackups().remove(bck);
				}
			}
		} catch (FileAlreadyExistsException e) {
			JOptionPane.showMessageDialog(janela,
					"Você tentou sobreescrever um backup", "Erro",
					JOptionPane.ERROR_MESSAGE);
			if (espaco.getBackups().contains(bck)) {
				espaco.getBackups().remove(bck);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(janela, "Erro inesperado:", "Erro",
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
