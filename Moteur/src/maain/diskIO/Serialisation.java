package maain.diskIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serialisation {
	// test
	public static <T> void save(T serObject, String filename) {
		ObjectOutputStream oos = null;
		System.out.println("[Serial] serialisation ...");
		try {
			final FileOutputStream fichier = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(serObject);
			oos.flush();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("[Serial] serialisation effectuée avec succès .");
	}

	@SuppressWarnings("unchecked")
	public static <T> T load(String filename) {

		System.out.println("[Serial] chargement ...");
		ObjectInputStream ois = null;
		T serObject = null;
		try {
			final FileInputStream fichier = new FileInputStream(filename);
			ois = new ObjectInputStream(fichier);
			serObject = (T) ois.readObject();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("[Serial] chargement effectué avec succès ...");
		return serObject;
	}

}
