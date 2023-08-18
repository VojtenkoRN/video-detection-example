package com.video_detection_example;

import ai.djl.MalformedModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.opencv.core.Mat;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Startup
@Singleton
public class VideoFileDetectionService {

    private static final Set<String> FILTER = Set.of(
          "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat",
          "traffic light", "fire hydrant", "stop sign", "parking meter"
    );

    @Inject
    OpenCv openCv;
    @Inject
    YoloPredictorFactory yoloPredictorFactory;
    @Inject
    DesktopVideoPanelFactory videoPanelFactory;
    @ConfigProperty(name = "video.path")
    String videoPath;

    @PostConstruct
    public void detect() throws TranslateException, IOException, ModelNotFoundException, MalformedModelException {
        try (var model = yoloPredictorFactory.loadModel()) {
            try (var predictor = model.newPredictor()) {
                var camera = openCv.capture(videoPath);
                var videoPanel = videoPanelFactory.generate(videoPath);

                Mat frame = new Mat();
                while (camera.read(frame)) {
                    if (!frame.empty()) {
                        Image image = ImageFactory.getInstance().fromImage(frame);
                        DetectedObjects predict = predictor.predict(image);
                        List<DetectedObjects.DetectedObject> allItems = predict.items();
                        var filteredItems = allItems.stream()
                              .filter(detected -> FILTER.contains(detected.getClassName()))
                              .filter(detected -> detected.getProbability() > 0.8d)
                              .toList();

                        image.drawBoundingBoxes(convert(filteredItems));

                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        image.save(output, "jpg");
                        byte[] data = output.toByteArray();
                        ByteArrayInputStream input = new ByteArrayInputStream(data);
                        BufferedImage img = ImageIO.read(input);
                        ImageIcon vidpanelImage = new ImageIcon(img);
                        videoPanel.setIcon(vidpanelImage);
                        videoPanel.repaint();
                    }
                }
            }
        }
    }

    private DetectedObjects convert(List<DetectedObjects.DetectedObject> items) {
        var classNames = items.stream().map(Classifications.Classification::getClassName).toList();
        var probabilities = items.stream().map(Classifications.Classification::getProbability).toList();
        var boundingBoxes = items.stream().map(DetectedObjects.DetectedObject::getBoundingBox).toList();

        return new DetectedObjects(classNames, probabilities, boundingBoxes);
    }

}
