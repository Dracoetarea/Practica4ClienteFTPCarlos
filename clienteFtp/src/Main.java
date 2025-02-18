import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Usuario (enter para anonimo): ");
        String usuario = scanner.nextLine().trim();

        String clave = "";
        if (usuario.isEmpty()) {
            usuario = "anonimo";
        } else {
            System.out.print("Contraseña: ");
            clave = scanner.nextLine();
        }

        Gestor gestor = new Gestor();

        try {
            if (gestor.conectarse(usuario, clave)) {
                System.out.println("Conectado al servidor");
                Menu menu = new Menu(gestor);
                menu.showMenu();
                gestor.desconectarse();
            } else {
                System.err.println("Error al conectarse");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}