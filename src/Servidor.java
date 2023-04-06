import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Servidor {

    DatagramSocket socket;
    int puerto;
    PalabraRandom palabraRandom = new PalabraRandom();
    char[] palabra;
    boolean acabado;
    boolean letraAdivinada;

    public Servidor(int puerto){
        try {
            socket = new DatagramSocket(puerto);
            System.out.println("Servidor iniciado por el puerto: " + puerto);
            System.out.println("\u001B[34mPalabra: " + palabraRandom.getPalabraRandom() + "\u001B[0m");
        } catch (SocketException e){
            e.printStackTrace();
        }
        this.puerto = puerto;
        acabado = false;
    }

    public void runServer() throws IOException {
        byte[] receivingData = new byte[4];
        byte[] sendingData;
        InetAddress clientIP;
        int clientPort;

        //el servidor atén el port indefinidament
        while(!acabado){

            //creació del paquet per rebre les dades
            DatagramPacket packet = new DatagramPacket(receivingData, 4);
            //espera de les dades
            socket.receive(packet);
            //processament de les dades rebudes i obtenció de la resposta
            sendingData = processData(packet.getData(), packet.getLength());
            //obtenció de l'adreça del client
            clientIP = packet.getAddress();
            //obtenció del port del client
            clientPort = packet.getPort();
            //creació del paquet per enviar la resposta
            packet = new DatagramPacket(sendingData, sendingData.length, clientIP, clientPort);
            //enviament de la resposta
            socket.send(packet);
        }
        socket.close();
    }

    private byte[] processData(byte[] data, int length) {
        char nombre = ByteBuffer.wrap(data).getChar();
        System.out.println("Recibido-> " + nombre);
        palabraRandom.comprobarLetra(nombre);

        return ss();
    }

    private byte[] ss(){
        //char[] arrayChars = {'h', 'e', 'l', 'l', 'o'};
        palabra = palabraRandom.palabraToCharArray();
        // Crear un ByteBuffer con el tamaño adecuado
        ByteBuffer buf = ByteBuffer.allocate(palabra.length * 2);
        // Agregar cada carácter al ByteBuffer
        for (char c : palabra) {
              buf.putChar(c);
        }
        // Obtener el array de bytes con los datos
        byte[] byteArray = buf.array();
        // Enviar el array de bytes por la red o almacenarlo en un archivo, por ejemplo
        // Recibir el array de bytes
        // Crear un ByteBuffer con los datos recibidos
        ByteBuffer receivedBuf = ByteBuffer.wrap(byteArray);
        // Crear un nuevo array de caracteres a partir de los datos del ByteBuffer
        //char[] newCharArray = receivedBuf.asCharBuffer().array();
        return byteArray;
    }

    public static void main(String[] args) throws SocketException, IOException {
        Servidor sAdivina = new Servidor(5556);

        try {
            sAdivina.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Fi Servidor");



    }
}
