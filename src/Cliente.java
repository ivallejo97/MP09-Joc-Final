import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Cliente {

    private int portDesti;
    private int result;
    private String nom, ipSrv;
    private int intentos;
    private InetAddress adrecaDesti;
    Scanner scanner = new Scanner(System.in);

    public Cliente(String ip, int port) {
        this.portDesti = port;
        result = -1;
        intentos = 5;
        ipSrv = ip;

        try {
            adrecaDesti = InetAddress.getByName(ipSrv);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setNom(String n) {
        nom=n;
    }

    public int getIntentos() {
        return intentos;
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[4];
        byte [] sendingData;
        char n;

        //Missatge de benvinguda
        System.out.println("Hola " + nom + "!\nEscribe una letra: ");
        //Bucle de joc
        while(result!=0 & result!=-2) {

            Scanner sc = new Scanner(System.in);
            n = sc.nextLine().charAt(0);
            //byte[] missatge = ByteBuffer.allocate(4).putInt(n).array();
            byte[] missatge = ByteBuffer.allocate(4).putChar(n).array();

            //creació del paquet a enviar
            DatagramPacket packet = new DatagramPacket(missatge,missatge.length,adrecaDesti,portDesti);
            //creació d'un sòcol temporal amb el qual realitzar l'enviament
            DatagramSocket socket = new DatagramSocket();
            //Enviament del missatge
            socket.send(packet);

            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 4);
            //espera de les dades
            socket.setSoTimeout(5000);
            try {
                socket.receive(packet);
                //processament de les dades rebudes i obtenció de la resposta
                result = getDataToRequest(packet.getData(), packet.getLength());
            }catch(SocketTimeoutException e) {
                System.out.println("El servidor no respòn: " + e.getMessage());
                result=-2;
            }
        }


    }

    private int getDataToRequest(byte[] data, int length) {
        char nombre = ByteBuffer.wrap(data).getChar();

        if(nombre==1) System.out.println("Letra Adivinada");
        else if (nombre==0) System.out.println("Letra no adivinada");
        intentos++;

        return nombre;
    }

    //primer missatge que se li envia al server
    private byte[] getFirstRequest() {
        System.out.println("Escriu el teu nom: ");
        nom = scanner.nextLine();
        return nom.getBytes();
    }



    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static void main(String[] args) {
        String jugador, ipSrv, port;

        //Demanem la ip del servidor i nom del jugador
        System.out.println("IP del servidor?");
        Scanner sip = new Scanner(System.in);
        ipSrv = sip.next();
        System.out.println("Nom jugador:");
        jugador = sip.next();

        //Cliente cAdivina = new Cliente(ipSrv, Integer.valueOf(port));
        Cliente cAdivina = new Cliente(ipSrv, 5556);

        cAdivina.setNom(jugador);
        try {
            cAdivina.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(cAdivina.getResult() == 0) {
            System.out.println("Fi, ho has aconseguit amb "+ cAdivina.getIntentos() +" intents");
        } else {
            System.out.println("Has perdut");
        }

    }
}
