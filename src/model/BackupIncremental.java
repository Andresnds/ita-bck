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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class BackupIncremental extends Backup implements Serializable {
	private static final TipoBackup tipo = TipoBackup.INCREMENTAL;
	private String nome;
	private File raiz;
	private EspacoBackup espaco;
	private long tamanho = 0;
	private Calendar dataDeCriacao;
	private HashMap<File, Modificacao> listaModificacao;
	private List<File> source;
	private BackupCompleto backupCompleto;
	private int index;
	private int numTotalFiles = 0;
	private int numPercorridoFiles = 0;
	transient private Buffer buffer;

	public BackupIncremental(BackupCompleto backupCompleto) throws IOException {
		this.backupCompleto = backupCompleto;
		backupCompleto.getBackupsIncrementais().add(this);
		index = backupCompleto.getBackupsIncrementais().indexOf(this);
		nome = backupCompleto.getNome() + index;
		raiz = backupCompleto.getRaiz().getParent().resolve(nome).toFile();
		espaco = backupCompleto.getEspaco();
		dataDeCriacao = Calendar.getInstance();
		listaModificacao = new HashMap<File, Modificacao>();
		source = backupCompleto.getSource();
		Files.createDirectory(raiz.toPath());

		contarArquivos();
		espaco.adicionarBackup(this);
	}

	public BackupIncremental(BackupIncremental backupIncremental)
			throws IOException {
		this(backupIncremental.backupCompleto);
	}

	public void salvarArquivos() throws IOException {
		for (final File sourceF : source) {
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
					numPercorridoFiles++;
					Path fileCompleto = backupCompleto.getRaiz()
							.resolve(sourceFile.getFileName())
							.resolve(sourceFile.relativize(file));
					Path fileIncremental = target.resolve(sourceFile
							.relativize(file));
					Path relativePath = sourceFile.getParent().relativize(file);

					if (!Files.exists(fileCompleto)) {
						listaModificacao.put(relativePath.toFile(),
								Modificacao.NOVO);
						if (!Files.exists(getArquivoMaisAtualizado(raiz
								.toPath().relativize(fileIncremental))))
							Files.copy(file, fileIncremental);
					}

					else if (fileCompleto.toFile().lastModified() != file
							.toFile().lastModified()) {
						listaModificacao.put(relativePath.toFile(),
								Modificacao.MODIFICADO);
						if (getArquivoMaisAtualizado(
								raiz.toPath().relativize(fileIncremental))
								.toFile().lastModified() != file.toFile()
								.lastModified()) {
							Files.copy(file, fileIncremental);
						}
					}

					else {
						listaModificacao.put(relativePath.toFile(),
								Modificacao.NAOMODIFICADO);
					}
					buffer.atualiza();
					return FileVisitResult.CONTINUE;
				}

			});
		}
	}

	private Path getArquivoMaisAtualizado(Path relativePath) {
		if (Files.exists(raiz.toPath().resolve(relativePath)))
			return raiz.toPath().resolve(relativePath);
		if (index == 0)
			return backupCompleto.getRaiz().resolve(relativePath);
		return backupCompleto.getBackupsIncrementais().get(index - 1)
				.getArquivoMaisAtualizado(relativePath);
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
	public Path getRaiz() {
		return raiz.toPath();
	}

	@Override
	public void restaurar(Path destino) throws IOException {
		numTotalFiles = listaModificacao.size();
		numPercorridoFiles = 0;
		for (File relative : listaModificacao.keySet()) {
			numPercorridoFiles++;
			buffer.atualiza();
			Path relativeFile = relative.toPath();
			Modificacao modif = listaModificacao.get(relative);
			if (modif == Modificacao.NAOMODIFICADO) {
				Files.createDirectories(destino.resolve(relativeFile
						.getParent()));
				Files.copy(backupCompleto.getRaiz().resolve(relativeFile),
						destino.resolve(relativeFile));
			} else if (modif == Modificacao.MODIFICADO
					|| modif == Modificacao.NOVO) {
				Files.createDirectories(destino.resolve(relativeFile
						.getParent()));
				Files.copy(getArquivoMaisAtualizado(relativeFile),
						destino.resolve(relativeFile));
			}
		}

	}

	@Override
	public EspacoBackup getEspaco() {
		return espaco;
	}

	@Override
	public void restaurar(String destino) throws IOException {
		Path path = Paths.get(destino);
		restaurar(path);

	}

	public int getProgresso() {
		return numPercorridoFiles;

	}

	private void contarArquivos() throws IOException {
		for (final File sourceF : source) {
			final Path sourceFile = sourceF.toPath();
			Files.walkFileTree(sourceFile, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					numTotalFiles++;
					tamanho += file.toFile().length();
					return FileVisitResult.CONTINUE;
				}

			});
		}
	}

	@Override
	public int getNumTotalFiles() {
		return numTotalFiles;
	}

	@Override
	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;

	}

	public long getTamanho() {
		return tamanho;
	}
}
