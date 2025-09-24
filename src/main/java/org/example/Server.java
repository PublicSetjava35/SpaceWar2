package org.example;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class Server {
    public static int PORT = 6000;
    public static int joined_PlayersGame = 0;
    public static void main(String[] args) throws IOException {
        ArrayList<Player> players = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket(PORT);
        while(true) {
            Socket socket = serverSocket.accept();
            Player player = new Player(905,650,10, 1.F,0,0);
            players.add(player);
            Thread thread = new Thread(new ClientHandler(socket, players, player));
            thread.start();
            joined_PlayersGame++; // читаем количество подключений!
            System.out.println("client connected -> " + thread.getName());
        }
    }
    static class Player {
        public int playerX = 0, playerY = 0, speed = 0;
        public float animal = 1.F;
        public int nowX, nowY;
        public Player(int playerX, int playerY, int speed, float animal, int nowX, int nowY) {
             this.playerY = playerY;
             this.playerX = playerX;
             this.animal = animal;
             this.speed = speed;
             this.nowY = nowY;
             this.nowX = nowX;
        }
    }
    static class ClientHandler implements Runnable {
        public int up = 85, down = 50, left = 45, right = 25;
        public int panelX = 1820, panelY = 1080;
        static ArrayList<Point> bullets = new ArrayList<>();
        private static boolean bulletsThreadStarted = false;
        public Rectangle wall, playerWall;
        public DataOutputStream outputStream;
        public DataInputStream inputStream;
        static ArrayList<Player> players;
        public int bullet_speed = 10;
        Socket socket;
        final Player player;

        public ClientHandler(Socket socket, ArrayList<Player> players, Player player) {
            this.socket = socket;
            this.player = player;
            ClientHandler.players = players;
        }
        @Override
        public void run() {
            try {
                outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                while (true) {
                    int bytes = inputStream.readByte();
                    if(bytes == -1) break;
                    player.nowX = player.playerX;
                    player.nowY = player.playerY;
                    if(bytes == up) player.nowY -= player.speed;
                    if(player.nowX <= 0) player.nowX = 0;
                    if(bytes == down) player.nowY += player.speed;
                    if(player.nowY > panelY - 130) player.nowY = panelY - 130;
                    if(bytes == left) player.nowX -= player.speed;
                    if(player.nowY < 0) player.nowY = 0;
                    if(bytes == right) player.nowX += player.speed;
                    if(player.nowX > panelX - 100) player.nowX = panelX - 100;
                    // создаем пули
                    if(bytes == 108) {
                        int playerX = inputStream.readInt();
                        int playerY = inputStream.readInt();
                        synchronized (bullets) {
                            bullets.add(new Point(playerX + 5, playerY + 5));
                        }
                    }
                    if (!bulletsThreadStarted) {
                        bulletsThreadStarted = true;
                        new Thread(() -> {
                            while (true) {
                                try {
                                    synchronized (ClientHandler.bullets) {
                                        ClientHandler.bullets.removeIf(b -> b.y < 0);
                                        for (Point bullet : ClientHandler.bullets) {
                                            bullet.y -= bullet_speed;
                                        }
                                    }
                                    Thread.sleep(12);
                                } catch (InterruptedException ignored) {}
                            }
                        }).start();
                    }
                    // проверяем столкновение с барьером
                    wall = new Rectangle(0, 450, panelX, 50);
                    playerWall = new Rectangle(player.nowX, player.nowY, 100,100);
                    if(!playerWall.intersects(wall)) {
                        player.playerX = player.nowX;
                        player.playerY = player.nowY;
                    }
                    // после нажатие escape игрок убирается
                    if (bytes == 105) {
                        players.remove(player);
                        System.out.println("client disconnected");
                    }
                    if(bytes == 95) {int length = inputStream.readInt();
                        byte[] buffer = new byte[length];
                        inputStream.readFully(buffer);
                        String panels = new String(buffer, 0, length, StandardCharsets.UTF_8);
                        String[] splits = panels.trim().split("\\s+");
                        panelX = Integer.parseInt(splits[0]);
                        panelY = Integer.parseInt(splits[1]);
                    }
                    synchronized (players) {
                        outputStream.writeInt(players.size());
                        for (Player player : players) {
                            outputStream.writeInt(player.playerY);
                            outputStream.writeInt(player.playerX);
                        }
                        outputStream.flush();
                    }
                    synchronized (bullets) {
                        outputStream.writeInt(bullets.size());
                        for(Point bullet:bullets) {
                            outputStream.writeInt(bullet.y);
                            outputStream.writeInt(bullet.x);
                        }
                        outputStream.flush();
                    }
                }
            } catch (IOException e) {
                System.err.println("server disconnected -> " + e);
            } finally {
                synchronized (players) {
                    players.remove(player);
                }
            }
        }
    }
}
