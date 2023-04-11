import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Cliente {

    private int portDesti;
    private String nom, ipSrv;
    private InetAddress adrecaDesti;
    Scanner sc = new Scanner(System.in);

    public Cliente(String ip, int port) {
        this.portDesti = port;
        ipSrv = ip;

        try {
            adrecaDesti = InetAddress.getByName(ipSrv);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        byte[] sendingData;
        char letra;

        sendingData = getFirstRequest();
        System.out.println("\n**********************************************************");
        System.out.println("\u001B[34mBienvenido al juego de adivinar la palabra " + nom + "!\u001B[0m");
        System.out.println("**********************************************************");
        //Bucle de joc
        while(mustContinue(sendingData)) {
            System.out.print("Introduce una letra: ");
            letra = sc.nextLine().charAt(0);
            if (letra == '1') System.exit(0);
            byte[] missatge = ByteBuffer.allocate(1024).putChar(letra).array();
            DatagramPacket packet = new DatagramPacket(missatge,missatge.length,adrecaDesti,portDesti);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            packet = new DatagramPacket(receivedData, 1024);
            socket.setSoTimeout(5000);
            socket.receive(packet);
            sendingData = getDataToRequest(packet.getData(), packet.getLength());

        }
    }

    private byte[] getDataToRequest(byte[] data, int length) {
        String rebut = new String(data,0, length);
        System.out.println(rebut);

        return rebut.getBytes();
    }

    //primer missatge que se li envia al server
    private byte[] getFirstRequest() {
        System.out.println("Escribe tu nombre: ");
        nom = sc.nextLine();
        return nom.getBytes();
    }

    //Si se li diu adeu al server el client es desconnecta
    private boolean mustContinue(byte [] data) {
        char[] msg = new char[data.length];
        return !msg.equals('1');
    }


    public static void main(String[] args) {
        Cliente cAdivina = new Cliente("localhost", 5556);

        try {
            cAdivina.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
