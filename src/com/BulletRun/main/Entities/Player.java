/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.Entities;
import com.BulletRun.main.Projectiles.Bullet;
import com.BulletRun.main.main.GamePanel;
import java.awt.*;

/**
 *
 * @author Mug3r
 */
public class Player {
    //FIELDS
    private int x;
    private int y;
    private int r;
    private int playerr;
    private int firerate = 0;
    
    private int dx;
    private int dy;
    private int speed;
    
    private boolean lt;
    private boolean rt;
    private boolean dn;
    private boolean up;
    
    
    public boolean devmode = false;
    
    private boolean firing;
    private long firingTimer;
    private long firingDelay;
    
    private boolean recovering;
    long recoveryTimer;
    
    private int lives;
    
    private Color color1;
    private Color color2;
    
    private int score;
    
    private int powerLevel;
    private int power;
    private int[] requiredPower = {
    1, 2, 3 ,4, 5, 6, 7, 8, 9, 10, 11
    };
    private boolean dead;
    
    public Player() { 
        
         x = GamePanel.WIDTH / 2;
         y = GamePanel.HEIGHT / 2 + 40;
         r = 5;
         playerr = 6;
         
         dx = 0;
         dy = 0;
         speed = 5;
                         
         lives = 3;
         dead = false;
         
         color1 = Color.GRAY;
         color2 = Color.BLUE;
         
         firing = false;
         firingTimer = System.nanoTime();
         firingDelay = 200;
         
         recovering = false;
         recoveryTimer = 0;
         
         score = 0;
        
    }
    
    //FUNCTIONS
    
    public void setLt (boolean b) { lt = b; }
    public void setRt (boolean b) { rt = b; }
    public void setUp (boolean b) { up = b; }
    public void setDn (boolean b) { dn = b; }
    
    public int getx(){return x;}
    public int gety(){return y;}
    public int getr(){return r;}
    
    public int getScore(){return score;}
    
    public int getlives(){return lives;}
    
    public boolean isRecovering(){return recovering;}
    
    
    public void setFiring(boolean b) {firing = b;}
    
    public void addScore(int i){score +=i;}
    
    public void gainLife(){
    lives++;
    }
    
    public void loseLife(){
    if(!devmode){
        lives--;
        recovering = true;
        recoveryTimer =System.nanoTime();
        if(lives<1){
            dead = true;
        }}
    else lives = 1;
    }
    double threshhold = 0;
    public void increasePower(int i){
    power += i;
    if(powerLevel == 5){
        if(power > requiredPower[powerLevel]){
        power = requiredPower[powerLevel];
        threshhold+=0.50;
        if(threshhold >= 1){
        playerr++;
        threshhold = 0;}}
        return;
    }
    if(power>=requiredPower[powerLevel]){
        power -= requiredPower[powerLevel];
        powerLevel++;
        playerr = playerr+2;
    }
    
   }
    
    public int getPowerLevel() {return powerLevel;}
    public int getPower() {return power;}
    public int getRequiredPower() {return requiredPower[powerLevel];}
    public boolean isDead(){return dead;}    
    
    public void update(){
        
       /* if(lt) {
            dx -= speed;
        } else if(rt){
            dx += speed;
        }
        
        if(up) {
            dy -= speed;
        } else if(dn){
            dy += speed;
        }
        
        x += dx;
        y += dy;
        */
        
        Point p = MouseInfo.getPointerInfo().getLocation();
        x = p.x - 5;
        y = p.y - 25;
        if(x < r)x = r;
        if(y < r)y = r;
        if(x > GamePanel.WIDTH - r) x = GamePanel.WIDTH - r;
        if(y > GamePanel.HEIGHT - r) y = GamePanel.HEIGHT - r;
        
        dx = 0;
        dy = 0;
        
        if(firing){
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            
            if(power > requiredPower[powerLevel]){firerate += powerLevel*30+3;}
            if(elapsed + (firerate*3) > firingDelay){
                
            firingTimer = System.nanoTime();
            
            if(powerLevel < 2){
            GamePanel.bullets.add(new Bullet(270, x, y));
            }
                else if(powerLevel < 4 ){
                    GamePanel.bullets.add(new Bullet(270, x + 5, y));
                    GamePanel.bullets.add(new Bullet(270, x - 5, y));}
                    else if (powerLevel < 5 ){
                        GamePanel.bullets.add(new Bullet(270, x, y));
                        GamePanel.bullets.add(new Bullet(275, x + 5, y));
                        GamePanel.bullets.add(new Bullet(265, x - 5, y));
            }               else if (powerLevel < 10 ){
                                GamePanel.bullets.add(new Bullet(270, x, y));
                                GamePanel.bullets.add(new Bullet(275, x + 5, y));
                                GamePanel.bullets.add(new Bullet(265, x - 5, y));
                                GamePanel.bullets.add(new Bullet(260, x + 10, y));
                                GamePanel.bullets.add(new Bullet(280, x - 10, y));
            }else if (powerLevel < 15 ){
                                GamePanel.bullets.add(new Bullet(270, x, y));
                                GamePanel.bullets.add(new Bullet(275, x + 5, y));
                                GamePanel.bullets.add(new Bullet(265, x - 5, y));
                                GamePanel.bullets.add(new Bullet(260, x + 10, y));
                                GamePanel.bullets.add(new Bullet(280, x - 10, y));
                                GamePanel.bullets.add(new Bullet(285, x + 20, y));
                                GamePanel.bullets.add(new Bullet(295, x - 20, y));
            }
            
            }
        }
        if(recovering){
            long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
                if(elapsed > 2000) {
                    recovering = false;
                    recoveryTimer = 0;
        }}
    
    }
    public void draw(Graphics2D g){
        if(recovering){
            g.setColor(color2);
            g.fillOval(x - playerr, y - playerr, 2 * playerr, 2 * playerr);
        
            g.setStroke(new BasicStroke(3));
            g.setColor(color2.darker());
            g.drawOval(x - playerr, y - playerr, 2 * playerr, 2 * playerr);
            g.setStroke(new BasicStroke(1));}
            else{
                g.setColor(color1);
                g.fillOval(x - playerr, y - playerr, 2 * playerr, 2 * playerr);
        
                g.setStroke(new BasicStroke(3));
                g.setColor(color1.brighter());
                g.drawOval(x - playerr, y - playerr, 2 * playerr, 2 * playerr);
                g.setStroke(new BasicStroke(1));
            }
    
    
    
    }

    public boolean getRecovering() {
        return recovering;
    }

    public int getplayerr() {
        return playerr;
    }

}
