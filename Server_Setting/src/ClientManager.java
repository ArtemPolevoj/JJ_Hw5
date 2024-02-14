import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {

    //region fields
    private final Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;
    public static ArrayList<ClientManager> clients = new ArrayList<>();
    //endregion

    //region constructors
    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            clients.add(this);
            Message ms = (Message) objectInputStream.readObject();
            name = ms.getName();
            System.out.println(name + " подключился к чату.");
            broadcastMessage(new Message("all", name, " подключился к чату."));
        } catch (Exception e) {
            removeClient(socket, objectOutputStream, objectInputStream);
        }
    }
    //endregion

    //region methods
    private void removeClient(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        broadcastMessage(new Message("all", name, " покинул чат."));
        try {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcastMessage(Message message) {
        try {
            if (message.getFrom().contains("all")) {
                for (ClientManager client : clients) {
                    if (!client.equals(this)) {
                        client.objectOutputStream.writeObject(message);
                        client.objectOutputStream.flush();
                    }
                }
            } else {
                for (ClientManager client : clients) {
                    if (client.name.contains(message.getFrom())) {
                        client.objectOutputStream.writeObject(message);
                        client.objectOutputStream.flush();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            removeClient(socket, objectOutputStream, objectInputStream);
        }
    }


    @Override
    public void run() {
        Message massageFromClient;
        while (!socket.isClosed()) {
            try {
                // Чтение данных
                massageFromClient = (Message) objectInputStream.readObject();
                // Отправка данных всем слушателям
                broadcastMessage(massageFromClient);
            } catch (Exception e) {
                removeClient(socket, objectOutputStream, objectInputStream);
            }
        }
    }
    //endregion
}
