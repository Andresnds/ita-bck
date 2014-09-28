package model;
import javax.swing.JProgressBar;


public class Buffer {
	private int value;
	private JProgressBar bar;
	private Backup bck;

	public Buffer(JProgressBar bar, Backup bck) {
		this.bar = bar;
		this.bck = bck;
	}

	private void get() {
		value = bck.getProgresso();
	}

	private void set() {
		bar.setValue(value);
	}

	public void atualiza() {
		get();
		set();
	}

}
