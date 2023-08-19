package com.video_detection_example;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * All ms coco classnames
 * <a href="https://gist.github.com/AruniRC/7b3dadd004da04c80198557db5da4bda#file-ms_coco_classnames-txt-L4">
 * from this
 * </a>
 */
public final class Synset {

    private static final Set<SynsetClass> SYNSET = Set.of(
          new SynsetClass(0, "__background__"),
          new SynsetClass(1, "person"),
          new SynsetClass(2, "bicycle"),
          new SynsetClass(3, "car"),
          new SynsetClass(4, "motorcycle"),
          new SynsetClass(5, "airplane"),
          new SynsetClass(6, "bus"),
          new SynsetClass(7, "train"),
          new SynsetClass(8, "truck"),
          new SynsetClass(9, "boat"),
          new SynsetClass(10, "traffic light"),
          new SynsetClass(11, "fire hydrant"),
          new SynsetClass(12, "stop sign"),
          new SynsetClass(13, "parking meter"),
          new SynsetClass(14, "bench"),
          new SynsetClass(15, "bird"),
          new SynsetClass(16, "cat"),
          new SynsetClass(17, "dog"),
          new SynsetClass(18, "horse"),
          new SynsetClass(19, "sheep"),
          new SynsetClass(20, "cow"),
          new SynsetClass(21, "elephant"),
          new SynsetClass(22, "bear"),
          new SynsetClass(23, "zebra"),
          new SynsetClass(24, "giraffe"),
          new SynsetClass(25, "backpack"),
          new SynsetClass(26, "umbrella"),
          new SynsetClass(27, "handbag"),
          new SynsetClass(28, "tie"),
          new SynsetClass(29, "suitcase"),
          new SynsetClass(30, "frisbee"),
          new SynsetClass(31, "skis"),
          new SynsetClass(32, "snowboard"),
          new SynsetClass(33, "sports ball"),
          new SynsetClass(34, "kite"),
          new SynsetClass(35, "baseball bat"),
          new SynsetClass(36, "baseball glove"),
          new SynsetClass(37, "skateboard"),
          new SynsetClass(38, "surfboard"),
          new SynsetClass(39, "tennis racket"),
          new SynsetClass(40, "bottle"),
          new SynsetClass(41, "wine glass"),
          new SynsetClass(42, "cup"),
          new SynsetClass(43, "fork"),
          new SynsetClass(44, "knife"),
          new SynsetClass(45, "spoon"),
          new SynsetClass(46, "bowl"),
          new SynsetClass(47, "banana"),
          new SynsetClass(48, "apple"),
          new SynsetClass(49, "sandwich"),
          new SynsetClass(50, "orange"),
          new SynsetClass(51, "broccoli"),
          new SynsetClass(52, "carrot"),
          new SynsetClass(53, "hot dog"),
          new SynsetClass(54, "pizza"),
          new SynsetClass(55, "donut"),
          new SynsetClass(56, "cake"),
          new SynsetClass(57, "chair"),
          new SynsetClass(58, "couch"),
          new SynsetClass(59, "potted plant"),
          new SynsetClass(60, "bed"),
          new SynsetClass(61, "dining table"),
          new SynsetClass(62, "toilet"),
          new SynsetClass(63, "tv"),
          new SynsetClass(64, "laptop"),
          new SynsetClass(65, "mouse"),
          new SynsetClass(66, "remote"),
          new SynsetClass(67, "keyboard"),
          new SynsetClass(68, "cell phone"),
          new SynsetClass(69, "microwave"),
          new SynsetClass(70, "oven"),
          new SynsetClass(71, "toaster"),
          new SynsetClass(72, "sink"),
          new SynsetClass(73, "refrigerator"),
          new SynsetClass(74, "book"),
          new SynsetClass(75, "clock"),
          new SynsetClass(76, "vase"),
          new SynsetClass(77, "scissors"),
          new SynsetClass(78, "teddy bear"),
          new SynsetClass(79, "hair drier"),
          new SynsetClass(80, "toothbrush")
    );

    private Synset() {
        throw new UnsupportedOperationException("Class instance creation is unsupported");
    }

    public static String asString() {
        return SYNSET.stream()
              .sorted(Comparator.comparing(SynsetClass::num))
              .map(SynsetClass::name)
              .collect(Collectors.joining(","));
    }

    private record SynsetClass(int num, String name) {
    }

}
