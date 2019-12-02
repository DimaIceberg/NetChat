package chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client extends Thread{
    public Socket socket;
    public int id_client;

    public Client(Socket socket, int id_client) {
        this.socket = socket;
        this.id_client = id_client;
    }

    @Override
    public void run() {
        try {
            open_connection();
        } catch(IOException e) {
            e.printStackTrace();
            Main.checkClientConnection(id_client);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Main.checkClientConnection(id_client);
        }
    }

    public void open_connection() throws IOException, NoSuchElementException {

        System.out.println("Info: Client " + id_client + " connected");

        Scanner sc = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        while (true) {
            String str = sc.nextLine();
            if (str.equals("/exit")) {
                break;
            }
            System.out.println("Client " + id_client + " say: " + str);
            out.println("echo: " + str);
        }

        socket.close();
        System.out.println("Info: Client " + id_client + " disconnected");

    }
}
