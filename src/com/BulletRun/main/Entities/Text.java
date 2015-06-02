/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.Entities;
import java.awt.*;
/**
 *
 * @author Mug3r
 */
public class Text {
    
    //FILEDS
    private double x;
    private double y;
    private double r;
    private String s;
    
    private long start;
    private long time;
    
    //CONSTRUCTOR
    public Text(double x, double y, long time, String s){
    
        this.x = x;
        this.y = y;
        this.time = time;
        this.s = s;
        
        start = System.nanoTime();
    
    }
    
    public boolean update(){

        long elapsed = (System.nanoTime() - start)/1000000;
        if(elapsed > time){
        return true;
        }
        return false;
    
}
    
    public void draw(Graphics2D g){
    
        g.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        long elapsed = (System.nanoTime() - start)/1000000;
        int alpha= (int)(255*Math.sin(3.14 * elapsed / time));
        if(alpha > 255){alpha = 255;}
        if(alpha < 0){alpha = 0;}
        g.setColor(new Color(255,255,255,alpha));
        
        int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        g.drawString(s, (int) (x - length /2), (int) y);
    
    }
    
}
