import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            // Tạo địa chỉ IP cụ thể mà server sẽ lắng nghe
            InetAddress address = InetAddress.getByName("0.0.0.0");
            ServerSocket serverSocket = new ServerSocket(12345, 50, address); // Cổng 12345 với địa chỉ IP chỉ định
            System.out.println("Server đang chờ hai người chơi tại " + address);

            // Kết nối với người chơi 1
            Socket player1Socket = serverSocket.accept();
            System.out.println("Người chơi 1 đã kết nối!");
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(player1Socket.getOutputStream()));

            // Kết nối với người chơi 2
            Socket player2Socket = serverSocket.accept();
            System.out.println("Người chơi 2 đã kết nối!");
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(player2Socket.getOutputStream()));

            // Tạo trò chơi mới
            TicTacToe game = new TicTacToe();

            while (true) {
                BufferedWriter currentWriter = (game.getCurrentPlayer() == 'X') ? writer1 : writer2;
                BufferedReader currentReader = (game.getCurrentPlayer() == 'X') ? reader1 : reader2;

                // Gửi trạng thái bảng đến người chơi hiện tại
                currentWriter.write("Bảng hiện tại:\n" + game.getBoardString());
                currentWriter.write("Lượt của bạn! Nhập hàng và cột (ví dụ: 1 2):\n");
                currentWriter.flush();

                // Nhận nước đi
                String move = currentReader.readLine();
                String[] parts = move.split(" ");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (game.makeMove(row, col)) {
                    if (game.checkWin()) {
                        currentWriter.write("Bạn thắng!\n");
                        currentWriter.flush();

                        BufferedWriter otherWriter = (game.getCurrentPlayer() == 'X') ? writer2 : writer1;
                        otherWriter.write("Bạn thua! Người chơi " + game.getCurrentPlayer() + " đã thắng.\n");
                        otherWriter.flush();
                        break;
                    } else if (game.isDraw()) {
                        writer1.write("Trò chơi hòa!\n");
                        writer2.write("Trò chơi hòa!\n");
                        writer1.flush();
                        writer2.flush();
                        break;
                    }
                    game.switchPlayer();
                } else {
                    currentWriter.write("Nước đi không hợp lệ! Thử lại.\n");
                    currentWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
