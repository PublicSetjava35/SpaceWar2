package org.example;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class SpaceWar extends ClassParameters implements text_field {
    public SpaceWar() {method_for_supported_parameters();}
    // client local
    public void method_for_supported_parameters() {
                // jpg files
                context = new AnnotationConfigApplicationContext(Images.class);
                images = new ClassImages[]{
                context.getBean("image", ClassImages.class),
                context.getBean("image2", ClassImages.class),
                context.getBean("image3", ClassImages.class),
                context.getBean("image4", ClassImages.class),
                context.getBean("wall", ClassImages.class)};
                // audio files .mp3
                audioContext = new AnnotationConfigApplicationContext(AudioFormat.class);
                audios = new ClassAudio[]{audioContext.getBean("entered", ClassAudio.class),
                audioContext.getBean("err", ClassAudio.class)};
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
        panels = new JPanel[7];
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {keys2[e.getKeyCode()] = true;}
            public void keyReleased(KeyEvent e) {keys2[e.getKeyCode()] = false;}
        });

        // создаем поле жизней
        xp_player_label = new JLabel(type.HEAL + ":[ " + "1024" + " ]");
        xp_player_label.setBounds(0,this.getHeight() + 925, 125, 100);
        xp_player_label.setFont(new Font("HEAL", Font.PLAIN, 20));
        xp_player_label.setForeground(Color.RED);

        xp_bot_label = new JLabel(type.HEAL + ":[ " + "1024" + " ]");
        xp_bot_label.setBounds(0, -25, 125, 100);
        xp_bot_label.setFont(new Font("HEAL", Font.PLAIN, 20));
        xp_bot_label.setForeground(Color.RED);

        ammo_player_label = new JLabel(type.AMMO + ":[ " + "128" + " ]");
        ammo_player_label.setBounds(150,this.getHeight() + 925, 150,100);
        ammo_player_label.setFont(new Font("AMMO", Font.PLAIN, 20));
        ammo_player_label.setForeground(Color.RED);

        timerEagle = new Timer(100, e -> {
            if(keys2[KeyEvent.VK_SPACE]) {
                bullets.add(new Point(playerX2 + 5, playerY2 + 5));
                PLAYER_AMMO -= AMMO_BREAK;
                if(PLAYER_AMMO <= 0) PLAYER_AMMO = 0;
                ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
                if(PLAYER_AMMO == 0) ((Timer) e.getSource()).stop();
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_R) PLAYER_AMMO = 128;
                if(PLAYER_AMMO == 128) timerEagle.start();
                ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
            }
        });
        timer_bot = new Timer(100, e -> {
            bot_bullets.add(new Point(botX+5, botY+5));
        });

        timer2 = new Timer(10, e -> {
            panels[0].updateUI();
             nowY = playerY2;
             nowX = playerX2;
            if(keys2[KeyEvent.VK_W]) nowY -= speed;
            if(nowY <= 0) nowY = 0;
            if(keys2[KeyEvent.VK_S]) nowY += speed;
            if(nowY >= this.getHeight() - 130) nowY = this.getHeight() - 130;
            if(keys2[KeyEvent.VK_A]) nowX -= speed;
            if(nowX <= 0) nowX = 0;
            if(keys2[KeyEvent.VK_D]) nowX += speed;
            if(nowX >= this.getWidth() - 100) nowX = this.getWidth() - 100;

            playerRECT = new Rectangle(nowX, nowY, 100,100);
            botRECT = new Rectangle(botX, botY, 100,100);

            wall = new Rectangle(wallX, wallY, this.getWidth()+100, 35);

            if(!playerRECT.intersects(wall)) {
                isWall = true;
                playerY2 = nowY;
                playerX2 = nowX;
            } else {
                isWall = false;
            }
            // пули бота
            Iterator<Point> iterator = bot_bullets.iterator();
            while(iterator.hasNext()) {
                Point bot_bullet = iterator.next();
                Rectangle bulletRectangle = new Rectangle(bot_bullet.x, bot_bullet.y, 5, 35);
                if (bulletRectangle.intersects(playerRECT)) {
                    XP_PLAYER -= BOT_DAMAGE;
                    if(XP_PLAYER <= 0) XP_PLAYER = 0;
                    xp_player_label.setText(type.HEAL + ":[ " + XP_PLAYER + " ]");
                    if(XP_PLAYER == 0) {
                        panels[5].setVisible(true);

                        if(timer2 != null) {
                            timer2.stop();
                        } else {System.out.println("timer2 not initialized");}
                        // initializations timerAnimal
                        if(timerAnimal != null) {
                            timerAnimal.stop();
                        } else {System.out.println("timerAnimal not initialized");}
                        // initializations timerAnimal
                        if(timerBot != null) {
                            timerBot.stop();
                        } else {System.out.println("timerBot not initialized");}
                        // initializations timerAnimal
                        if(bot_bullet_timer != null) {
                            bot_bullet_timer.stop();
                        } else { System.out.println("timerBot not initialized");}
                        // initializations timerAnimal
                        if(bulletTimer != null) {
                            bulletTimer.stop();
                        } else {System.out.println("timerBot not initialized");}
                        // initializations timerAnimal
                        if(timer_bot != null) {
                            timer_bot.stop();
                        } else {System.out.println("timerBot not initialized");}
                        if(timerEagle != null) {
                            timerEagle.start();
                        } else {System.out.println("timerBot not initialized");}
                        }
                    iterator.remove();
                }
            }
            // пули игрока
            Iterator<Point> iteratorRECT = bullets.iterator();
            while(iteratorRECT.hasNext()) {
                Point bullet = iteratorRECT.next();
                Rectangle bulletRectangle = new Rectangle(bullet.x, bullet.y, 5, 35);
                if (bulletRectangle.intersects(botRECT)) {
                    XP_BOT -= BULLET_DAMAGE;
                    if(XP_BOT <= 0) XP_BOT = 0;
                    xp_bot_label.setText(type.HEAL + ":[ " + XP_BOT + " ]");
                    if(XP_BOT == 0) {
                        panels[6].setVisible(true);

                        if(timer2 != null) {
                            timer2.stop();
                        } else {System.out.println("timer2 not initialized");}
                        // initializations timerAnimal
                        if(timerAnimal != null) {
                            timerAnimal.stop();
                        } else {System.out.println("timerAnimal not initialized");}
                        // initializations timerAnimal
                        if(timerBot != null) {
                            timerBot.stop();
                        } else {System.out.println("timerBot not initialized");}
                        // initializations timerAnimal
                        if(bot_bullet_timer != null) {
                            bot_bullet_timer.stop();
                        } else { System.out.println("timerBot not initialized");}
                        // initializations timerAnimal
                        if(bulletTimer != null) {
                            bulletTimer.stop();
                        } else {System.out.println("timerBot not initialized");}
                        // initializations timerAnimal
                        if(timer_bot != null) {
                            timer_bot.stop();
                        } else {System.out.println("timerBot not initialized");}
                        if(timerEagle != null) {
                            timerEagle.start();
                        } else {System.out.println("timerBot not initialized");}
                    }
                    iteratorRECT.remove();
                }
            }
            panels[0].repaint();
        });
        // offline
        panels[3] = new JPanel();
        panels[3].setBounds(750,405,465,100);
        panels[3].setBackground(Color.BLACK);
        panels[3].setLayout(null);
        // server 
        panels[4] = new JPanel();
        panels[4].setBounds(750,445, 465,100);
        panels[4].setBackground(Color.BLACK);
        panels[4].setLayout(null);

        JLabel die_label = new JLabel("поражение");
        die_label.setBounds(170,-30,150,100);
        die_label.setFont(new Font("label", Font.PLAIN, 20));
        die_label.setForeground(Color.RED);

        JLabel die_label_2 = new JLabel("победа");
        die_label_2.setBounds(185,-30,150,100);
        die_label_2.setFont(new Font("label", Font.PLAIN, 20));
        die_label_2.setForeground(Color.RED);
        panels[5] = new JPanel();
        panels[5].add(die_label);
        panels[5].setBounds(panels[4].getX(), panels[4].getY(), 465, 125);
        panels[5].setBackground(Color.BLACK);

        panels[6] = new JPanel();
        panels[6].add(die_label_2);
        panels[6].setBounds(panels[5].getX(), panels[5].getY(), 465, 125);
        panels[6].setBackground(Color.BLACK);

        panels[0] = new JPanel() {
          public void paintComponent(Graphics g) {
              super.paintComponent(g);
              Graphics2D g2 = (Graphics2D) g;
              g2.setColor(Color.BLACK);
              g2.drawImage(images[1].getImage2(), 0,0, this.getWidth(), this.getHeight(), null);
              g2.fillRect(playerX2, playerY2, 100, 100);
              g2.setColor(Color.BLACK);
              g2.fillRect(botX, botY, 100,100);
              if(!isWall) {
                  g2.drawImage(images[4].getImage5(), wallX, wallY, this.getWidth(), 50, null);
              }
              for(Point bullet:new ArrayList<>(bullets)) {
                  g2.setColor(Color.RED);
                  g2.fillRect(bullet.x, bullet.y, 5,35);
              }
              bullets.removeIf(bullet -> bullet.y == 5);
              for(Point bot_bullet:new ArrayList<>(bot_bullets)) {
                  g2.setColor(Color.RED);
                  g2.fillRect(bot_bullet.x, bot_bullet.y, 5,35);
              }
              bot_bullets.removeIf(bot_bullet -> bot_bullet.y >= this.getHeight() - 100);
              // создаем жизни
              g2.setColor(Color.BLACK);
              g2.fillRect(0, this.getHeight() - 80,300,50);
              g2.setColor(Color.BLACK);
              g2.fillRect(0,0,300,50);
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
            button_false();
            timer2.start();
            timerTime.start();
            bulletTimer.start();
            timerEagle.start();
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
            button_false();
        });
        buttons[1].setVisible(false);
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
                 button_false();
             } catch (IOException exception) {
                 panels[2].setVisible(true);
                 audios[1].err();
                 timerErr.start();
                 System.err.println("server disconnected -> " + exception);
             }
        });
        buttons[3].setBounds(100, 350, 200,100);
        buttons[3].setFocusable(false);
        buttons[3].setFont(new Font("exit", Font.PLAIN, 20));
        buttons[3].setText("выйти");
        buttons[3].setBackground(Color.BLUE);
        buttons[3].setForeground(Color.WHITE);
        buttons[3].addActionListener(e -> System.exit(0));

        button_continue = new JButton("продолжить");
        button_continue.setBounds(310,15,155,70);
        button_continue.setFocusable(false);
        button_continue.setFont(new Font("continue", Font.PLAIN, 20));
        button_continue.setBackground(Color.BLUE);
        button_continue.setForeground(Color.WHITE);

        button_continue.addActionListener( e -> {
            panels[3].setVisible(false);
            timer2.start();
            timerTime.start();
            bulletTimer.start();
            timerEagle.start();
            bot_bullet_timer.start();
        });
        update = new JButton("повторить");
        update.setBounds(155, 15, 150,70);
        update.setFocusable(false);
        update.setFont(new Font("повторить", Font.PLAIN, 20));
        update.setBackground(Color.BLUE);
        update.setForeground(Color.WHITE);
        update.addActionListener( e -> {
            panels[3].setVisible(false);
            panels[5].setVisible(false);
            panels[6].setVisible(false);
            bullets.clear();
            bot_bullets.clear();
            timer2.start();
            timerTime.start();
            bulletTimer.start();
            timerEagle.start();
            bot_bullet_timer.start();
            XP_PLAYER = 1024;
            XP_BOT = 1024;
            PLAYER_AMMO = 128;
            xp_player_label.setText(type.HEAL + ":[ " + XP_PLAYER + " ]");
            ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
            xp_bot_label.setText(type.HEAL + ":[ " + XP_BOT + " ]");
            XL = 0;
            playerX2 = 905;
            playerY2 = 650;
            botX = 905;
            botY = 65;
        });
        menu = new JButton("меню");
        menu.setBounds(0,15, 150,70);
        menu.setFocusable(false);
        menu.setBackground(Color.BLUE);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("menu", Font.PLAIN, 20));
        menu.addActionListener(e -> {
            button_true();
            panels_False();
            panels[4].setVisible(false);
            panels[5].setVisible(false);
            panels[6].setVisible(false);
            XP_BOT = 1024;
            XP_PLAYER = 1024;
            PLAYER_AMMO = 128;
            xp_player_label.setText(type.HEAL + ":[ " + XP_PLAYER + " ]");
            ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
            xp_bot_label.setText(type.HEAL + ":[ " + XP_BOT + " ]");
            // initializations timer2
            if(timer2 != null) {
                timer2.stop();
            } else {System.out.println("timer2 not initialized");}
            // initializations timerAnimal
            if(timerAnimal != null) {
                timerAnimal.stop();
            } else {System.out.println("timerAnimal not initialized");}
            // initializations timerAnimal
            if(timerBot != null) {
                timerBot.stop();
            } else {System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(bot_bullet_timer != null) {
                bot_bullet_timer.stop();
            } else { System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(bulletTimer != null) {
                bulletTimer.stop();
            } else {System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(timer_bot != null) {
                timer_bot.stop();
            } else {System.out.println("timerBot not initialized");}
            if(timerEagle != null) {
                timerEagle.start();
            } else {System.out.println("timerBot not initialized");}
            frame.setTitle(SpaceWar);
            bullets.clear();
            bot_bullets.clear();
            playerX2 = 905;
            playerY2 = 650;
            botX = 905;
            botY = 65;
            XP_PLAYER = 1024;
            PLAYER_AMMO = 128;
        });
        menu_2 = new JButton("меню");
        menu_2.setBounds(0,50, 220,70);
        menu_2.setFocusable(false);
        menu_2.setBackground(Color.BLUE);
        menu_2.setForeground(Color.WHITE);
        menu_2.setFont(new Font("menu", Font.PLAIN, 20));
        menu_2.addActionListener(e -> {
            button_true();
            panels_False();
            panels[4].setVisible(false);
            panels[5].setVisible(false);
            panels[6].setVisible(false);
            XP_BOT = 1024;
            XP_PLAYER = 1024;
            PLAYER_AMMO = 128;
            xp_player_label.setText(type.HEAL + ":[ " + XP_PLAYER + " ]");
            ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
            xp_bot_label.setText(type.HEAL + ":[ " + XP_BOT + " ]");
            // initializations timer2
            if(timer2 != null) {
                timer2.stop();
            } else {System.out.println("timer2 not initialized");}
            // initializations timerAnimal
            if(timerAnimal != null) {
                timerAnimal.stop();
            } else {System.out.println("timerAnimal not initialized");}
            // initializations timerAnimal
            if(timerBot != null) {
                timerBot.stop();
            } else {System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(bot_bullet_timer != null) {
                bot_bullet_timer.stop();
            } else { System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(bulletTimer != null) {
                bulletTimer.stop();
            } else {System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(timer_bot != null) {
                timer_bot.stop();
            } else {System.out.println("timerBot not initialized");}
            if(timerEagle != null) {
                timerEagle.start();
            } else {System.out.println("timerBot not initialized");}
            frame.setTitle(SpaceWar);
            bullets.clear();
            bot_bullets.clear();
            playerX2 = 905;
            playerY2 = 650;
            botX = 905;
            botY = 65;
            XP_PLAYER = 1024;
            PLAYER_AMMO = 128;
        });
        update_2 = new JButton("повторить");
        update_2.setBounds(240, 50, 220,70);
        update_2.setFocusable(false);
        update_2.setFont(new Font("повторить", Font.PLAIN, 20));
        update_2.setBackground(Color.BLUE);
        update_2.setForeground(Color.WHITE);
        update_2.addActionListener( e -> {
            panels[5].setVisible(false);
            panels[6].setVisible(false);
            bullets.clear();
            bot_bullets.clear();
            timer2.start();
            timerTime.start();
            bulletTimer.start();
            timerEagle.start();
            bot_bullet_timer.start();
            XP_PLAYER = 1024;
            XP_BOT = 1024;
            PLAYER_AMMO = 128;
            xp_player_label.setText(type.HEAL + ":[ " + XP_PLAYER + " ]");
            ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
            xp_bot_label.setText(type.HEAL + ":[ " + XP_BOT + " ]");
            XL = 0;
            playerX2 = 905;
            playerY2 = 650;
            botX = 905;
            botY = 65;
        });
        update_3 = new JButton("повторить");
        update_3.setBounds(240, 50, 220,70);
        update_3.setFocusable(false);
        update_3.setFont(new Font("повторить", Font.PLAIN, 20));
        update_3.setBackground(Color.BLUE);
        update_3.setForeground(Color.WHITE);
        update_3.addActionListener( e -> {
            panels[5].setVisible(false);
            panels[6].setVisible(false);
            bullets.clear();
            bot_bullets.clear();
            timer2.start();
            timerTime.start();
            bulletTimer.start();
            timerEagle.start();
            bot_bullet_timer.start();
            XP_PLAYER = 1024;
            XP_BOT = 1024;
            PLAYER_AMMO = 128;
            xp_player_label.setText(type.HEAL + ":[ " + XP_PLAYER + " ]");
            ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
            xp_bot_label.setText(type.HEAL + ":[ " + XP_BOT + " ]");
            XL = 0;
            playerX2 = 905;
            playerY2 = 650;
            botX = 905;
            botY = 65;
        });
        menu_3 = new JButton("меню");
        menu_3.setBounds(0,50, 220,70);
        menu_3.setFocusable(false);
        menu_3.setBackground(Color.BLUE);
        menu_3.setForeground(Color.WHITE);
        menu_3.setFont(new Font("menu", Font.PLAIN, 20));
        menu_3.addActionListener(e -> {
            button_true();
            panels_False();
            panels[4].setVisible(false);
            panels[5].setVisible(false);
            panels[6].setVisible(false);
            XP_BOT = 1024;
            XP_PLAYER = 1024;
            PLAYER_AMMO = 128;
            xp_player_label.setText(type.HEAL + ":[ " + XP_PLAYER + " ]");
            ammo_player_label.setText(type.AMMO + ":[ " + PLAYER_AMMO + " ]");
            xp_bot_label.setText(type.HEAL + ":[ " + XP_BOT + " ]");
            // initializations timer2
            if(timer2 != null) {
                timer2.stop();
            } else {System.out.println("timer2 not initialized");}
            // initializations timerAnimal
            if(timerAnimal != null) {
                timerAnimal.stop();
            } else {System.out.println("timerAnimal not initialized");}
            // initializations timerAnimal
            if(timerBot != null) {
                timerBot.stop();
            } else {System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(bot_bullet_timer != null) {
                bot_bullet_timer.stop();
            } else { System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(bulletTimer != null) {
                bulletTimer.stop();
            } else {System.out.println("timerBot not initialized");}
            // initializations timerAnimal
            if(timer_bot != null) {
                timer_bot.stop();
            } else {System.out.println("timerBot not initialized");}
            if(timerEagle != null) {
                timerEagle.start();
            } else {System.out.println("timerBot not initialized");}
            frame.setTitle(SpaceWar);
            bullets.clear();
            bot_bullets.clear();
            playerX2 = 905;
            playerY2 = 650;
            botX = 905;
            botY = 65;
            XP_PLAYER = 1024;
            PLAYER_AMMO = 128;
        });
        server_menu = new JButton("меню");
        server_menu.setBounds(5, 15, 200, 70);
        server_menu.setFocusable(false);
        server_menu.setFont(new Font("server_menu", Font.PLAIN, 20));
        server_menu.setBackground(Color.BLUE);
        server_menu.setForeground(Color.WHITE);

        server_menu.addActionListener( e -> {
            // серверная часть
            panels[4].setVisible(false);
            try {
                outputStream.writeByte(105);
                outputStream.flush();
            } catch (IOException ignored){}
            panels_False();
            button_true();
            if(timer != null) timer.stop();
            if(TimerServerBullet != null) TimerServerBullet.stop();
            if(thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        });

        server_continue = new JButton("продолжить");
        server_continue.setBounds(250,15,200,70);
        server_continue.setFocusable(false);
        server_continue.setFont(new Font("server_continue", Font.PLAIN, 20));
        server_continue.setBackground(Color.BLUE);
        server_continue.setForeground(Color.WHITE);
        server_continue.addActionListener(e -> panels[4].setVisible(false));

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "panel");
        frame.getRootPane().getActionMap().put("panel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(panels[3] != null) {
                    panels[3].setVisible(false);
                } else {System.out.println("panels not synchronized");}

                server_isEmptyIN = !server_isEmptyIN;    
                panels[4].setVisible(!server_isEmptyIN);
                isEmptyIN = !isEmptyIN;
                if(!isEmptyIN) {
                    panels[3].setVisible(true);
                    // initializations timer2
                    if(timer2 != null) {
                        timer2.stop();
                    } else {System.out.println("timer2 not initialized");}
                    // initializations timerAnimal
                    if(timerAnimal != null) {
                        timerAnimal.stop();
                    } else {System.out.println("timerAnimal not initialized");}
                    // initializations timerAnimal
                    if(timerBot != null) {
                        timerBot.stop();
                    } else {System.out.println("timerBot not initialized");}
                    // initializations timerAnimal
                    if(bot_bullet_timer != null) {
                        bot_bullet_timer.stop();
                    } else { System.out.println("timerBot not initialized");}
                    // initializations timerAnimal
                    if(bulletTimer != null) {
                        bulletTimer.stop();
                    } else {System.out.println("timerBot not initialized");}
                    // initializations timerAnimal
                    if(timer_bot != null) {
                        timer_bot.stop();
                    } else {System.out.println("timerBot not initialized");}
                    if(timerEagle != null) {
                        timerEagle.start();
                    } else {System.out.println("timerBot not initialized");}
                } else {
                    timer2.start();
                    timerTime.start();
                    bulletTimer.start();
                    timerEagle.start();
                    bot_bullet_timer.start();
                    XL = 0;
                    panels[3].setVisible(false);
                }
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
        menu.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                menu.setBackground(Color.CYAN);
                menu.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                menu.setBackground(Color.BLUE);
                menu.setForeground(Color.WHITE);
            }
        });
        update.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                update.setBackground(Color.CYAN);
                update.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                update.setBackground(Color.BLUE);
                update.setForeground(Color.WHITE);
            }
        });
        button_continue.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button_continue.setBackground(Color.CYAN);
                button_continue.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                button_continue.setBackground(Color.BLUE);
                button_continue.setForeground(Color.WHITE);
            }
        });
        server_menu.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                server_menu.setBackground(Color.CYAN);
                server_menu.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                server_menu.setBackground(Color.BLUE);
                server_menu.setForeground(Color.WHITE);
            }
        });
        server_continue.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                server_continue.setBackground(Color.CYAN);
                server_continue.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                server_continue.setBackground(Color.BLUE);
                server_continue.setForeground(Color.WHITE);
            }
        });
        menu_2.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                menu_2.setBackground(Color.CYAN);
                menu_2.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                menu_2.setBackground(Color.BLUE);
                menu_2.setForeground(Color.WHITE);
            }
        });
        update_2.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                update_2.setBackground(Color.CYAN);
                update_2.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                update_2.setBackground(Color.BLUE);
                update_2.setForeground(Color.WHITE);
            }
        });
        menu_3.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                menu_3.setBackground(Color.CYAN);
                menu_3.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                menu_3.setBackground(Color.BLUE);
                menu_3.setForeground(Color.WHITE);
            }
        });
        update_3.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                update_3.setBackground(Color.CYAN);
                update_3.setForeground(Color.BLACK);
                audios[0].run();
            }
            public void mouseExited(MouseEvent e) {
                update_3.setBackground(Color.BLUE);
                update_3.setForeground(Color.WHITE);
            }
        });
        // add panels and frame
        frame.add(panels[0]);
        frame.add(panels[1]);
        frame.add(panels[2]);
        panels[0].add(xp_player_label);
        panels[0].add(ammo_player_label);
        panels[0].add(xp_bot_label);
        this.add(panels[4]);
        panels[0].add(panels[3]);
        panels[0].add(panels[5]);
        panels[0].add(panels[6]);
        panels[3].add(menu);
        panels[3].add(update);
        panels[3].add(button_continue);
        panels[4].add(server_menu);
        panels[4].add(server_continue);
        panels[5].add(menu_2);
        panels[5].add(update_2);
        panels[6].add(menu_3);
        panels[6].add(update_3);
        frame.add(this);
        frame.add(label);
        panels[0].setLayout(null);
        panels[5].setLayout(null);
        panels[6].setLayout(null);
        this.setLayout(null);
        panels_False();
        panels[4].setVisible(false);
        panels[5].setVisible(false);
        panels[6].setVisible(false);
        xp_player_label.setVisible(true);
        frame.setVisible(true);
    }
    public void button_false() {
        // button false
        buttons[0].setVisible(false);
        buttons[1].setVisible(false);
        buttons[2].setVisible(false);
        buttons[3].setVisible(false);
    }
    public void button_true() {
        // button true
        buttons[0].setVisible(true);
//        buttons[1].setVisible(true);
        buttons[2].setVisible(true);
        buttons[3].setVisible(true);
    }
    public void panels_False() {
        // panels false
        panels[0].setVisible(false);
        panels[1].setVisible(false);
        panels[2].setVisible(false);
        panels[3].setVisible(false);
        this.setVisible(false);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawImage(images[3].getImage4(), 0,0, this.getWidth(), this.getHeight(), null);
        ArrayList<Player> snapshot;
        synchronized (players) {
            snapshot = new ArrayList<>(players);
        }
        for (Player player : snapshot) {
            g2.setColor(Color.BLACK);
            g2.fillRect(player.playerX, player.playerY, 100, 100);
        }
        g2.drawImage(images[4].getImage5(), wallX_server, wallY_server, this.getWidth(), 50, null);

        for (Point bullet : server_bullets) {
            g2.setColor(Color.RED);
            g2.fillRect(bullet.x, bullet.y, 35, 5);
        }
    }
    // server network
    public void for_network_parameters(Socket socket) throws IOException {
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        if(timer != null) timer.stop();
        if(TimerServerBullet != null) TimerServerBullet.stop();

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
        TimerServerBullet = new Timer(100, e -> {
            try {
                if(keys[KeyEvent.VK_SPACE]) {
                    outputStream.writeByte(108);
                    outputStream.flush();
                }
            } catch (IOException exception) {
                System.err.println("err, bullets -> " + exception);
            }
        });
        TimerServerBullet.start();
        players.add(new Player(905, 650));
            thread = new Thread(() -> {
                try {
                    while (true) {
                        updateUI();
                        int player = inputStream.readInt();
                        ArrayList<Player> newList = new ArrayList<>();
                        for (int i = 0; i < player; i++) {
                            playerX = inputStream.readInt();
                            playerY = inputStream.readInt();
                            newList.add(new Player(playerX, playerY));
                        }
                        synchronized (players) {
                           players = newList;
                        }
                        int bulletServer = inputStream.readInt();
                        ArrayList<Point> newBullets = new ArrayList<>();
                        for (int i = 0; i < bulletServer; i++) {
                            int x = inputStream.readInt();
                            int y = inputStream.readInt();

                            newBullets.add(new Point(x, y));
                        }
                        synchronized (server_bullets) {
                            server_bullets = newBullets;
                        }
                        repaint();
                    }
                } catch (IOException e) {
                    System.err.println("err bytes -> " + e);
                }
            });
            thread.start();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                try {
                    int width = getWidth(), height = getHeight();
                    String messagePanel = width + " " + height;

                    setBounds(0, 0, width, height);
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
                    if (botX >= this.getWidth() - 100) {
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
                    bot_bullet_timer.start();
                    timer_bot.start();
                }
            });
            bulletTimer = new Timer(12, e -> {
                for(Point bullet:bullets) bullet.y -= speed_bullet;
            });
            bulletTimer.start();
            bot_bullet_timer = new Timer(12, e -> {
                for(Point bot_bullet:bot_bullets) bot_bullet.y += speed_bullet;
            });
        }
    }
    public static void main(String[] runnable_pack_game) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {new Animals();}
        });
    }
}