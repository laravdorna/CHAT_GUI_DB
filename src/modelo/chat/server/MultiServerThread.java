package modelo.chat.server;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 * Este hilo es responsable de leer la entrada del servidor e imprimirla en
 * consola. Se ejecuta en un bucle infinito hasta que el cliente se desconecta
 * del servidor.
 *
 * @author Juan Martinez Piñeiro y Lara Vázquez Dorna
 */
public class MultiServerThread extends Thread {

    private Socket socket;
    private Server server;
    private PrintWriter writer;

    /**
     * Constructor. crea un servidor y un socket
     *
     * @param socket
     * @param server
     */
    public MultiServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * metodo heredado de la interface Thread que imprime los usuarios
     * conectados y sus respuestas pasandoselas a los otros clientes
     */
    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            String stream = reader.readLine();
            String userName = "";

            if (stream.startsWith("/")) {
                String cmd = stream.substring(1, stream.indexOf(' '));
                String content = stream.substring(stream.indexOf(' ') + 1);

                if (cmd.equalsIgnoreCase("login")) {

                    String user = "";
                    String passwd = "";

                    //user = content.substring(0, content.indexOf(' '));
                    //passwd = content.substring(content.indexOf(' ') + 1);
                    user = content;

                    //me conecto a base de datos y compruebo en el DAO si existe el usuario, si no existe lo crea
                    if (false) {
                        return;
                    }

                    printUsers();

                    userName = user;
                    server.addUserName(userName);

                    String serverMessage = "Nuevo usuario conectado: " + userName;
                    server.broadcast(serverMessage, this);
                    server.saveToFile(serverMessage, this);
                }
            }

            String clientMessage;
            String serverMessage;
            do {
                clientMessage = reader.readLine();
                if (clientMessage.startsWith("/")) {
                    String cmd = stream.substring(1, stream.indexOf(' '));
                    String content = stream.substring(stream.indexOf(' ') + 1);

                    switch (cmd) {
                        case "logout":
                            return;
                        case "update":
                            String user = "";
                            String passwd = "";

                            user = stream.substring(0, stream.indexOf(' '));
                            passwd = stream.substring(stream.indexOf(' ') + 1);

                        //me conecto a base de datos, compruebo en el DAO, y actualizo
                    }
                } else {
                    serverMessage = "[" + userName + "]: " + clientMessage;
                    server.broadcast(serverMessage, this);
                    server.saveToFile(serverMessage, this);
                }
            } while (!clientMessage.equals("chao"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " ha dejado el chat.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            Logger.getLogger(MultiServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envía una lista de usuarios en línea al usuario recién conectado.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("/users " + String.join(",", server.getUserNames()));
        } else {
            writer.println("No hay otros usuarios conectados");
        }
    }

    /**
     * Envia el mensaje al cliente.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
