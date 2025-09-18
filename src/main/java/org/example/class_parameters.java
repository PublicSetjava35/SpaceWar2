package org.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class class_parameters extends JPanel {
    static ArrayList<Player> players = new ArrayList<>();
    public static ArrayList<Point> bullet = new ArrayList<>();
    public static Rectangle rectangle, blockBarer;
    public static int width, height;
    public static boolean[] keys = new boolean[256];
    public static boolean[] keys2 = new boolean[256];
    public static AnnotationConfigApplicationContext context;
    public static class_Images[] images;
    public static DataInputStream inputStream;
    public static DataOutputStream outputStream;
    public static JFrame frame;
    public static JLabel label, errLabel;
    public static JButton[] buttons;
    public static JPanel[] panels;
    public static Timer timer, timer2;
    public static Timer timerErr, timerTime;
    public static Timer timerAnimal, timerBot, timerServerAnimal;
    public static int speed = 10;
    public static Thread thread;
    public static int playerX2 = 905;
    public static int playerY2 = 650;
    public static int XY = 0, XL = 0;
    public static float animalX = 1.F, animalY = 1.F, animalBotX = 8.F, animalServer = 1.F;
    public static boolean isAnimal = false;
    public static int playerX = 150, playerY = 150;
    public static int botX = 905, botY = 65;

    static class Player extends Point {
        public int playerX, playerY;
        Player(int playerX, int playerY) {
            this.playerY = playerY;
            this.playerX = playerX;
        }
    }
}
