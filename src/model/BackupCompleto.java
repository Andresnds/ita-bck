package model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class BackupCompleto extends Backup implements Serializable {
	private static final TipoBackup tipo = TipoBackup.COMPLETO;
	private String nome;
	private long tamanho = 0;
	private int numTotalFiles = 0;
	private int numPercorridoFiles = 0;
	private File raiz;
	private EspacoBackup espaco;
	private Calendar dataDeCriacao;
	private HashMap<File, Modificacao> listaModificacao;
	private List<File> source;
	private List<BackupIncremental> backupsIncrementais;
	transient private Buffer buffer;

	public BackupCompleto(String nome, EspacoBackup espaco, List<File> source)
			throws IOException {
		this.espaco = espaco;
		dataDeCriacao = Calendar.getInstance();
		this.nome = nome;
		backupsIncrementais = new ArrayList<BackupIncremental>();
		this.source = source;
		listaModificacao = new HashMap<File, Modificacao>();
		raiz = espaco.getRaiz().resolve(nome).toFile();
		Files.createDirectory(raiz.toPath());

		contarArquivos();
		espaco.adicionarBackup(this);
	}

	public void salvarArquivos() throws IOException {
		for (File sourceF : source) {
			final Path sourceFile = sourceF.toPath();
			final Path target = raiz.toPath().resolve(sourceFile.getFileName());
			Files.walkFileTree(sourceFile, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException {
					Path targetDir = target.resolve(sourceFile.relativize(dir));
					try {
						Files.copy(dir, targetDir);
					} catch (FileAlreadyExistsException e) {
						if (!Files.isDirectory(targetDir))
							throw e;
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					listaModificacao.put(sourceFile.getParent()
							.relativize(file).toFile(),
							Modificacao.NAOMODIFICADO);

					Files.copy(file,
							target.resolve(sourceFile.relativize(file)));
					numPercorridoFiles++;
					buffer.atualiza();
					return FileVisitResult.CONTINUE;
				}

			});
		}
	}

	public Path getRaiz() {
		return raiz.toPath();
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public Calendar getDataCriacao() {
		return dataDeCriacao;
	}

	@Override
	public TipoBackup getTipo() {
		return tipo;
	}

	@Override
	public HashMap<File, Modificacao> getListaModicacao() {
		return listaModificacao;
	}

	@Override
	public void restaurar(final Path destino) throws IOException {
		numPercorridoFiles = 0;
		Files.walkFileTree(raiz.toPath(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes arg1) throws IOException {
				Path destinoDir = destino
						.resolve(raiz.toPath().relativize(dir));
				try {
					Files.copy(dir, destinoDir);
				} catch (FileAlreadyExistsException e) {
					if (!Files.isDirectory(destinoDir))
						throw e;
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				Files.copy(file,
						destino.resolve(raiz.toPath().relativize(file)));
				numPercorridoFiles++;
				buffer.atualiza();
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public void restaurar(String destino) throws IOException {
		restaurar(Paths.get(destino));
	}

	@Override
	public EspacoBackup getEspaco() {
		return espaco;
	}

	public List<BackupIncremental> getBackupsIncrementais() {
		return backupsIncrementais;
	}

	public List<File> getSource() {
		return source;
	}

	public int getProgresso() {
		return numPercorridoFiles;
	}

	protected void contarArquivos() throws IOException {
		for (File sourceF : source) {
			final Path sourceFile = sourceF.toPath();
			Files.walkFileTree(sourceFile, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					numTotalFiles++;
					tamanho+=file.toFile().length();
					return FileVisitResult.CONTINUE;
				}

			});
		}
	}

	@Override
	public int getNumTotalFiles() {
		return numTotalFiles;
	}
	
	public long getTamanho() {
		return tamanho;
	}

	@Override
	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;

	}

}
