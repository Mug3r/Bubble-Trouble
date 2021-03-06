/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.Entities;
import com.BulletRun.main.main.GamePanel;
import static com.BulletRun.main.main.GamePanel.player;
import java.awt.*;
/**
 *
 * @author Mug3r
 */
public class Enemy {
    
    //FIELDS
    
    private double x;
    private double y;
    private double r;
    
    private double dx;
    private double dy;
    private double rad;
    private double speed;
    
    private int health;
    private int type;
    private int rank;
    
    private Color color1;
    
    private boolean ready;
    private boolean dead;
    
    private boolean hit;
    private long hitTimer;
    
    private boolean slow;
            
    //CONSTRUCTOR
    public Enemy(int type, int rank){
        this.type = type;
        this.rank = rank;
        //default enemy = "Trojan File"
        if(type == 1){
        color1 = Color.RED;
        color1 = new Color(255, 0, 0, 128);
            if(rank == 1){
                health = 1;
                speed = 1;
                r = 5;
            } else if(rank == 2){
                health = 2;
                speed = 2;
                r = 10;
            }else if(rank == 3){
                health = 2;
                speed = 4;
                r = 15;}
            else if(rank == 4){
                health = 2;
                speed = 5;
                r = 20;}
        }
        // stronger, faster Default = "Spyware"
        if(type == 2){
            //color1 = Color.ORANGE;
            color1 = new Color(255, 96, 28, 128);
                if(rank == 1){
                    speed = 3;
                    r = 4;
                    health = 2;
                }
                if (rank == 2){
                    speed = 4;
                    r = 5;
                    health = 3;
                }
                if(rank == 3){
                    speed = 5;
                    r = 4;
                    health = 2;
                }
                if(rank == 4){
                    speed = 7;
                    r = 4;
                    health = 4;
                }
        }
        // Slow but Hard To kill = "Malware"
        if(type == 3){
        //color1 = Color.BLUE;
        color1 = new Color(0, 0, 255, 128);
            if(rank == 1){
                speed = 1.5;
                r = 5;
                health = 5;
            
            }
            if(rank == 2){
                speed = 3;
                r = 15;
                health = 10;
            }
            if(rank == 3){
                speed = 3.5;
                r = 20;
                health = 18;
            }
            if(rank == 4){
                speed = 4;
                r = 25;
                health = 30;
            }
        }
        
        
        x = Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4;
        y = -r;
        
        double angle = Math.random()*140+20;
        rad = Math.toRadians(angle);
        
        dx = Math.cos(rad) * speed;
        dy  =Math.sin(rad) * speed;
        
        ready = false;
        dead = false;
         
        hit = false; 
        hitTimer = 0;
    
    
    }
    
    //FUNCTIONS
    public double getx(){return x;}
    public double gety(){return y;}
    public int getr(){return (int)r;}
    
    public int getType(){return type;}
    public int getRank(){return rank;}
    
    public void setSlow(boolean b){slow = b;}
    
    public boolean isDead(){return dead;}
    
        
    public void hit(){
    health-=player.damage;
    hitTimer = System.nanoTime();
    hit = true;
    if(health <= 0){dead = true;}
    }
    private int amount;
    
    public void explode(){
        if(rank > 1){
            int amount = 0;
            if(type == 1){
                amount = 3;}
            if(type == 2){
                amount = 2;}
            if(type == 3){
                amount = 4;}
            if(type == 4){
                amount = 3;}
            
            for(int i = 0; i < amount; i++){
            
                Enemy e = new Enemy(getType(), getRank()-1);
                e.setSlow(slow);
                e.x = this.x;
                e.y = this.y;
                double angle = 0;
                if(!ready){
                    angle = Math.random()*140 +20;
                }
                else{
                angle = Math.random()*360;
                }
                e.rad = Math.toRadians(angle);
                GamePanel.enemies.add(e);
            }
                
                
                
                
                
            
            
        }
    }
    
    public void update(){
    if(slow){
        x += dx * 0.3;
        y += dy * 0.3;
    }
    else{
    x += dx;
    y += dy;
    }
    
    if(!ready){
        if(x > r && x < GamePanel.WIDTH - r &&
                y > r && y< GamePanel.HEIGHT){
        ready = true;
        }
    }
    
    if(x < r && dx <0) dx = -dx;
    if(y < r && dy < 0) dy =-dy;
    if(x > GamePanel.WIDTH - r && dx > 0) dx =-dx;
    if(y > GamePanel.HEIGHT - r && dy > 0) dy =-dy;
    
    if(hit){
    long elapsed = System.nanoTime() - hitTimer / 100000;
        if(elapsed > 50){
        hit = false;
        hitTimer = 0;
        }}
    
    }
    
    public void draw(Graphics2D g){
        if(hit){
            g.setColor(Color.WHITE);
        
        g.fillOval((int)(x - r),(int)(y - r), (int)r*2,(int)r*2);
        
        
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.WHITE.darker());
        g.drawOval((int)(x - r),(int)(y - r), (int)r*2, (int)r*2);
        g.setStroke(new BasicStroke(1));}
        
    else {
        g.setColor(color1);
        
        g.fillOval((int)(x - r),(int)(y - r), (int)r*2,(int)r*2);
        
        
        g.setStroke(new BasicStroke(3));
        g.setColor(color1.darker());
        g.drawOval((int)(x - r),(int)(y - r), (int)r*2, (int)r*2);
        g.setStroke(new BasicStroke(1));}
    
    }
    
}
