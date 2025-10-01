package org.example;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

@AllArgsConstructor
public class class_parameters extends JPanel {
    static ArrayList<Player> players = new ArrayList<>();
    public static final ArrayList<Point> bullets = new ArrayList<>();
    public static final ArrayList<Point> bot_bullets = new ArrayList<>();
    public static ArrayList<Point> server_bullets = new ArrayList<>();
    public static Rectangle playerRECT, wall;
    public static Rectangle botRECT;
    public static AnnotationConfigApplicationContext context, audioContext;
    public static class_Images[] images;
    public static class_audio[] audios;
    public static DataInputStream inputStream;
    public static DataOutputStream outputStream;
    public static JFrame frame;
    public static JLabel label, errLabel;
    public static JButton[] buttons;
    public static JButton menu, update, button_continue;
    public static JButton menu_2, update_2, menu_3, update_3;
    public static JButton server_menu, server_continue;
    public static JPanel[] panels;
    public static Timer timer, timer2;
    public static Timer timerErr, timerTime;
    public static Timer timerAnimal, timerBot;
    public static Timer bulletTimer, timerEagle;
    public static Timer TimerServerBullet;
    public static Timer timer_bot;
    public static Timer bot_bullet_timer;
    public static JLabel xp_player_label;
    public static JLabel xp_bot_label;
    public static JLabel ammo_player_label;
    public static Thread thread;
    public static int XP_PLAYER = 1024, XP_BOT = 1024;
    public static int BULLET_DAMAGE = 30, BOT_DAMAGE = 30;
    public static int PLAYER_AMMO = 128;
    public static int AMMO_BREAK = 1;
    public static int wallX = 0, wallY = 475;
    public static boolean[] keys = new boolean[256];
    public static boolean[] keys2 = new boolean[256];
    public static boolean isWall = true;
    public static boolean isEmptyIN = false;
    public static boolean server_isEmptyIN = false;
    public static int speed = 10, speed_bullet = 10;
    public static int playerX2 = 905;
    public static int playerY2 = 650;
    public static int nowX = 0;
    public static int nowY = 0;
    public static int wallX_server = 0, wallY_server = 475;
    public static int XY = 0, XL = 0;
    public static float animalX = 1.F, animalY = 1.F, animalBotX = 8.F;
    public static boolean isAnimal = false;
    public static int playerX = 150, playerY = 150;
    public static int botX = 905, botY = 65;
    public static int PORT = 6000;

    static class Player extends Point {
        public int playerX, playerY;
        Player(int playerX, int playerY) {
            this.playerY = playerY;
            this.playerX = playerX;
        }
    }
}
