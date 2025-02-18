import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.apache.commons.net.ftp.FTPFile;

public class Menu {
    private final Gestor gestor;
    private final Scanner scanner;

    public Menu(Gestor gestor) {
        this.gestor = gestor;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() throws IOException {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Listar archivos");
            System.out.println("2. Descargar archivo");
            if (gestor.estaAutenticado()) System.out.println("3. Subir archivo");
            System.out.println("4. Salir");
            System.out.print("Introduce opcion: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> listarArchivos();
                case 2 -> descargarArchivo();
                case 3 -> {
                    if (gestor.estaAutenticado()) subirArchivo();
                    else System.err.println("la opcion no existe.");
                }
                case 4 -> salir = true;
                default -> System.err.println("La opcion no existe");
            }
        }
    }

    private void listarArchivos() throws IOException {
        FTPFile[] archivos = gestor.listarFicheros();
        System.out.println("\nArchivos en el servidor:");
        for (FTPFile archivo : archivos) {
            System.out.print(archivo.getName()+"\n");
        }
    }

    private void descargarArchivo() {
        System.out.print("Introduce la ruta del archivo que quieres descargar: ");
        String rutaServidor = scanner.nextLine();
        System.out.print("Introduce la ruta donde deseas guardar: ");
        String rutaLocal = scanner.nextLine();

        File archivoLocal = new File(rutaLocal);
        try {
            File directorioPadre = archivoLocal.getParentFile();

            if (directorioPadre != null) {
                if (!directorioPadre.exists() && !directorioPadre.mkdirs()) {
                    System.err.println("Error: No se pudo crear el directorio " + directorioPadre.getAbsolutePath());
                    return;
                }
            }

            if (gestor.descargarFicheros(rutaServidor, rutaLocal)) {
                System.out.println("Archivo descargado exitosamente.");
            } else {
                System.err.println("Error: Verifica la ruta del servidor o permisos.");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void subirArchivo() {
        System.out.print("Introduce la ruta del archivo a subir: ");
        String local = scanner.nextLine();
        String nombreArchivo = new File(local).getName();

        try {
            if (gestor.subirFicheros(local, nombreArchivo)) {
                System.out.println("Se ha subido el archivo con el mismo nombre.");
            } else {
                System.err.println("Error al subir archivo");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
