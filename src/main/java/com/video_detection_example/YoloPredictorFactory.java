package com.video_detection_example;

import ai.djl.Application;
import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.YoloV5Translator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.Translator;
import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.IOException;

@Startup
@Singleton
public class YoloPredictorFactory {

    private static final int IMAGE_SIZE = 640;
    private static final float THRESHOLD = 0.5f;
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

        Pipeline pipeline = new Pipeline()
              .add(new Resize(IMAGE_SIZE))
              .add(new ToTensor());

        Translator<Image, DetectedObjects> translator = YoloV5Translator
              .builder()
              .setPipeline(pipeline)
              .optSynset(Synset.asNameList())
              .optThreshold(THRESHOLD)
              .optOutputType(YoloV5Translator.YoloOutputType.AUTO)
              .build();

        Criteria<Image, DetectedObjects> criteria = Criteria.builder()
              .setTypes(Image.class, DetectedObjects.class)
              .optModelUrls(modelPath)
              .optModelName(modelName)
              .optDevice(Device.gpu())
              .optDevice(Device.cpu())
              .optApplication(Application.CV.OBJECT_DETECTION)
              .optTranslator(translator)
              .optEngine(Engine.getDefaultEngineName())
              .optProgress(new ProgressBar())
              .optArgument("optApplyRatio", true)
              .optArgument("rescale", true)
              .build();

        MODEL = ModelZoo.loadModel(criteria);
    }

    public Predictor<Image, DetectedObjects> generate() {
        return MODEL.newPredictor();
    }

}
