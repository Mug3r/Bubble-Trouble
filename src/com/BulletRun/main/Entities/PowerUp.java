/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.Entities;
import com.BulletRun.main.main.GamePanel;
import java.awt.*;
/**
 *
 * @author Mug3r
 */
public class PowerUp {
    
    private double x;
    private double y;
    private int r;
    
    private int type;
    
    private Color color1;
    
    //Constructor
    public PowerUp(int type, double x, double y){
    
        this.type = type;
        this.x = x;
        this.y = y;
        
        if(type == 1){
        
            color1 = Color.PINK;
            r = 3;
        
        }
        
        if (type == 2) {
        
            color1 = Color.YELLOW;
            r = 3;}
        
        if(type == 3){
            color1 = Color.YELLOW;
            r = 5;}
        
        if(type == 4){
            color1 = Color.WHITE;
            r = 4;
        }
        }  
        
        
        
    
        
    
    
    //METHODS
    
    public double getx(){return x;}
    public double gety(){return y;}
    public double getr(){return r;}
    // power up types: 1 = +1 life
    // 2 = +1 power
    // 3 = +2 power
    // 4 = slow down time
    public int getType(){return type;}
    
    public boolean update(){
    
        y += 2;
        
        if(y > GamePanel.HEIGHT + r){
            return true;}
        
        return false;
    
    }
    public void draw(Graphics2D g){
        
        g.setColor(color1);
        g.fillRect((int) (x - r), (int)(y -r), (int)r*2,(int)r*2);
        
        g.setStroke(new BasicStroke(3));
        g.setColor(color1.darker());
        g.drawRect((int) (x - r), (int)(y -r), (int)r*2,(int)r*2);
        g.setStroke(new BasicStroke(1));
    
    }
}
