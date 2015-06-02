/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.Projectiles;
import com.BulletRun.main.main.GamePanel;
import java.awt.*;
/**
 *
 * @author Mug3r
 */
public class Bullet {
    //Fields
    private double x;
    private double y;
    private int r;
        
    private double dx;
    private double dy;
    private double rad;
    private double speed;
    
    private Color color1;
    
    public Bullet(double angle, int x, int y){
    
        this.x = x;
        this.y = y;
        r = 3;
        speed = 10;
        rad = Math.toRadians(angle);
        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;
        
        
        
        color1 = Color.WHITE;
    
    }
    
    public double getx(){return x;}
    public double gety(){return y;}
    public double getr(){return r;}

    
    public boolean update(){
        
        x += dx;
        y += dy;
        
        if(x < -r || x > GamePanel.WIDTH + r ||
                y < -r || y > GamePanel.HEIGHT + r){
            return true;
        }
        
        return false;
    
    }
    public void draw(Graphics2D g){
        
        g.drawOval((int)(x - r),(int)(y - r),2 * r,2 * r);
        g.setColor(new Color(255,255,255, 100));
        g.fillOval((int)(x - r),(int)(y - r),2 * r,2 * r);
        g.setColor(new Color(255,255,255, 50));
        
        g.setStroke(new BasicStroke(1));
        
        
    
    }
    
}
