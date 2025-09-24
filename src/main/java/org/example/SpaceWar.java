package org.example;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class SpaceWar extends class_parameters implements Text {
    private static final Log log = LogFactory.getLog(SpaceWar.class);

    public SpaceWar() {
        method_for_supported_parameters();}
    // client local
    public void method_for_supported_parameters() {
                // jpg files
                context = new AnnotationConfigApplicationContext(Images.class);
                images = new class_Images[]{
                context.getBean("image", class_Images.class),
                context.getBean("image2", class_Images.class),
                context.getBean("image3", class_Images.class),
                context.getBean("image4", class_Images.class),
                context.getBean("wall", class_Images.class)};
                // audio files .mp3
                audioContext = new AnnotationConfigApplicationContext(audio_format.class);
                audios = new class_audio[]{audioContext.getBean("entered", class_audio.class),
                audioContext.getBean("err", class_audio.class)};
        label = new JLabel(new ImageIcon(images[0].getImage()));

        frame = new JFrame(SpaceWar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,650);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.setLayout(null);

        buttons = new JButton[4];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            frame.add(buttons[i]);
        }
        panels = new JPanel[4];
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {keys2[e.getKeyCode()] = true;}
            public void keyReleased(KeyEvent e) {keys2[e.getKeyCode()] = false;}
        });
        timer2 = new Timer(10, e -> {
            panels[0].updateUI();
            int nowY = playerY2;
            int nowX = playerX2;
            if(keys2[KeyEvent.VK_W]) nowY -= speed;
            if(nowY <= 0) nowY = 0;
            if(keys2[KeyEvent.VK_S]) nowY += speed;
            if(nowY >= this.getHeight() - 130) nowY = this.getHeight() - 130;
            if(keys2[KeyEvent.VK_A]) nowX -= speed;
            if(nowX <= 0) nowX = 0;
            if(keys2[KeyEvent.VK_D]) nowX += speed;
            if(nowX >= this.getWidth() - 100) nowX = this.getWidth() - 100;
            rectangle = new Rectangle(nowX, nowY, 100,100);
            wall = new Rectangle(wallX, wallY, this.getWidth()+100, 35);

            if(!rectangle.intersects(wall)) {
                isWall = true;
                playerY2 = nowY;
                playerX2 = nowX;
            } else {
                isWall = false;
            }
            panels[0].repaint();
        });
        timerEagle = new Timer(100, e ->{
            if(keys2[KeyEvent.VK_SPACE]) {
                bullets.add(new Point(playerX2 + 5, playerY2 + 5));
                bullets.trimToSize();
            }
        });
        timerEagle.start();
        // offline
        panels[0] = new JPanel() {
          public void paintComponent(Graphics g) {
              super.paintComponent(g);
              Graphics2D g2 = (Graphics2D) g;
              g2.setColor(Color.BLACK);
              g2.drawImage(images[1].getImage2(), 0,0, this.getWidth(), this.getHeight(), null);
              g2.fillRect(playerX2, playerY2, 100, 100);
              g2.setColor(Color.RED);
              g2.fillRect(botX, botY, 100,100);
              if(!isWall) {
                  g2.drawImage(images[4].getImage5(), wallX, wallY, this.getWidth(), 50, null);
              }
              for(Point bullet:new ArrayList<>(bullets)) {
                  g2.setColor(Color.RED);
                  g2.fillRect(bullet.x, bullet.y, 5,35);
                  if(bullet.y == 5) bullets.remove(bullet);
              }
          }
        };
        panels[0].setFocusable(true);
        panels[0].requestFocusInWindow();
        // settings
        panels[1] = new JPanel() {
            public void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                graphics.drawImage(images[2].getImage3(), 0,0, this.getWidth(), this.getHeight(), null);
            }
        };
        errLabel = new JLabel(not_connected);
        errLabel.setBounds(0,0,100,100);
        errLabel.setFont(new Font(err, Font.PLAIN, 20));
        errLabel.setForeground(Color.RED);

        panels[2] = new JPanel();
        panels[2].setBounds(770,0,400,50);
        panels[2].setBackground(Color.BLUE);
        panels[2].add(errLabel);
        // offline
        buttons[0].setBounds(100,120,200,100);
        buttons[0].setFocusable(false);
        buttons[0].setFont(new Font(local, Font.PLAIN, 20));
        buttons[0].setBackground(Color.BLUE);
        buttons[0].setForeground(Color.WHITE);
        buttons[0].setText("одиночная игра");
        buttons[0].addActionListener( e -> {
            frame.setTitle(SpaceWar+offline);
            isJButton_false();
            timer2.start();
            timerTime.start();
            panels[0].setVisible(true);
        });
        buttons[1].setBounds(100,350,200,100);
        buttons[1].setFocusable(false);
        buttons[1].setFont(new Font(settings, Font.PLAIN, 20));
        buttons[1].setBackground(Color.BLUE);
        buttons[1].setForeground(Color.WHITE);
        buttons[1].setText("настройки");
        buttons[1].addActionListener(e -> {
            panels[1].setVisible(true);
            isJButton_false();
        });
        buttons[2].setBounds(100, 235, 200,100);
        buttons[2].setFocusable(false);
        buttons[2].setFont(new Font("online", Font.PLAIN, 20));
        buttons[2].setText("сетевая игра");
        buttons[2].setBackground(Color.BLUE);
        buttons[2].setForeground(Color.WHITE);
        buttons[2].addActionListener( e-> {
             try {
                 Socket socket = new Socket("localhost", PORT);
                 for_network_parameters(socket);
                 frame.setTitle(SpaceWar+online);
                 isJButton_false();
             } catch (IOException exception) {
                 panels[2].setVisible(true);
                 audios[1].err();
                 timerErr.start();
                 System.err.println("server disconnected -> " + exception);
             }
        });
        buttons[3].setBounds(100, 470, 200,100);
        buttons[3].setFocusable(false);
        buttons[3].setFont(new Font("exit", Font.PLAIN, 20));
        buttons[3].setText("выйти");
        buttons[3].setBackground(Color.BLUE);
        buttons[3].setForeground(Color.WHITE);
        buttons[3].addActionListener(e -> System.exit(0));

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "panel");
        frame.getRootPane().getActionMap().put("panel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                panels_False();
                isJButton_true();
                if (outputStream != null)
                    try {
                        outputStream.writeByte(105);
                        outputStream.flush();
                    } catch (IOException ignored) {
                  } else  {System.err.println("server, not connected");}
                // initializations timer
                if(timer != null) {
                    timer.stop();
                } else { System.err.println("timer not initialized");}
                // initializations timer2
                if(timer2 != null) {
                    timer2.stop();
                } else {System.err.println("timer2 not initialized");}
                // initializations timerAnimal
                if(timerAnimal != null) {
                    timerAnimal.stop();
                } else {System.err.println("timerAnimal not initialized");}
                if(timerBot != null) {
                    timerBot.stop();
                } else {System.err.println("timerBot not initialized");}
                frame.setTitle(SpaceWar);
                botX = 905; botY = 65; playerX2 = 905; playerY2 = 650;
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
              int width = frame.getWidth(), height = frame.getHeight();
              label.setBounds(0,0, width, height);
              panels[0].setBounds(0,0, width, height);
              panels[1].setBounds(0,0, width, height);
              setBounds(0,0, width, height);
            }
        });
        buttons[0].addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttons[0].setBackground(Color.CYAN);
                buttons[0].setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                buttons[0].setBackground(Color.BLUE);
                buttons[0].setForeground(Color.WHITE);
            }
        });
        buttons[1].addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttons[1].setBackground(Color.CYAN);
                buttons[1].setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                buttons[1].setBackground(Color.BLUE);
                buttons[1].setForeground(Color.WHITE);
            }
        });
        buttons[2].addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttons[2].setBackground(Color.CYAN);
                buttons[2].setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                buttons[2].setBackground(Color.BLUE);
                buttons[2].setForeground(Color.WHITE);
            }
        });
        buttons[3].addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttons[3].setBackground(Color.CYAN);
                buttons[3].setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                buttons[3].setBackground(Color.BLUE);
                buttons[3].setForeground(Color.WHITE);
            }
        });
        // add panels and frame
        frame.add(panels[0]);
        frame.add(panels[1]);
        frame.add(panels[2]);
        frame.add(this);
        frame.add(label);
        panels_False();
        frame.setVisible(true);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawImage(images[3].getImage4(), 0,0,this.getWidth(), this.getHeight(), null);
        ArrayList<Player> snapshot = players;
        for (Player player : snapshot) {
            g2.fillRect(player.playerX, player.playerY, 100, 100);
        }
        g2.drawImage(images[4].getImage5(), wallX_server, wallY_server, this.getWidth(), 50, null);
        ArrayList<Point> buffer = server_bullets;
        for (Point bullet:buffer) {
            g2.setColor(Color.RED);
            g2.fillRect(bullet.x, bullet.y, 5,35);
        }
    }
    public void isJButton_false() {
        // button false
        buttons[0].setVisible(false);
        buttons[1].setVisible(false);
        buttons[2].setVisible(false);
        buttons[3].setVisible(false);
    }
    public void isJButton_true() {
        // button true
        buttons[0].setVisible(true);
        buttons[1].setVisible(true);
        buttons[2].setVisible(true);
        buttons[3].setVisible(true);
    }
    public void panels_False() {
        // panels false
        panels[0].setVisible(false);
        panels[1].setVisible(false);
        panels[2].setVisible(false);
        this.setVisible(false);
    }
    // server network
    public void for_network_parameters(Socket socket) throws IOException{
        outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {keys[e.getKeyCode()] = true;}

            public void keyReleased(KeyEvent e) {keys[e.getKeyCode()] = false;}
        });
        this.setFocusable(true);
        this.requestFocusInWindow();
        timer = new Timer(10, e -> {
            try {
                if(keys[KeyEvent.VK_W]) outputStream.writeByte(up);
                if(keys[KeyEvent.VK_S]) outputStream.writeByte(down);
                if(keys[KeyEvent.VK_A]) outputStream.writeByte(left);
                if(keys[KeyEvent.VK_D]) outputStream.writeByte(right);
                outputStream.flush();
            } catch (IOException ee) {
                System.err.println("err bytes -> " + ee);
            }
        });
        timer.start();
        players.add(new Player(905, 650));
        TimerServerBullet = new Timer(100, e -> {
            try {
                if(keys[KeyEvent.VK_SPACE]) {
                    outputStream.writeByte(108);
                    outputStream.writeInt(playerX+5);
                    outputStream.writeInt(playerY+5);
                    outputStream.flush();
                }
                outputStream.flush();
            } catch (IOException exception) {
                System.err.println("err, bullets -> " + exception);
            }
        });
        TimerServerBullet.start();
        thread = new Thread(() -> {
            try {
                while (true) {
                    updateUI();
                    int player = inputStream.readInt();
                    ArrayList<Player> newList = new ArrayList<>();
                    for(int i = 0; i < player; i++) {
                         playerY = inputStream.readInt();
                         playerX = inputStream.readInt();
                         // проверяем столкновение всего экрана
                        this.addComponentListener(new ComponentAdapter() {
                            public void componentResized(ComponentEvent e) {
                                try {
                                    int width = getWidth(), height = getHeight();
                                    String messagePanel = width + " " + height;
                                    setBounds(0,0, width, height);
                                    byte[] bytesPanels = messagePanel.getBytes(StandardCharsets.UTF_8);
                                    outputStream.writeByte(95);
                                    outputStream.writeInt(messagePanel.length());
                                    outputStream.write(bytesPanels);
                                    outputStream.flush();
                                } catch (IOException ee) {
                                    System.err.println("err bytes -> " + ee);
                                }
                            }
                        });
                         newList.add(new Player(playerX, playerY));
                    }
                    players = newList;
                    int bulletServer = inputStream.readInt();
                    ArrayList<Point> newBullets = new ArrayList<>();
                    for (int i = 0; i < bulletServer; i++) {
                        int x = inputStream.readInt();
                        int y = inputStream.readInt();
                        newBullets.add(new Point(x, y));
                    }
                    server_bullets = newBullets;
                    repaint();
                }
            } catch (IOException e) {
                System.err.println("err bytes -> " + e);
            }
        });
        thread.start();
        setVisible(true);
    }
    static class Animals extends SpaceWar {
        public Animals() {
            timerAnimal = new Timer(10, e -> {
                playerX2 += (int) animalX;
                playerY2 += (int) animalY;
                if (playerX2 >= this.getWidth() - 100) {
                    animalX = (int) -Math.abs(animalX);
                } else if(playerX2 <= 0) {
                    animalX = (int) Math.abs(animalX);
                } else if(playerY2 >= this.getHeight() - 130) {
                    animalY = (int) -Math.abs(animalY);
                } else if(playerY2 <= 500) {
                    animalY = (int) Math.abs(animalY);
                }
            });
            timerBot = new Timer(10, e -> {
             botX += (int) animalBotX;
             if(botX >= this.getWidth() - 100) {
                 animalBotX = -Math.abs(animalBotX);
             } else if (botX < 0) {
                 animalBotX = Math.abs(animalBotX);
             }
            });
            frame.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        isAnimal = !isAnimal;
                        if(isAnimal) {
                            timerAnimal.start();
                        } else {timerAnimal.stop();}
                    }
                }
            });
            timerErr = new Timer(300, e -> {
                XY++;
                if(XY == 3) {
                    XY = 0;
                    ((Timer)e.getSource()).stop();
                    panels[2].setVisible(false);
                }
            });
            timerTime = new Timer(200, e -> {
                XL++;
                if(XL == 10) {
                    XL = 0;
                    ((Timer) e.getSource()).stop();
                    timerBot.start();
                }
            });
            bulletTimer = new Timer(12, e -> {
                for(Point bullet:bullets) bullet.y -= speed_bullet;
            });
            bulletTimer.start();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {new Animals();}
        });
    }
}