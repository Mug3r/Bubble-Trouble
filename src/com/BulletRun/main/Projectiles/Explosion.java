/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.Projectiles;
import java.awt.*;
/**
 *
 * @author Mug3r
 */
public class Explosion {
    //FIELDS
    private double x;
    private double y;
    private int r;
    private int maxRadius;
    
    //CONSTRUCTOR
    public Explosion(double x, double y, int r, int max){
    
        this.x = x;
        this.y = y;
        this.r = r;
        maxRadius = max;
    
    }
    
    public boolean update(){
    
        r+=2;
        if(r>=maxRadius){
            return true;
        }
        return false;
    }
    
    public void draw(Graphics2D g){
    
        g.setColor(new Color(255,255,255,128));
        g.setStroke(new BasicStroke(3));
        g.drawOval((int)(x-r),(int)(y-r), (int)r*2,(int)r*2);
        g.setStroke(new BasicStroke(1));
    
    }

}
