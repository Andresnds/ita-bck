package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.HashMap;

abstract public class Backup {
	public abstract String getNome();
	
	public abstract long getTamanho();

	public abstract Calendar getDataCriacao();

	public abstract TipoBackup getTipo();

	public abstract Path getRaiz();

	public abstract EspacoBackup getEspaco();

	public abstract HashMap<File, Modificacao> getListaModicacao();

	public abstract void restaurar(Path destino) throws IOException;

	public abstract void restaurar(String destino) throws IOException;

	public abstract int getProgresso();

	public abstract int getNumTotalFiles();

	public abstract void setBuffer(Buffer buffer);

	public abstract void salvarArquivos() throws IOException;
}
