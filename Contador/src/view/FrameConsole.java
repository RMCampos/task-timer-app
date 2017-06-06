package view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
import javax.swing.JFrame;

public class FrameConsole extends JFrame {

	PrintStream printStream;

	public FrameConsole(){
		initComponents();
		iniciarConsole();
		this.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
	}

	private void initComponents() {
		pnlSuperior = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		txaConsole = new javax.swing.JTextArea();
		pnlInferior = new javax.swing.JPanel();
		btnFechar = new javax.swing.JButton();
		btnLimpar = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		pnlSuperior.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		txaConsole.setEditable(false);
		txaConsole.setBackground(new java.awt.Color(0, 0, 0));
		txaConsole.setColumns(20);
		txaConsole.setForeground(new java.awt.Color(255, 255, 255));
		txaConsole.setRows(5);
		txaConsole.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jScrollPane1.setViewportView(txaConsole);

		javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
		pnlSuperior.setLayout(pnlSuperiorLayout);
		pnlSuperiorLayout.setHorizontalGroup(
			pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(pnlSuperiorLayout.createSequentialGroup()
			.addContainerGap()
			.addComponent(jScrollPane1)
			.addContainerGap())
		);
		pnlSuperiorLayout.setVerticalGroup(
			pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(pnlSuperiorLayout.createSequentialGroup()
			.addContainerGap()
			.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
			.addContainerGap())
		);

		pnlInferior.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pnlInferior.setPreferredSize(new java.awt.Dimension(104, 40));

		btnFechar.setFont(new java.awt.Font("Verdana", 0, 12));
		btnFechar.setText("Fechar");
		btnFechar.setPreferredSize(new java.awt.Dimension(75, 30));
		btnFechar.addActionListener(new java.awt.event.ActionListener() {
	    @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnFecharActionPerformed(evt);
			}
		});

		btnLimpar.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
		btnLimpar.setText("Limpar");
		btnLimpar.addActionListener(new java.awt.event.ActionListener() {
	    @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLimparActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout pnlInferiorLayout = new javax.swing.GroupLayout(pnlInferior);
		pnlInferior.setLayout(pnlInferiorLayout);
		pnlInferiorLayout.setHorizontalGroup(
			pnlInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInferiorLayout.createSequentialGroup()
			.addGap(0, 190, Short.MAX_VALUE)
			.addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			.addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
		);
		pnlInferiorLayout.setVerticalGroup(
			pnlInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInferiorLayout.createSequentialGroup()
			.addGap(0, 1, Short.MAX_VALUE)
			.addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
			.addComponent(btnLimpar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addComponent(pnlInferior, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
			.addComponent(pnlSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
			.addComponent(pnlSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			.addComponent(pnlInferior, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
		);

		pack();
	}

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {
		AOCLICARbtnFechar();
	}

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {
		AOCLICARbtnLimpar();
	}

	private javax.swing.JButton btnFechar;
	private javax.swing.JButton btnLimpar;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JPanel pnlInferior;
	private javax.swing.JPanel pnlSuperior;
	private javax.swing.JTextArea txaConsole;

	private void iniciarConsole() {
		this.printStream = new PrintStream(new CustomOutputStream(this.txaConsole), true);
		this.printStream = new PrintStream(new CustomOutputStream( this.txaConsole ) );
		System.setOut(printStream);
		System.setErr(printStream);
	}

	public void AOCLICARbtnLimpar() {
		this.txaConsole.setText( "" );
	}

	public void AOCLICARbtnFechar() {
		this.setVisible( false );
	}

	class CustomOutputStream extends OutputStream {

		private final JTextArea area;

		public CustomOutputStream( JTextArea pArea ) {
			this.area = pArea;
		}

		@Override
		public void write( int b ) throws IOException {
			this.area.append( String.valueOf( (char)b) );
		}
	}

	public void addTexto( String pTexto ) {
		this.printStream.append( pTexto );
	}
}
