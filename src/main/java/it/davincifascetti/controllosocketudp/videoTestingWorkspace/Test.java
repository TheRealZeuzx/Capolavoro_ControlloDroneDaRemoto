package it.davincifascetti.controllosocketudp.videoTestingWorkspace;


import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;
import java.awt.image.BufferedImage;
import org.bytedeco.ffmpeg.global.avutil;


public class Test {
    public File file=null;

    public Test(String fileName){
        this.file = new File(fileName);
    };
    
    
    public static void main(String[] args) {
        String filePath = "video.mp4";
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
            while ((grabbedFrame = grabber.grab()) != null) {
                // Converti il frame in un IplImage
                IplImage iplImage = converter.convertToIplImage(grabbedFrame);

                // Converti IplImage a Frame
                org.bytedeco.javacv.Frame frameConverted = converter.convert(iplImage);

                // Converti Frame a BufferedImage
                BufferedImage bufferedImage = converter2D.convert(frameConverted);

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
                // Aggiungi un ritardo per visualizzare i frame al ritmo corretto (opzionale)
                long sleepTime = (long) (1000 / grabber.getFrameRate());
                Thread.sleep(sleepTime);
            }

            // Rilascia le risorse
            frame.dispose();
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}