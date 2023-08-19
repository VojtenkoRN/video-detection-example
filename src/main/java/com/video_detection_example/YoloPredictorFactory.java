package com.video_detection_example;

import ai.djl.Application;
import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.translator.YoloV5TranslatorFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
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

    private static final String IMAGE_SIZE = "640";
    private static final String THRESHOLD = "0.5";
    @ConfigProperty(name = "model.path")
    String modelPath;
    @ConfigProperty(name = "model.name")
    String modelName;
    @ConfigProperty(name = "env.debug")
    boolean debug;
    private ZooModel<Image, DetectedObjects> MODEL;

    @PostConstruct
    private void initModel() throws ModelNotFoundException, MalformedModelException, IOException {
        if (debug) {
            Engine.debugEnvironment();
        }

        Criteria<Image, DetectedObjects> criteria = Criteria.builder()
              .setTypes(Image.class, DetectedObjects.class)
              .optModelUrls(modelPath)
              .optModelName(modelName)
              .optDevice(Device.gpu())
              .optApplication(Application.CV.OBJECT_DETECTION)
              .optEngine(Engine.getDefaultEngineName())
              .optArgument("width", IMAGE_SIZE)
              .optArgument("height", IMAGE_SIZE)
              .optArgument("resize", "true")
              .optArgument("rescale", "true")
              .optArgument("optApplyRatio", "true")
              .optArgument("threshold", THRESHOLD)
              .optArgument("synset", Synset.asString())
              .optTranslatorFactory(new YoloV5TranslatorFactory())
              .optProgress(new ProgressBar())
              .build();

        MODEL = ModelZoo.loadModel(criteria);
    }

    public Predictor<Image, DetectedObjects> generate() {
        return MODEL.newPredictor();
    }

}
