package visual;
import model.Backup;


@SuppressWarnings("serial")
public class JanelaProgresso extends javax.swing.JDialog {
	private javax.swing.JButton botaoCancelar;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JProgressBar jProgressBar1;
	private Thread thread;
	
	public JanelaProgresso(java.awt.Frame parent, String title, Thread thread) {
		super(parent, title);
		initComponents();
		this.thread = thread;
	}

	private void initComponents() {

		botaoCancelar = new javax.swing.JButton();
		jProgressBar1 = new javax.swing.JProgressBar();
		jProgressBar1.setIndeterminate(true);
		jLabel1 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(new java.awt.Rectangle(0, 0, 400, 150));
		setMaximumSize(new java.awt.Dimension(400, 150));
		setMinimumSize(new java.awt.Dimension(200, 75));

		botaoCancelar.setText("Cancelar");
		botaoCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				botaoCancelarActionPerformed(evt);
			}
		});

		jLabel1.setText("Aguarde, enquanto a operação é realizada...");

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
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addGap(0,
																		0,
																		Short.MAX_VALUE)
																.addComponent(
																		botaoCancelar))
												.addComponent(
														jProgressBar1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		jLabel1,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		365,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(0,
																		0,
																		Short.MAX_VALUE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(jLabel1)
						.addGap(18, 18, 18)
						.addComponent(jProgressBar1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(botaoCancelar)
						.addContainerGap()));

		pack();
	}

	private void botaoCancelarActionPerformed(java.awt.event.ActionEvent evt) {
		thread.interrupt();
		this.dispose();
	}
	
	public void iniciarProgressBar(Backup bck) {
		jProgressBar1.setMinimum(0);
		jProgressBar1.setMaximum(bck.getNumTotalFiles());
		jProgressBar1.setValue(bck.getProgresso());
		jProgressBar1.setIndeterminate(false);
	}
	public javax.swing.JProgressBar getBar() {
		return jProgressBar1;
	}
	
}