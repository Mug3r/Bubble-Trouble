/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.main;

import com.BulletRun.main.Entities.Enemy;
import com.BulletRun.main.Entities.Player;
import com.BulletRun.main.Entities.PowerUp;
import com.BulletRun.main.Entities.Text;
import com.BulletRun.main.Projectiles.Bullet;
import com.BulletRun.main.Projectiles.Explosion;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Mug3r
 */
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
    //FIELDS
    public static int WIDTH = 1280;
    public static int HEIGHT = 920;
    
    private Thread thread;
    int re = (int)(Math.random()*255+1), gr = (int)(Math.random()*255+1), bl = (int)(Math.random()*255+1);
    private boolean colorup;
    
    private boolean running = false;
    private boolean devmode = false;
    
    private BufferedImage image;
    private Graphics2D g;
    
    private int FPS = 45;
    private double averageFPS;
    
    public static Player player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<PowerUp> powerups;
    public static ArrayList<Explosion>explosions;
    public static ArrayList<Text> texts;
    
    private long waveStartTimer;
    private long startTimerDiff;
    private int waveNumber;
    private boolean waveStart;
    private int waveDelay = 2000;
    
    private long slowDownTimer;
    private long slowDownTimerDiff;
    private int slowDownLength = 6000;
    
    private boolean highscoresLoaded = false;
    
    
    
    
    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocus();
        
        
       }
    //METHODS
    public void addNotify(){
        super.addNotify();
        
        if(thread == null){
        thread = new Thread(this);
        thread.start();
        }
        addKeyListener(this);
        addMouseListener(this);
    }
        public void run() {
            
            running = true;
            
            image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            ;
            
            player = new Player();
            bullets = new ArrayList<Bullet>();
            enemies = new ArrayList<Enemy>();
            powerups = new ArrayList<PowerUp>();
            explosions = new ArrayList<Explosion>();
            texts = new ArrayList<Text>();
            
            waveStartTimer = 0;
            startTimerDiff = 0;
            waveStart = true;
            waveNumber = 0;
                        
            long startTime;
            long URDTime;
            long waitTime;
            long totalTime = 0;
            
            int frameCount = 0;
            int maxFrameCount = 30;
            
            long targetTime = 1000 / FPS;
            
            //GAME LOOP
            while(running){
                
            startTime = System.nanoTime();
                        
            gameUpdate();
            gameRender();
            gameDraw();
            
            if(!highscoresLoaded){
            getHighScores();
            highscoresLoaded=true;}
            
            URDTime = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - URDTime;
            
            try{
            thread.sleep(waitTime);}
            catch(Exception e){
            }
            
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == maxFrameCount){
            averageFPS = 1000.0 / ((totalTime / frameCount)) / 1000000;
            frameCount = 0;
            totalTime = 0;
            }
            
            }
            if(player.isDead()){
                g.setColor(new Color(255, 0,0, 100));
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.setColor(Color.WHITE);
                g.setFont(new Font("century Gothic", Font.PLAIN,16));
                String s = "- D E F E A T -";            
                int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
                s = "Final Score: " + player.getScore();
                length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 40);
                gameDraw();
                saveHighScores();}
                else if(!player.isDead()){
                    g.setColor(new Color(0, 100, 255, 100));
                    g.fillRect(0, 0, WIDTH, HEIGHT);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("century Gothic", Font.PLAIN,16));
                    String s = "- V I C T O R Y -";
                    int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                    g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
                    s = "Final Score: " + player.getScore();
                    length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                    g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 40);
                    gameDraw();
                    saveHighScores();}
            
            
                
            
        }
        
        private void gameUpdate() {
            //new wave
            if(waveStartTimer == 0 && enemies.size() == 0){
            
                waveNumber++;
                waveStart = false;
                waveStartTimer = System.nanoTime();
            
            }
            else {
                startTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
                if(startTimerDiff > waveDelay){
                waveStart = true;
                waveStartTimer = 0;
                startTimerDiff = 0;
                }
            }
            
            //create enemies
            if(waveStart && enemies.size() == 0){
            createNewEnemies();
            }   
                   
            //Player update
            player.update();
            //Bullet update
            for(int i = 0; i < bullets.size(); i++){
                boolean remove = bullets.get(i).update();
                if(remove){
                    bullets.remove(i);
                    i--;
                }
               
            }
            //Enemy update
            for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).update();
            }
            
            //PowerUp update
            for(int i = 0; i < powerups.size(); i++){
            
                boolean remove = powerups.get(i).update();
                if(remove){
                    powerups.remove(i);
                    i--;
                }
            
            }
            
            //Bullet - Enemy Collision
            for(int i =0; i<bullets.size(); i++){
            
                Bullet b = bullets.get(i);
                double bx = b.getx();
                double by = b.gety();
                double br = b.getr();
                
                for(int j = 0; j < enemies.size(); j++){
                
                    Enemy e = enemies.get(j);
                    double ex = e.getx();
                    double ey = e.gety();
                    double er = e.getr();
                    
                    double dx = bx - ex;
                    double dy = by - ey;
                    double dist = Math.sqrt(dx*dx+dy*dy);
                    
                    if(dist < br + er){
                        e.hit();
                        bullets.remove(i);
                        i--;
                        break;
                    }
                    
                    
                }
            
            }
            
            // check dead enemies
            
            for(int i = 0; i < enemies.size(); i++){
            
                if(enemies.get(i).isDead()){
                Enemy e = enemies.get(i);
                             
                //roll for powerup
                double rand = Math.random();
                if(rand<0.001){
                
                    powerups.add(new PowerUp(1, e.getx(), e.gety()));                                   
                }else if(rand < 0.020){powerups.add(new PowerUp(1, e.getx(), e.gety())); rand = 0;}
                else if(rand < 0.120){powerups.add(new PowerUp(2,e.getx(), e.gety())); rand = 0;}
                else if(rand < 0.130){powerups.add(new PowerUp(4,e.getx(), e.gety())); rand = 0;}
                      
                player.addScore(e.getType() + e.getRank());                
                enemies.remove(i);
                i--;
                
                e.explode();
                explosions.add(new Explosion(e.getx(),e.gety(), e.getr(), e.getr() + 30));
                
                }
                
            }
            
            //Explosion Update
            for(int i = 0; i < explosions.size(); i++){
            
                boolean remove = explosions.get(i).update();
                if(remove){
                
                    explosions.remove(i);
                    i--;
                
                }
            
            }
            
           
            // text update
            for(int i = 0; i < texts.size(); i++){
                
                boolean remove = texts.get(i).update();
                if(remove){
                
                    texts.remove(i);
                    i--;
                    
                }
            
            }
            //Check dead player
            if(player.isDead()){
            running = false;}
            
            //check player - enemy collision
            if(!player.isRecovering()){
            
                int px = player.getx();
                int py = player.gety();
                int pr = player.getplayerr();
                
                for(int i = 0; i < enemies.size(); i++){
                
                    Enemy e = enemies.get(i);
                    double ex = e.getx();
                    double ey = e.gety();
                    double er = e.getr();
                    
                    double dx = px - ex;
                    double dy = py - ey;
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    
                    if(dist < pr + er){
                    
                        player.loseLife();
                    
                    }
                
                }
             
            }
            //player-powerup collision
            int px1 = player.getx();
            int py1 = player.gety();
            int pr1 = player.getplayerr();
            
            for(int i = 0; i < powerups.size(); i++){
            
                PowerUp p = powerups.get(i);
                double x = p.getx();
                double y = p.gety();
                double r = p.getr();
                double dx = px1 - x;
                double dy = py1 - y;
                double dist = Math.sqrt(dx * dx + dy* dy);
                
                if(dist < pr1 + r) {
                    
                    int type = p.getType();
                    
                    switch(type){
                    
                        case 1:
                            player.gainLife();
                            texts.add(new Text(player.getx(),player.gety(), 2000, "Lives +1"));
                            break;
                            
                        case 2:
                            player.increasePower(2);
                            texts.add(new Text(player.getx(),player.gety(), 2000, "Power +2"));
                            break;
                        case 3:
                            player.increasePower(1);
                            texts.add(new Text(player.getx(),player.gety(), 2000, "Power +1"));
                            break;

                        case 4:
                            slowDownTimer = System.nanoTime();
                            
                            for(int j = 0; j < enemies.size();j++){
                            
                                enemies.get(j).setSlow(true);
                            }
                        texts.add(new Text(player.getx(),player.gety(), 2000, "Slow Time!"));
                        break;
                    }
                    
                    powerups.remove(i);
                    i--;
                    
                }
                
                //Slowdown update
                
            
            }
            
            if(slowDownTimer != 0){
                    slowDownTimerDiff = (System.nanoTime()  - slowDownTimer) / 1000000;
                    if(slowDownTimerDiff > slowDownLength){
                        slowDownTimer = 0;
                        for(int j = 0; j < enemies.size();j++){
                            
                                enemies.get(j).setSlow(false);
                            
                            }
                    }
                }
            
            
        }
        
        
        private void gameRender(){
            //Draw Background
            g.setColor(new Color(re,gr,bl));
            if(colorup){
                if(re<200){
                re += (int)(Math.random()*2+1);
                } else {colorup = false;}
                if(gr<200){
                gr += (int)(Math.random()*2+1);
                } else {colorup = false;}
                if(bl<200){
                bl += (int)(Math.random()*2+1);
                } else {colorup = false;}
            }
            else{
                if(re>15){
                re -= (int)(Math.random()*2+Math.random()*5);
                } else {colorup = true;}
                if(gr>15){
                gr -= (int)(Math.random()*2+Math.random()*5);
                } else {colorup = true;}
                if(bl>15){
                bl -= (int)(Math.random()*2+Math.random()*5);
                } else {colorup = true;}
            }
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            //FPS
            if(devmode){
            g.drawString("FPS: " +averageFPS,10, 15);
            
            //Bullets
            g.drawString("Number of Bullets: " +bullets.size(),10, 390);}
            
            //Draw player
            player.draw(g);
            
            //Draw Bullet
            for(int i = 0; i < bullets.size(); i++){
                bullets.get(i).draw(g);
                }
            for(int i = 0; i < bullets.size(); i++){
                bullets.get(i).draw(g);
                }
            //Draw Enemy
             for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).draw(g);
            }for(int i = 0; i < bullets.size(); i++){
                bullets.get(i).draw(g);
                }
             //Draw Powerups
             for(int i = 0; i < powerups.size(); i++){
                 powerups.get(i).draw(g);
             }
             
             //Draw explosions
             for(int i = 0; i < explosions.size(); i++ ){
             
                 explosions.get(i).draw(g);
             
             }
             
             //Draw Text
             for(int i = 0; i < texts.size(); i++){
                 texts.get(i).draw(g);
             }
            
            //Draw Wave Number
            if(waveStartTimer != 0){
                g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
                String s = " - W A V E  " + waveNumber + "  -";
                int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                int alpha = (int)(255*Math.sin(3.14 * startTimerDiff / waveDelay));
                if(alpha > 255) alpha = 255;
                g.setColor(new Color(255,255,255,alpha));
                g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
            }
            //Draw player lives
             for (int i = 0; i < player.getlives(); i++){
            g.setColor(Color.WHITE);
            g.fillOval(20+(20 * i), 20, player.getr()*2, player.getr()*2);
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.WHITE.darker());
            g.drawOval(20+(20 * i), 20, player.getr()*2, player.getr()*2);
            g.setStroke(new BasicStroke(1));
            
            //Draw player score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            g.drawString("Score: " + player.getScore(),WIDTH - 80, 20);
            // draw slowdown meter
             if(slowDownTimer != 0){
                 g.setColor(Color.WHITE);
                 g.drawRect(20,60,100,8);
                 g.fillRect(20, 60,
                         (int)(100 - 100.0 * slowDownTimerDiff / slowDownLength), 8);
        }
             // draw slowdown meter
             if(slowDownTimer != 0){
                 g.setColor(Color.WHITE);
                 g.drawRect(20,60,100,8);
                 g.fillRect(20, 60,
                         (int)(100 - 100.0 * slowDownTimerDiff / slowDownLength), 8);
             }
             //Draw Player Power
             g.setColor(Color.YELLOW);
             g.fillRect(20, 40, player.getPower()*8, 8);
             g.setColor(Color.YELLOW.darker());
             g.setStroke(new BasicStroke(2));
             for(int j = 0; j < player.getRequiredPower();j++){
             
                 g.drawRect(20+8*j, 40, 8, 8);
             
             }
             g.setStroke(new BasicStroke(1));
             
             
        }
        
        
    }
        
        private void gameDraw(){
            Graphics g2 = this.getGraphics();
            g2.drawImage(image, 0, 0, null);
            g2.dispose();
        }
        private void createNewEnemies() {
            Random rn = new Random(2);
            enemies.clear();
            Enemy e;
            
            if(waveNumber == 1){
            
                for(int i = 0; i < 2; i++) {
                
                    enemies.add(new Enemy(1, 1));
                    enemies.add(new Enemy(2, 1));
                
                }
            }
            
            if(waveNumber == 2){
            
                for(int i = 0; i < 4; i++) {
                
                    enemies.add(new Enemy(1, 2));
                    enemies.add(new Enemy(2, 1));
                    enemies.add(new Enemy(3, 1));
                    
                
                }
                enemies.add(new Enemy(1, 2));
                
            }
        if(waveNumber == 3){
            enemies.add(new Enemy(1, 4));
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 4; i++) {
                
                    enemies.add(new Enemy(1, 3));
                    
                    enemies.add(new Enemy(3, 1));
                }
            }
        if(waveNumber == 4){
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 4; i++) {
                
                    enemies.add(new Enemy(1, 3));
                    
                }
                enemies.add(new Enemy(2, 3));
                enemies.add(new Enemy(3, 2));
            }
        if(waveNumber == 5){
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 8; i++) {
                
                    enemies.add(new Enemy(1, 2));
                    enemies.add(new Enemy(2, 1));
                    enemies.add(new Enemy(3, 1));
                    
                }
                
            }
        if(waveNumber == 6){
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 8; i++) {
                
                    enemies.add(new Enemy(1, 2));
                    enemies.add(new Enemy(2, 1));
                    enemies.add(new Enemy(3, 1));
                    
                }
                enemies.add(new Enemy(1, 4));
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(1, 2));
                
            }
        if(waveNumber == 7){
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 8; i++) {
                
                    enemies.add(new Enemy(1, 2));
                    enemies.add(new Enemy(2, 2));
                    enemies.add(new Enemy(3, 3));
                    
                }
                enemies.add(new Enemy(1, 4));
                enemies.add(new Enemy(1, 4));
                enemies.add(new Enemy(1, 4));
                
            }
        if(waveNumber == 8){
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 9; i++) {
                
                    enemies.add(new Enemy(1, 2));
                    enemies.add(new Enemy(2, 1));
                    enemies.add(new Enemy(3, 1));
                    
                }
                enemies.add(new Enemy(1, 4));
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(1, 2));
                
            }
        if(waveNumber == 9){
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 10; i++) {
                
                    enemies.add(new Enemy(1, 2));
                    enemies.add(new Enemy(2, 1));
                    enemies.add(new Enemy(3, 1));
                    
                }
                enemies.add(new Enemy(1, 4));
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(1, 2));
                
            }
        if(waveNumber == 10){
            enemies.add(new Enemy(1, 4));
                for(int i = 0; i < 10; i++) {
                
                    enemies.add(new Enemy(1, 4));
                    enemies.add(new Enemy(2, 4));
                    enemies.add(new Enemy(3, 4));
                    
                }
                enemies.add(new Enemy(1, 4));
                enemies.add(new Enemy(1, 4));
                enemies.add(new Enemy(1, 4));
                
            }
        if(waveNumber == 11){
            running = false;
        }
        
        
        
        }

        
   
    public void keyTyped(KeyEvent e) {
    }

    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP | e.getKeyCode() == KeyEvent.VK_W){
			player.setUp(true);
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN |e.getKeyCode() == KeyEvent.VK_S){
			player.setDn(true);
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT|e.getKeyCode() == KeyEvent.VK_A){
			player.setLt(true);
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT |e.getKeyCode() == KeyEvent.VK_D){
			player.setRt(true);
		}
                
         if(e.getKeyCode() == KeyEvent.VK_L){
         player.devmode = true;
         } 
        if(player.getRecovering()==false){if(e.getKeyCode() == KeyEvent.VK_Z|e.getKeyCode() == KeyEvent.VK_SPACE ){
        player.setFiring(true);}}
        
        
    }

   
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP | e.getKeyCode() == KeyEvent.VK_W){
			player.setUp(false);
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN |e.getKeyCode() == KeyEvent.VK_S){
			player.setDn(false);
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT|e.getKeyCode() == KeyEvent.VK_A){
			player.setLt(false);
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT |e.getKeyCode() == KeyEvent.VK_D){
			player.setRt(false);
		}
                if(e.getKeyCode() == KeyEvent.VK_L){
         player.devmode = false;
         } 
        if(e.getKeyCode() == KeyEvent.VK_Z|e.getKeyCode() == KeyEvent.VK_SPACE){
        player.setFiring(false);}
    }
    
    String message = "";
    String replacement = "";

    private void saveHighScores() {
        try {
            Path path = Paths.get("res/Bubble Trouble High Scores.txt");
            Charset charset = StandardCharsets.UTF_8;
            
            String content = new String(Files.readAllBytes(path), charset);
            content = content.replaceAll(content, replacement += JOptionPane.showInputDialog("Enter a your name") + "#" + player.getScore());
            Files.write(path, content.getBytes(charset));
            
            showHighScores();
            
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showHighScores() {
        try {
            Scanner s = new Scanner(new File("res/Bubble Trouble High Scores.txt"));
              
            while(s.hasNext()){
            
                String line = s.nextLine();
                              
                Scanner r = new Scanner(line).useDelimiter("#");
                String name = r.next();
                int score = r.nextInt();
                message += name + "\t : \t" + score + "\n";
                replacement += name + "#" + score + "#";
            }
            JOptionPane.showMessageDialog(null, "Highscores:\n Name: \t : \t Score:\n"  + message );
            Game.window.dispose();
            if(JOptionPane.showInputDialog("Continue? Y/N").charAt(0) == 'y' ||JOptionPane.showInputDialog("Continue? Y/N").charAt(0) == 'Y'){
            Game.start();} else {}
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getHighScores(){
    
         try {
            Scanner s = new Scanner(new File("res/Bubble Trouble High Scores.txt"));
              
            while(s.hasNext()){
            
                String line = s.nextLine();
                              
                Scanner r = new Scanner(line).useDelimiter("#");
                String name = r.next();
                int score = r.nextInt();
                message += name + "\t : \t" + score + "\n";
                replacement += name + "#" + score + "\n";
                
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

   
    public void mouseClicked(MouseEvent e) {
        
        
        
    }

  
    public void mousePressed(MouseEvent e) {
        
       if(e.getButton() == MouseEvent.BUTTON1){
        player.setFiring(true);}
        
    }

   
    public void mouseReleased(MouseEvent e) {
        
        if(e.getButton() == MouseEvent.BUTTON1){
        player.setFiring(false);}
        
    }


    
    public void mouseEntered(MouseEvent e) {
        
        
        
    }

   
    public void mouseExited(MouseEvent e) {
       
        
        
    }
    
        
      
    }

        
    
    
    

