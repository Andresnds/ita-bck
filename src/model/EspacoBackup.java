package model;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class EspacoBackup implements Serializable {
	transient private Path raiz;
	private String path;
	private List<Backup> backups;

	public EspacoBackup(Path dir) throws IOException {
		raiz = dir.resolve("ITA BCK");
		backups = new ArrayList<Backup>();
		if (!Files.exists(raiz))
			Files.createDirectory(raiz);
		salvarEspaco();
	}

	public void salvarEspaco() throws IOException {
		path = raiz.toString();
		OutputStream os = new FileOutputStream(raiz.resolve("metadata.espaco")
				.toFile());

		DataOutputStream dos = new DataOutputStream(os);
		ObjectOutputStream out = new ObjectOutputStream(dos);

		out.writeObject(this);
		out.close();

	}

	public void adicionarBackup(Backup bck) {
		backups.add(bck);
	}

	public EspacoBackup(String path) throws IOException {
		this(Paths.get(path));
	}

	public Path getRaiz() {
		return raiz;
	}

	public static EspacoBackup inicializar(Path path) throws IOException,
			ClassNotFoundException {
		File metadata = path.resolve("ITA BCK").resolve("metadata.espaco")
				.toFile();
		if (metadata.exists()) {
			InputStream is = new FileInputStream(metadata);
			DataInputStream dis = new DataInputStream(is);
			ObjectInputStream ois = new ObjectInputStream(dis);
			EspacoBackup e = (EspacoBackup) ois.readObject();
			e.raiz = Paths.get(e.path);
			ois.close();
			return e;
		}

		return new EspacoBackup(path);
	}

	public List<Backup> getBackups() {
		return backups;
	}


	public long getFreeSpace() {
		return raiz.toFile().getFreeSpace();
	}
}