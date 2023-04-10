import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Cliente2 {

    private int portDesti;
    private int result;
    private String nom, ipSrv;
    private int intentos;
    private InetAddress adrecaDesti;
    Scanner scanner = new Scanner(System.in);

    public Cliente2(String ip, int port) {
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
        byte [] receivedData = new byte[1024];
        byte [] sendingData;
        char letra;


        sendingData = getFirstRequest();
        System.out.println("Hola " + nom + "!\nEscribe una letra: ");
        //Bucle de joc
        while(mustContinue(sendingData)) {

            Scanner sc = new Scanner(System.in);
            letra = sc.nextLine().charAt(0);
            //byte[] missatge = ByteBuffer.allocate(4).putInt(letra).array();
            byte[] missatge = ByteBuffer.allocate(1024).putChar(letra).array();

            //creació del paquet a enviar
            DatagramPacket packet = new DatagramPacket(missatge,missatge.length,adrecaDesti,portDesti);
            //creació d'un sòcol temporal amb el qual realitzar l'enviament
            DatagramSocket socket = new DatagramSocket();
            //Enviament del missatge
            socket.send(packet);

            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);
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
        int estado = ByteBuffer.wrap(data).getInt();
        if(estado==1) System.out.println("DDD");
        else if (estado==0) System.out.println("AAA");
        else if (estado == 2) System.out.println("SSS");

        String rebut = new String(data,0, length);
        System.out.println(rebut);

        return estado;
    }

    //primer missatge que se li envia al server
    private byte[] getFirstRequest() {
        System.out.println("Escribe tu nombre: ");
        nom = scanner.nextLine();
        return nom.getBytes();
    }

    //Si se li diu adeu al server el client es desconnecta
    private boolean mustContinue(byte [] data) {
        String msg = new String(data).toLowerCase();
        return !msg.equals("adeu");
    }


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }


    /*Charset charset = StandardCharsets.UTF_8;
        char[] datos = charset.decode(ByteBuffer.wrap(data, 0, length)).array();
        byte[] datosBytes = new String(datos, 0, length).getBytes(charset);
        int datosInt = ByteBuffer.wrap(datosBytes).getInt();*/
    public static void main(String[] args) {
        Cliente2 cAdivina = new Cliente2("localhost", 5556);

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
