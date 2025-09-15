package org.example;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpaceWar extends class_parameters {
    public SpaceWar() {method_for_supported_parameters();}
    // server network
    public void for_network_parameters() {

    }
    // client local
    public void method_for_supported_parameters() {
        context = new AnnotationConfigApplicationContext(Images.class);

        class_Images images = context.getBean("image", class_Images.class);

        label = new JLabel(new ImageIcon(images.getImage()));

        frame = new JFrame("SpaceWar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,650);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.setLayout(null);

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
            }
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        Timer timer = new Timer(10, e -> {
            updateUI();
            if(keys[KeyEvent.VK_W]) playerY -= speed;
            if(keys[KeyEvent.VK_S]) playerY += speed;
            if(keys[KeyEvent.VK_A]) playerX -= speed;
            if(keys[KeyEvent.VK_D]) playerX += speed;
            repaint();
        });
        timer.start();
        buttons = new JButton[4];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            frame.add(buttons[i]);
        }
        panels = new JPanel[3];
        for(int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            frame.add(panels[i]);
        }
        buttons[0].setBounds(100,120,200,100);
        buttons[0].setFocusable(false);
        buttons[0].setFont(new Font("local", Font.PLAIN, 20));
        buttons[0].setText("одиночная игра");
        buttons[0].addActionListener( e-> {
            buttons[0].setVisible(false);
            buttons[1].setVisible(false);
            buttons[2].setVisible(false);
            buttons[3].setVisible(false);
            setVisible(true);
        });
        buttons[1].setBounds(100,350,200,100);
        buttons[1].setFocusable(false);
        buttons[1].setFont(new Font("settings", Font.PLAIN, 20));
        buttons[1].setText("настройки");
        buttons[1].addActionListener(e -> {

        });
        buttons[2].setBounds(100, 235, 200,100);
        buttons[2].setFocusable(false);
        buttons[2].setFont(new Font("online", Font.PLAIN, 20));
        buttons[2].setText("сетевая игра");
        buttons[2].addActionListener( e-> {

        });
        buttons[3].setBounds(100, 470, 200,100);
        buttons[3].setFocusable(false);
        buttons[3].setFont(new Font("exit", Font.PLAIN, 20));
        buttons[3].setText("выйти");
        buttons[3].addActionListener(e -> {System.exit(0);});
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "panel");
        frame.getRootPane().getActionMap().put("panel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panels[0].setVisible(false);
                panels[2].setVisible(false);
                // button
                buttons[0].setVisible(true);
                buttons[1].setVisible(true);
                buttons[2].setVisible(true);
                buttons[3].setVisible(true);
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
              int width = frame.getWidth(), height = frame.getHeight();;
              label.setBounds(0,0, width, height);
              setBounds(0,0, width, height);
            }
        });
        frame.add(this);
        frame.add(label);
        this.setVisible(false);
        frame.setVisible(true);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(playerX, playerY,100,100);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {new SpaceWar();}
        });
    }
}
