import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

public class Gestor {
    private static final String SERVIDOR = "127.0.0.1";
    private FTPClient cliente = new FTPClient();
    private boolean Autenticado = false;

    public boolean conectarse(String user, String password) throws IOException {
        cliente.connect(SERVIDOR);
        if (!cliente.login(user, password)){
            return false;
        }
        Autenticado = !user.equalsIgnoreCase("anonimo");
        cliente.enterLocalPassiveMode();
        cliente.setFileType(FTPClient.BINARY_FILE_TYPE);
        return true;
    }

    public void desconectarse() throws IOException {
        if (cliente.isConnected()) {
            cliente.logout();
            cliente.disconnect();
        }
    }

    public boolean estaAutenticado() {
        return Autenticado;
    }

    public FTPFile[] listarFicheros() throws IOException {
        return cliente.listFiles();
    }

    public boolean descargarFicheros(String ficheroServidor, String ficheroLocal) throws IOException {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(ficheroLocal))) {
            return cliente.retrieveFile(ficheroServidor, os);
        }
    }

    public boolean subirFicheros(String ficheroLocal, String ficheroServidor) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(ficheroLocal))) {
            return cliente.storeFile(ficheroServidor, is);
        }
    }
}