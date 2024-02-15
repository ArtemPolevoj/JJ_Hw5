import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //region fields
    private final ServerSocket serverSocket;
    //endregion

    //region Constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    //endregion

    //region methods
    public void runServer() {
        try (serverSocket) {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Подключен новый клиент!");
                ClientManager clientManager = new ClientManager(socket);
                Thread thread = new Thread(clientManager);
                thread.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //endregion
}
