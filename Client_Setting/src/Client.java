import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    //region fields
    private final Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final String name;
    //endregion

    //region constructors
    public Client(Socket socket, String userName) {
        this.socket = socket;
        name = userName;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            closeEverything(socket, objectOutputStream, objectInputStream);
        }
    }
    //endregion

    //region methods
    public void sendMessage() {

        try {
            objectOutputStream.writeObject(new Message("", name, ""));
            objectOutputStream.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                //задержка для ответа от сервера
                TimeUnit.MILLISECONDS.sleep(300);

                System.out.println("Кому(для отправки всем введите all)");
                String from = scanner.nextLine();
                System.out.println("Сообщение");
                String text = scanner.nextLine();
                objectOutputStream.writeObject(new Message(from, name, text));
                objectOutputStream.flush();

            }
        } catch (Exception e) {
            closeEverything(socket, objectOutputStream, objectInputStream);
        }

    }

    public void listenForMessage() {

        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    Message mes = (Message) objectInputStream.readObject();
                    System.out.println(mes);
                } catch (Exception e) {
                    closeEverything(socket, objectOutputStream, objectInputStream);
                }
            }
        }).start();

    }

    private void closeEverything(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
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
    //endregion
}
