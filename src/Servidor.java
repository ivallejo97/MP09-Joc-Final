import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Servidor {

    DatagramSocket socket;
    int puerto;
    PalabraRandom palabraRandom = new PalabraRandom();
    char[] palabra;
    boolean acabado;
    int intentos = 5;

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
        byte[] receivingData = new byte[1024];
        byte[] sendingData;
        InetAddress clientIP;
        int clientPort;

        //el servidor atén el port indefinidament
        while(!acabado){
            DatagramPacket packet = new DatagramPacket(receivingData, 24);
            socket.receive(packet);
            sendingData = processData(packet.getData());
            clientIP = packet.getAddress();
            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData, sendingData.length, clientIP, clientPort);
            socket.send(packet);
        }
        socket.close();
    }

    private byte[] processData(byte[] data) {
        char letra = ByteBuffer.wrap(data).getChar();
        System.out.print("\033[0;37mEl usuario ha introducido la letra: " + letra);

        palabraRandom.comprobarLetra(letra);

        String s = palabraRandom.mostrarPalabraActualizada();
        String estado = "";
        if (intentos > 0){
            if (palabraRandom.estado == 1){
                estado = "\033[1;32mLetra adivinada\n\033[0m" + s + "\nIntentos Restantes: " + intentos;
                System.out.println("\033[1;32mEl usuario ha adivinado una letra\033[0m");
            } else if (palabraRandom.estado == 0){
                intentos--;
                estado = "\033[1;31mLa letra no está en la palabra\n\033[0m" + s + "\nIntentos Restantes: " + intentos;
                System.out.println("\033[1;31mEl usuario ha dicho una letra incorrecta\033[0m");
            }else if (palabraRandom.estado == 2){
                estado = "\033[1;32mPalabra adivinada: \033[0m" + s + "\n\u001B[34mEl servidor ha generado otra palabra\u001B[0m";
                palabraRandom = new PalabraRandom();
                System.out.println(estado);
                System.out.println("\u001B[34mNueva Palabra: " + palabraRandom.getPalabraRandom());
                intentos = 5;
            }
            if (intentos == 0){
                estado = "\033[1;31mTe has quedado sin intentos\nLa palabra correcta era " + palabraRandom.getPalabraRandom() + "\n\u001B[34mSe ha generado otra palabra para seguir jugando\u001B[0m";
                System.out.println("\033[1;31mEl usuario no ha adivinado la palabra");
                palabraRandom = new PalabraRandom();
                System.out.println(estado);
                System.out.println("\u001B[34mNueva Palabra: " + palabraRandom.getPalabraRandom());
                intentos = 5;
            }
        }

        return estado.getBytes();

    }


    private byte[] getEstadoPalabra(){
        // Obtener el estado actual de la palabra
        palabra = palabraRandom.palabraToCharArray();
        // Crear un ByteBuffer con el tamaño adecuado
        ByteBuffer buf = ByteBuffer.allocate(palabra.length * 2);
        // Agregar cada carácter al ByteBuffer
        for (char c : palabra) {
            buf.putChar(c);
        }
        // Obtener el array de bytes con los datos
        Charset charset = StandardCharsets.UTF_8;
        ByteBuffer byteBuffer = charset.encode(CharBuffer.wrap(palabraRandom.palabraMostrar));
        byte[] bytes = byteBuffer.array();
        return bytes;
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
