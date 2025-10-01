package server;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Server {
    public static int PORT = 6000;
    public static int joined_PlayersGame = 0;
    public static int bullet_speed = 10;
    static boolean bulletThreadStarted = false;
    public static Random random = new Random();
    public static final ArrayList<Bullet> bullets = new ArrayList<>();
    public static final ArrayList<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ArrayList<Player> players = new ArrayList<>();
        if(!bulletThreadStarted) {
            bulletThreadStarted = true;
            new Thread(() -> {
                while (true) {
                    synchronized (bullets) {
                        bullets.removeIf(bullet -> bullet.y < 5);
                        for (Bullet bullet : new ArrayList<>(bullets)) {
                            bullet.y -= bullet_speed;
                        }
                    }
                    try {
                        Thread.sleep(12);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
        new Thread(() -> {
            while (true) {
                synchronized (clients) {
                    for(ClientHandler client:new ArrayList<>(clients)) {
                        client.ThreadClientsBytes();
                    }
                }
                try {
                    Thread.sleep(12);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        ServerSocket serverSocket = new ServerSocket(PORT);
        while(true) {
            Socket socket = serverSocket.accept();
            if(socket.isClosed()) break;
            int id = random.nextInt(100);
            Player player = new Player(905,650,10, 1.F,0,0, id);
            players.add(player);
            ClientHandler clientHandler = new ClientHandler(socket, players, player);
            synchronized (clients) {
                clients.add(clientHandler);
            }
            new Thread((clientHandler)).start();
            joined_PlayersGame++; // проверяем количество подключений!
        }
    }
    static class Player {
        public int playerX = 0, playerY = 0, speed = 0;
        public float animal = 1.F;
        public int nowX, nowY;
        public int id;
        public Player(int playerX, int playerY, int speed, float animal, int nowX, int nowY, int id) {
             this.playerY = playerY;
             this.playerX = playerX;
             this.animal = animal;
             this.speed = speed;
             this.nowY = nowY;
             this.nowX = nowX;
             this.id = id;
        }
    }
    static class Bullet {
        public int x = 0, y = 0;
        public int owner;
        Bullet(int x, int y, int owner) {
            this.owner = owner;
            this.x = x;
            this.y = y;
        }
    }
    static class ClientHandler implements Runnable {
        public int up = 85, down = 50, left = 45, right = 25;
        public int panelX = 1920, panelY = 1050;
        public Rectangle wall, playerWall;
        public DataOutputStream outputStream;
        public DataInputStream inputStream;
        static ArrayList<Player> players;
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
                    if(player.nowY <= 0) player.nowY = 0;
                    if(bytes == right) player.nowX += player.speed;
                    if(player.nowX > panelX - 100) player.nowX = panelX - 100;
                    // создаем пули
                    if(bytes == 108) {
                        synchronized (bullets) {
                            bullets.add(new Bullet(player.playerX + 5, player.playerY + 5, player.id));
                        }
                    }
                    Iterator<Bullet> iterator = bullets.iterator();
                    Rectangle bulletRECT = new Rectangle();
                    Rectangle playerRECT = new Rectangle();

                    while (iterator.hasNext()) {
                        Bullet bullet = iterator.next();
                        bulletRECT.setBounds(bullet.x, bullet.y, 5, 35);
                        for(Player player:players) {
                            if(player.id == bullet.owner) continue;
                            playerRECT.setBounds(player.nowX + 5, player.nowY + 5, 100, 100);
                            if (bulletRECT.intersects(playerRECT)) {
                                iterator.remove();
                                break;
                                }
                            }
                        }
                    // проверяем столкновение с барьером
                    wall = new Rectangle(0, 450, panelX, 50);
                    playerWall = new Rectangle(player.nowX, player.nowY, 100,100);
                    if(!playerWall.intersects(wall)) {
                        player.playerX = player.nowX;
                        player.playerY = player.nowY;
                    }
                    // после нажатие escape все удаляется
                    if (bytes == 105) {
                        synchronized (player) {
                            players.remove(player);
                        }
                        synchronized (clients) {
                            clients.remove(this);
                        }
                        synchronized (bullets) {
                            bullets.removeIf(bullet -> bullet.owner == player.id);
                        }
                        System.out.println("client disconnected");
                        break;
                    }
                    if(bytes == 95) {
                        int length = inputStream.readInt();
                        byte[] buffer = new byte[length];
                        inputStream.readFully(buffer);
                        String panels = new String(buffer, 0, length, StandardCharsets.UTF_8);
                        String[] splits = panels.trim().split("\\s+");
                        panelX = Integer.parseInt(splits[0]);
                        panelY = Integer.parseInt(splits[1]);
                    }
                }
            } catch (IOException e) {
                System.err.println("server disconnected -> " + e);
            } finally {
                synchronized (players) {
                    players.remove(player);
                }
                synchronized (clients) {
                    clients.remove(this);
                }
                synchronized (bullets) {
                    bullets.removeIf(bullet -> bullet.owner == player.id);
                }
            }
        }
        // будем получать параметры после подключения клиента
        public static void player_connected() {
        }
        public void ThreadClientsBytes() {
            if(outputStream == null) return;
            try {
                synchronized (players) {
                    outputStream.writeInt(players.size());
                    for(Player player:players) {
                        outputStream.writeInt(player.playerX);
                        outputStream.writeInt(player.playerY);
                    }
                }
                synchronized (bullets) {
                    outputStream.writeInt(bullets.size());
                    for(Bullet bullet:bullets) {
                        outputStream.writeInt(bullet.x);
                        outputStream.writeInt(bullet.y);
                    }
                }
                outputStream.flush();
            } catch (IOException exception) {
                synchronized (clients) {
                    clients.remove(this);
                    System.err.println("err bytes -> " + exception);
                }
            }
        }
    }
}
