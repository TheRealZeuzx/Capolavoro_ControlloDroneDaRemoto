package it.davincifascetti.controllosocketudp.videoTestingWorkspace;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import java.awt.image.BufferedImage;


public class Test {
    public String file=null;

    public Test(String fileName){
        this.file = fileName;
    };
    
    
    public static void main(String[] args) {
        new Test("video.mp4").test2();
    }
       

    public void test1(){
        String filePath = this.file;
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath);

        try {
            grabber.start();

            JFrame frame = new JFrame("Video Frame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            JLabel label = new JLabel();
            frame.add(label);
            frame.setVisible(true);

            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            Java2DFrameConverter converter2D = new Java2DFrameConverter();

            // Legge e visualizza i frame uno per uno
            org.bytedeco.javacv.Frame grabbedFrame;
            int frameCount = 0;
            while ((grabbedFrame = grabber.grab()) != null) {
                frameCount++;
                System.out.println("Frame " + frameCount + " grabbed.");
                // Converti il frame in un BufferedImage
                BufferedImage bufferedImage = converter2D.convert(grabbedFrame);

                // Mostra l'immagine nel JLabel
                if (bufferedImage != null) {
                    label.setIcon(new ImageIcon(bufferedImage));
                    frame.pack(); // Ridimensiona il frame per adattarsi all'immagine
                    System.out.println("Immagine impostata nel JLabel");
                } else {
                    System.out.println("Nessuna immagine disponibile");
                }

                // Aggiorna il frame per renderlo visibile
                frame.repaint();

                // // Aggiungi un ritardo per visualizzare i frame al ritmo corretto (opzionale)
                // long sleepTime = (long) (100 / grabber.getFrameRate());
                // Thread.sleep(sleepTime);
            }
            // Rilascia le risorse
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void test2(){
        String filePath = this.file;
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath);

        try {
            grabber.start();

            JFrame frame = new JFrame("Video Frame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            JLabel label = new JLabel();
            frame.add(label);
            frame.setVisible(true);

            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            Java2DFrameConverter converter2D = new Java2DFrameConverter();

            // Legge e visualizza i frame uno per uno
            org.bytedeco.javacv.Frame grabbedFrame;
            int frameCount = 0;
            double frameRate = grabber.getFrameRate();
            long delay = (long) (800 / frameRate);

            while ((grabbedFrame = grabber.grab()) != null) {
                frameCount++;
                System.out.println("Frame " + frameCount + " grabbed at timestamp: " + grabbedFrame.timestamp);

                // Controlla se il frame è valido
                if (grabbedFrame == null || grabbedFrame.image == null) {
                    System.out.println("Frame " + frameCount + " is null.");
                    continue;
                }

                // Converti il frame in un BufferedImage
                BufferedImage bufferedImage = converter2D.convert(grabbedFrame);

                // Mostra l'immagine nel JLabel
                if (bufferedImage != null) {
                    label.setIcon(new ImageIcon(bufferedImage));
                    frame.pack(); // Ridimensiona il frame per adattarsi all'immagine
                    System.out.println("Immagine impostata nel JLabel per frame " + frameCount);
                } else {
                    System.out.println("Nessuna immagine disponibile per frame " + frameCount);
                }

                // Aggiorna il frame per renderlo visibile
                frame.repaint();

                // Aggiungi un ritardo per visualizzare i frame al ritmo corretto
                Thread.sleep(delay);
            }

            // Rilascia le risorse
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
