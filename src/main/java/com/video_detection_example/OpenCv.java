package com.video_detection_example;

import io.quarkus.runtime.Startup;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.inject.Singleton;

@Startup
@Singleton
public class OpenCv {

    public VideoCapture capture(String path) {
        VideoCapture camera = new VideoCapture(path, Videoio.CAP_FFMPEG);
        if (!camera.isOpened()) {
            throw new RuntimeException("Can't open " + path);
        }

        var fps = camera.get(Videoio.CAP_PROP_FPS);
        var length = camera.get(Videoio.CAP_PROP_FRAME_COUNT);
        var width = camera.get(Videoio.CAP_PROP_FRAME_WIDTH);
        var height = camera.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        System.out.println(
              "FPS: " + fps +
                    "\nlength: " + length +
                    "\nwidth: " + width +
                    "\nheight: " + height
        );

        return camera;
    }

}
