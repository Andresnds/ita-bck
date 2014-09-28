package visual;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Backup;
import model.EspacoBackup;

@SuppressWarnings("serial")
public class JanelaPrincipal extends javax.swing.JFrame {
	private javax.swing.JButton buttonBackupCompleto;
	private javax.swing.JButton buttonIncrementarBackup;
	private javax.swing.JButton buttonMudarEspaco;
	private javax.swing.JButton buttonRestaurar;
	private javax.swing.JButton buttonSair;
	private javax.swing.JButton buttonSobre;
	private javax.swing.JFileChooser fc;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel labelEspaco;
	private javax.swing.JLabel labelEspacoDisponivel;
	private javax.swing.JTable tabelaBackups;
	private javax.swing.JTextField textFieldEspacoBackup;
	private EspacoBackup espaco;

	public JanelaPrincipal() {

		super("ITA BCK");
		initComponents();
		JOptionPane.showMessageDialog(this,
				"Escolha um diretorio onde você salvará seus backups");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(JanelaPrincipal.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				espaco = EspacoBackup.inicializar(file.toPath());
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(this, "Dados corrompidos");
				System.exit(1);
			}
			textFieldEspacoBackup.setText(file.toString());
			labelEspacoDisponivel.setText("Disponível: "
					+ displayHumanReadable(file.getFreeSpace()) + " / "
					+ displayHumanReadable(file.getTotalSpace()));

			atualizarTabela();
		} else {
			System.exit(0);
		}

	}

	public void atualizarTabela() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				Object[][] objetosTabela = new Object[espaco.getBackups()
						.size()][4];
				SimpleDateFormat df = new SimpleDateFormat(
						"dd/MM/yyyy hh:mm:ss");

				for (Backup bck : espaco.getBackups()) {
					objetosTabela[espaco.getBackups().indexOf(bck)][0] = bck
							.getNome();
					objetosTabela[espaco.getBackups().indexOf(bck)][1] = bck
							.getRaiz().toString();
					objetosTabela[espaco.getBackups().indexOf(bck)][2] = df
							.format(bck.getDataCriacao().getTime());
					objetosTabela[espaco.getBackups().indexOf(bck)][3] = bck
							.getTipo();
				}

				tabelaBackups.setModel(new javax.swing.table.DefaultTableModel(
						objetosTabela, new String[] { "Nome do Backup",
								"Diretorio", "Data", "Tipo" }) {
					@SuppressWarnings("rawtypes")
					Class[] types = new Class[] { java.lang.String.class,
							java.lang.String.class, java.lang.String.class,
							java.lang.String.class };
					boolean[] canEdit = new boolean[] { false, false, false,
							false };

					@SuppressWarnings({ "rawtypes", "unchecked" })
					public Class getColumnClass(int columnIndex) {
						return types[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return canEdit[columnIndex];

					}
				});
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {

		fc = new javax.swing.JFileChooser();
		jScrollPane1 = new javax.swing.JScrollPane();
		tabelaBackups = new javax.swing.JTable();
		buttonMudarEspaco = new javax.swing.JButton();
		buttonSair = new javax.swing.JButton();
		buttonBackupCompleto = new javax.swing.JButton();
		buttonIncrementarBackup = new javax.swing.JButton();
		buttonRestaurar = new javax.swing.JButton();
		buttonSobre = new javax.swing.JButton();
		textFieldEspacoBackup = new javax.swing.JTextField();
		labelEspacoDisponivel = new javax.swing.JLabel();
		labelEspaco = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		tabelaBackups
				.setModel(new javax.swing.table.DefaultTableModel(
						new Object[][] { { null, null, null, null },
								{ null, null, null, null },
								{ null, null, null, null },
								{ null, null, null, null },
								{ null, null, null, null } }, new String[] {
								"Nome do Backup", "Diretorio", "Data", "Tipo" }) {
					@SuppressWarnings("rawtypes")
					Class[] types = new Class[] { java.lang.String.class,
							java.lang.String.class, java.lang.String.class,
							java.lang.String.class };
					boolean[] canEdit = new boolean[] { false, false, false,
							false };

					@SuppressWarnings("rawtypes")
					public Class getColumnClass(int columnIndex) {
						return types[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return canEdit[columnIndex];

					}
				});
		jScrollPane1.setViewportView(tabelaBackups);

		buttonMudarEspaco.setText("Mudar");
		buttonMudarEspaco
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						buttonMudarEspacoActionPerformed(evt);
					}
				});

		buttonSair.setText("Sair");
		buttonSair.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSairActionPerformed(evt);
			}
		});

		buttonBackupCompleto.setText("Novo Backup Completo");
		buttonBackupCompleto
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						buttonBackupCompletoActionPerformed(evt);
					}
				});

		buttonIncrementarBackup.setText("Incrementar Backup");
		buttonIncrementarBackup
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						buttonIncrementarBackupActionPerformed(evt);
					}
				});

		buttonRestaurar.setText("Restaurar Backup");
		buttonRestaurar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonRestaurarActionPerformed(evt);
			}
		});

		buttonSobre.setText("Sobre");
		buttonSobre.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSobreActionPerformed(evt);
			}
		});

		textFieldEspacoBackup.setEditable(false);
		textFieldEspacoBackup.setText("EspaçoBackup");

		labelEspacoDisponivel.setText("Disponível: 9999 KB / 9999 MB");

		labelEspaco.setText("Espaço de Backup:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jScrollPane1)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		labelEspaco)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		textFieldEspacoBackup,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		412,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(18, 18,
																		18)
																.addComponent(
																		labelEspacoDisponivel,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addGap(18, 18,
																		18)
																.addComponent(
																		buttonMudarEspaco))
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		buttonBackupCompleto)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(
																		buttonIncrementarBackup)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		buttonRestaurar)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(
																		buttonSobre)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		buttonSair)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(buttonMudarEspaco)
												.addComponent(
														textFieldEspacoBackup,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														labelEspacoDisponivel)
												.addComponent(labelEspaco))
								.addGap(9, 9, 9)
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										210, Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(buttonSair)
												.addComponent(
														buttonBackupCompleto)
												.addComponent(
														buttonIncrementarBackup)
												.addComponent(buttonRestaurar)
												.addComponent(buttonSobre))
								.addContainerGap()));

		pack();
	}// </editor-fold>

	private void buttonMudarEspacoActionPerformed(java.awt.event.ActionEvent evt) {
		JOptionPane.showMessageDialog(null,
				"Escolha um diretorio onde você salvará seus backups");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(JanelaPrincipal.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				espaco = EspacoBackup.inicializar(file.toPath());
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(this, "Dados corrompidos");
			}
			textFieldEspacoBackup.setText(file.toString());
			labelEspacoDisponivel.setText("Disponível: "
					+ displayHumanReadable(file.getFreeSpace()) + " / "
					+ displayHumanReadable(file.getTotalSpace()));
			atualizarTabela();
		}
	}

	private void buttonBackupCompletoActionPerformed(
			java.awt.event.ActionEvent evt) {

		String nome = JOptionPane.showInputDialog(this,
				"Digite o nome do Bakcup", "Digite aqui");

		if (nome != null) {

			JOptionPane.showMessageDialog(null,
					"Escolha as pastas para realizar o bakcup");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setMultiSelectionEnabled(true);

			int returnVal = fc.showOpenDialog(JanelaPrincipal.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				ThreadBackupCompleto tbc = new ThreadBackupCompleto(
						fc.getSelectedFiles(), nome, espaco, this);
				JanelaProgresso dialog = new JanelaProgresso(this,
						"Criando Bakcup", tbc);
				tbc.setDialog(dialog);
				dialog.setVisible(true);
				tbc.start();
			}
		}
	}

	private void buttonIncrementarBackupActionPerformed(
			java.awt.event.ActionEvent evt) {
		if (tabelaBackups.getSelectedRow() != -1) {
			ThreadBackupIncremental tbi = new ThreadBackupIncremental(espaco,
					tabelaBackups, this);
			JanelaProgresso dialog = new JanelaProgresso(this,
					"Incrementando Bakcup", tbi);
			tbi.setDialog(dialog);
			dialog.setVisible(true);
			tbi.start();

		} else {
			JOptionPane.showMessageDialog(this, "Selecione um backup");
		}

	}

	private void buttonRestaurarActionPerformed(java.awt.event.ActionEvent evt) {
		if (tabelaBackups.getSelectedRow() != -1) {
			JOptionPane.showMessageDialog(this,
					"Escolha um diretorio onde você restaurará seu backup");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setMultiSelectionEnabled(false);
			int returnVal = fc.showOpenDialog(JanelaPrincipal.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				ThreadRestaurar tr = new ThreadRestaurar(fc, espaco,
						tabelaBackups, this);
				JanelaProgresso dialog = new JanelaProgresso(this,
						"Restaurando...", tr);
				tr.setDialog(dialog);
				dialog.setVisible(true);
				tr.start();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Selecione um backup");
		}
	}

	private void buttonSobreActionPerformed(java.awt.event.ActionEvent evt) {
		JOptionPane
				.showMessageDialog(
						this,
						"Esse programa foi criado por André Saraiva, aluno de engenharia da computação, turma 15 do ITA, Instituto Tecnológico de Aeronáutica, como trabalho para a disciplina de Programação Orientada a Objetos, CES22.",
						"Sobre", JOptionPane.INFORMATION_MESSAGE);
	}

	private void buttonSairActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private String displayHumanReadable(long bytes) {
		if (bytes < 1000)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(1000));
		String pre = "kMGTPE".charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(1000, exp), pre);
	}

	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new JanelaPrincipal().setVisible(true);
			}
		});
	}

}
