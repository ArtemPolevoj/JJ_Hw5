import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите своё имя: ");
            String name = scanner.nextLine();
            InetAddress address = InetAddress.getLocalHost();
            Socket socket = new Socket(address, 4300);
            Client client = new Client(socket, name);
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("InetAddress: " + inetAddress);
            String remoteIp = inetAddress.getHostAddress();
            System.out.println("Remote IP: " + remoteIp);
            System.out.println("LocalPort:" + socket.getLocalPort());
            client.listenForMessage();
            client.sendMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}