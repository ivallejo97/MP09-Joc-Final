import java.util.*;

public class PalabraRandom {

        String palabraRandom;

        public PalabraRandom(){
            generarPalabraRandom();
        }

        public void generarPalabraRandom(){
            Random random = new Random();
            String[] palabraRandom = {"Nacho","Coche","Kings","League","Vini","Rodry","Joselu"};
            setPalabraRandom(palabraRandom[random.nextInt(palabraRandom.length)]);
        }


        public void comprobarLetra(char letra){

            char[] pa = getPalabraRandom().toCharArray();

            for (int i = 0; i < pa.length; i++) {
                if (pa[i] == letra){
                    System.out.print(letra);
                } else {
                    System.out.print("-");
                }
            }
        }

        public char[] toCharArray(){
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
        PalabraRandom palabraRandom1 = new PalabraRandom();

        for (int i = 0; i < palabraRandom1.toCharArray().length; i++) {
            //System.out.print(palabraRandom1.getPalabraRandom().toCharArray()[i] + " ");
            System.out.println(palabraRandom1.toCharArray());
        }


        System.out.println("Letra");
        char c = scanner.next().charAt(0);
        palabraRandom1.comprobarLetra(c);



        //System.out.println(palabraRandom1.getPalabraRandom());
    }



}

