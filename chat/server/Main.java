package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static ServerSocket server;
    public static String serverCommand;
    public static int id;
    public static List<Client>clientsList;;


    public static void main(String[] args) {

        clientsList = new ArrayList<>();
        serverCommand = "";
        id = 0;

        try {
            server = new ServerSocket(8090);
            System.out.println("Info: Server started");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread listener = new Thread(()-> {
            while(true) {
                if (serverCommand.equals("/exit")) {
                    break;
                }
                try {
                    Socket socket = new Socket();
                    socket = server.accept();
                    id++;
                    clientsList.add(new Client(socket, id));
                    Thread thread = clientsList.get(id - 1);
                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        listener.start();

        Scanner srvCmdScan = new Scanner(System.in);

        while (true) {
            serverCommand = srvCmdScan.nextLine();
            if (serverCommand.equals("/exit")) {
                break;
            } else if (serverCommand.equals("/list")) {
                System.out.println("Number of clients: " + clientsList.size());
                for (Client tempClient : clientsList) {
                    System.out.println("Client id = " + tempClient.id_client);
                }
            }
        }

        try {
            server.close();
            System.out.println("Info: Server closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkClientConnection(int id_client) {
        System.out.println(clientsList.get(id_client - 1).socket.isConnected());
        if (!clientsList.get(id_client - 1).socket.isConnected()) {
            clientsList.remove(id_client - 1);
            System.out.println("Info: Client " + id_client + " was deleted");
        }
    }
}
