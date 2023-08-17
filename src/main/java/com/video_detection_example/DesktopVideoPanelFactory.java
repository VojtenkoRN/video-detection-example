package com.video_detection_example;

import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@Singleton
public class DesktopVideoPanelFactory {

    private static final int WIDTH = 3200;
    private static final int HEIGHT = 1800;

    public JLabel generate(String videoPath) {
        JFrame jframe = new JFrame(videoPath);
        JLabel videoPanel = new JLabel();
        jframe.setContentPane(videoPanel);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setUndecorated(false);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return videoPanel;
    }

}
