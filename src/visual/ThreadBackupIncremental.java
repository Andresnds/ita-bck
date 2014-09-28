package visual;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import model.Backup;
import model.BackupCompleto;
import model.BackupIncremental;
import model.Buffer;
import model.EspacoBackup;
import model.TipoBackup;

public class ThreadBackupIncremental extends Thread {
	private EspacoBackup espaco;
	private JTable tabelaBackups;
	private JanelaPrincipal janela;
	private boolean finished;
	private JanelaProgresso dialog;

	public ThreadBackupIncremental(EspacoBackup espaco, JTable tabelaBackups,
			JanelaPrincipal janela) {
		this.espaco = espaco;
		this.tabelaBackups = tabelaBackups;
		this.janela = janela;
	}

	@Override
	public void run() {
		try {
			Backup bckVelho = espaco.getBackups().get(
					tabelaBackups.getSelectedRow());
			Backup bckNovo;
			if (bckVelho.getTipo() == TipoBackup.COMPLETO)
				bckNovo = new BackupIncremental((BackupCompleto) bckVelho);
			else
				bckNovo = new BackupIncremental((BackupIncremental) bckVelho);
			if (bckNovo.getTamanho() < espaco.getFreeSpace()) {
				Buffer buffer = new Buffer(dialog.getBar(), bckNovo);
				bckNovo.setBuffer(buffer);
				bckNovo.salvarArquivos();
				dialog.iniciarProgressBar(bckNovo);
				espaco.salvarEspaco();
				janela.atualizarTabela();
			} else {
				JOptionPane.showMessageDialog(janela, "Espaço insuficiente!",
						"Erro", JOptionPane.ERROR_MESSAGE);
				if (espaco.getBackups().contains(bckNovo)) {
					espaco.getBackups().remove(bckNovo);
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(janela, "Conflito entre backups!",
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
		finished = true;

		janela.atualizarTabela();
		dialog.dispose();
	}

	public boolean isFinished() {
		return finished;
	}

	public void setDialog(JanelaProgresso dialog) {
		this.dialog = dialog;
	}
}
