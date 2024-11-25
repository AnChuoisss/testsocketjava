import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("0.0.0.0", 12345)) {
            System.out.println("Đã kết nối đến server!");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in);

            // Nhận và in thông điệp từ server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Gửi dữ liệu đến server
            String input;
            while (true) {
                input = scanner.nextLine();
                writer.write(input);
                writer.newLine();
                writer.flush();
                if (input.equalsIgnoreCase("exit")) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
