import java.util.*;

public class PalabraRandom {

    String palabraRandom;
    private char[] palabraAdivinar;
    public char[] palabraMostrar;
    int estado;


    public PalabraRandom() {
        generarPalabraRandom();
        inicializarPalabraMostrar();
    }

    public String generarPalabraRandom() {
        Random random = new Random();
        String[] palabraRandom = {"Nacho", "Coche", "Kings", "League", "Vini", "Rodry", "Joselu","Nubes","Nieve","Cebra","Llave"};
        setPalabraRandom(palabraRandom[random.nextInt(palabraRandom.length)]);
        return getPalabraRandom();
    }

    public int comprobarLetra(char letra) {
        char[] palabra = palabraToCharArray();
        boolean adivinado = false;

        for (int i = 0; i < palabra.length; i++) {
            if (palabra[i] == letra) {
                adivinado = true;
                actualizarPalabraMostrar(palabraMostrar[i] = letra);
            }
        }

        if (adivinado == true) estado = 1;
        else estado = 0;

        if (getPalabraRandom().equals(String.valueOf(palabraMostrar))) estado = 2;


        return estado;
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

    public String mostrarPalabraActualizada() {
        String palabra = "";
        for (int i = 0; i < palabraMostrar.length; i++) {
            palabra += String.valueOf("\033[1;32m" + palabraMostrar[i] + "\033[0m");
        }
        System.out.println();
        return palabra;
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

}
