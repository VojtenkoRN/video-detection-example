package com.video_detection_example;

import ai.djl.MalformedModelException;
import ai.djl.engine.Engine;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.translator.YoloV5TranslatorFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.IOException;

@Startup
@Singleton
public class YoloPredictorFactory {

    @ConfigProperty(name = "model.path")
    String modelPath;
    @ConfigProperty(name = "model.name")
    String modelName;
    @ConfigProperty(name = "env.debug")
    boolean debug;
    private Criteria<Image, DetectedObjects> criteria;

    @PostConstruct
    private void initModel() {
        if (debug) {
            Engine.debugEnvironment();
        }

        criteria = Criteria.builder()
              .setTypes(Image.class, DetectedObjects.class)
              .optModelUrls(modelPath)
              .optModelName(modelName)
              .optEngine("OnnxRuntime")
              .optTranslatorFactory(new YoloV5TranslatorFactory())
              .optProgress(new ProgressBar())
              .optArgument("optApplyRatio", true)
              .optArgument("rescale", true)
              .build();
    }

    public ZooModel<Image, DetectedObjects> loadModel() throws ModelNotFoundException, MalformedModelException, IOException {
        return criteria.loadModel();
    }

}
