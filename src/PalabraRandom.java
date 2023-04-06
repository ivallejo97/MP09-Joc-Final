import java.util.*;

public class PalabraRandom {

    String palabraRandom;
    private char[] palabraAdivinar;
    public char[] palabraMostrar;

    public PalabraRandom() {
        generarPalabraRandom();
        inicializarPalabraMostrar();
    }

    public void generarPalabraRandom() {
        Random random = new Random();
        String[] palabraRandom = {"Nacho", "Coche", "Kings", "League", "Vini", "Rodry", "Joselu","Nubes","Nieve","Cebra","Llave"};
        setPalabraRandom(palabraRandom[random.nextInt(palabraRandom.length)]);
    }

    public int comprobarLetra(char letra) {
        char[] palabra = palabraToCharArray();
        boolean adivinado = false;

        for (int i = 0; i < palabra.length; i++) {
            if (palabra[i] == letra) {
                //palabraMostrar[i] = letra;
                actualizarPalabraMostrar(palabraMostrar[i] = letra);
                adivinado = true;
            }
        }
        if (adivinado) {
            System.out.println("¡Adivinaste una letra!");
            mostrarPalabraActualizada();
            return 1;
        } else {
            System.out.println("La letra no está en la palabra.");
            mostrarPalabraActualizada();
            return 0;
        }
    }

    public void inicializarPalabraMostrar() {
        palabraAdivinar = palabraToCharArray();
        palabraMostrar = new char[palabraAdivinar.length];
        Arrays.fill(palabraMostrar, '-');
    }

    public void actualizarPalabraMostrar(char letra) {
        for (int i = 0; i < palabraAdivinar.length; i++) {
            if (palabraAdivinar[i] == letra) {
                palabraMostrar[i] = letra;
            }
        }
    }

    public void mostrarPalabraActualizada() {
        for (int i = 0; i < palabraMostrar.length; i++) {
            System.out.print(palabraMostrar[i] + " ");
        }
        System.out.println();
    }

    public char[] palabraToCharArray() {
        return getPalabraRandom().toCharArray();
    }

    public String getPalabraRandom() {
        return palabraRandom;
    }

    public void setPalabraRandom(String palabraRandom) {
        this.palabraRandom = palabraRandom;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        PalabraRandom palabra = new PalabraRandom();
        int intentosRestantes = 6;

        System.out.println("¡Bienvenido al juego de adivinanza de letras!");
        System.out.println("La palabra tiene " + palabra.getPalabraRandom().length() + " letras.");

        while (intentosRestantes > 0) {
            System.out.println("Intentos restantes: " + intentosRestantes);
            System.out.println("Adivina una letra: ");
            char letra = scanner.nextLine().charAt(0);
            palabra.comprobarLetra(letra);
            if (palabra.getPalabraRandom().equals(String.valueOf(palabra.palabraMostrar))) {
                System.out.println("¡Felicidades, has adivinado la palabra!");
                return;
            }
            //intentosRestantes--;
        }


        System.out.println("¡Lo siento, has perdido! La palabra era " + palabra.getPalabraRandom());
    }

}
