/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BulletRun.main.main;

import static com.BulletRun.main.main.GamePanel.HEIGHT;
import static com.BulletRun.main.main.GamePanel.WIDTH;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFrame;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Mug3r
 */
public class Game {
    private static boolean hasBeenRun = false;
    private static boolean music = false;
    static JFrame window = new JFrame("Bubble Trubble");
    
    public static void main(String[] args){
        
        
        JFrame main = new GameMenu();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(false);
        
        
        
        main.pack();
        main.setVisible(true);
        
        playSound();
        music = true;
        
        
        
    
    }
    public static void start(){
        if(!hasBeenRun){
        window.setContentPane(new GamePanel());  
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension(WIDTH,HEIGHT + 30));  
        window.setResizable(false);
        window.setVisible(true);
        hasBeenRun = true;}
        else{
        window.setContentPane(new GamePanel());  
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension(WIDTH,HEIGHT + 30));  
        window.setResizable(false);
        window.setVisible(true);
        }
        
        
    
    }
    
    public static void playSound() {
       
       

                try {
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res\\Music1.wav").getAbsoluteFile());
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.loop(100);
                        
                     } catch(Exception ex) {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                        }
    }     
    
    
    
    
    
}
