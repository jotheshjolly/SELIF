package com.jolly.jothesh.imagedector;

public class MetricsManager {
    //Emotions
    private static final int EMOTIONS_BASE = 0;
    static final int ANGER = EMOTIONS_BASE;
    static final int CONTEMPT = ANGER + 1;
    static final int DISGUST = CONTEMPT + 1;
    static final int ENGAGEMENT = DISGUST + 1;
    static final int FEAR = ENGAGEMENT + 1;
    static final int JOY = FEAR + 1;
    static final int SADNESS = JOY + 1;
    static final int SURPRISE = SADNESS + 1;
    static final int VALENCE = SURPRISE + 1;



    private static final int EXPRESSIONS_BASE = VALENCE + 1;
    private static final int NUM_EMOTIONS = EXPRESSIONS_BASE - EMOTIONS_BASE;

    //Expressions
    static final int ATTENTION = EXPRESSIONS_BASE;
    static final int BROW_FURROW = ATTENTION + 1;
    static final int BROW_RAISE = BROW_FURROW + 1;
    static final int CHEEK_RAISE = BROW_RAISE + 1;
    static final int CHIN_RAISE = CHEEK_RAISE + 1;
    static final int DIMPLER = CHIN_RAISE + 1;
    static final int EYE_CLOSURE = DIMPLER + 1;
    static final int EYE_WIDEN = EYE_CLOSURE + 1;
    static final int INNER_BROW_RAISE = EYE_WIDEN + 1;
    static final int JAW_DROP = INNER_BROW_RAISE + 1;
    static final int LID_TIGHTEN = JAW_DROP + 1;
    static final int LIP_DEPRESSOR = LID_TIGHTEN + 1;
    static final int LIP_PRESS = LIP_DEPRESSOR + 1;
    static final int LIP_PUCKER = LIP_PRESS + 1;
    static final int LIP_STRETCH = LIP_PUCKER + 1;
    static final int LIP_SUCK = LIP_STRETCH + 1;
    static final int MOUTH_OPEN = LIP_SUCK + 1;
    static final int NOSE_WRINKLE = MOUTH_OPEN + 1;
    static final int SMILE = NOSE_WRINKLE + 1;
    static final int SMIRK = SMILE + 1;
    static final int UPPER_LIP_RAISE = SMIRK + 1;

    private static final int MEASUREMENTS_BASE = UPPER_LIP_RAISE + 1;

    //TRUTH
    private static final int NUM_EXPRESSIONS = MEASUREMENTS_BASE - EXPRESSIONS_BASE;

    //Measurements
    static final int YAW = MEASUREMENTS_BASE;
    static final int PITCH = YAW + 1;
    static final int ROLL = PITCH + 1;
    static final int INTER_OCULAR_DISTANCE = ROLL + 1;

    private static final int QUALITIES_BASE = INTER_OCULAR_DISTANCE + 1;
    private static final int NUM_MEASUREMENTS = QUALITIES_BASE - MEASUREMENTS_BASE;


    // Qualities
    static final int BRIGHTNESS = QUALITIES_BASE;

    private static final int APPEARANCES_BASE = BRIGHTNESS + 1;
    private static final int NUM_QUALITIES = APPEARANCES_BASE - QUALITIES_BASE;



    // Appearances
    static final int GENDER = APPEARANCES_BASE;
    static final int AGE = GENDER + 1;
    static final int ETHNICITY = AGE + 1;

    static final int TRUTH = ETHNICITY + 1;
    static final int FALSE = TRUTH + 1;


    //truth
    private static final int TRUTH_BASE = 38;
    private static final int NUM_TRUTH = 2;

    private static final int NUM_APPEARANCES = ETHNICITY - APPEARANCES_BASE  + 1;
    private static final int NUM_ALL_METRICS = NUM_EMOTIONS + NUM_EXPRESSIONS
            + NUM_MEASUREMENTS + NUM_APPEARANCES + NUM_QUALITIES+NUM_TRUTH;

    private static String[] lowerCaseNames;
    private static String[] upperCaseNames;

    //static initialization of arrays
    static {
        lowerCaseNames = new String[NUM_ALL_METRICS];
        upperCaseNames = new String[NUM_ALL_METRICS];

        //populate lower case array with emotion names
        lowerCaseNames[ANGER] = "anger";
        lowerCaseNames[CONTEMPT] = "contempt";
        lowerCaseNames[DISGUST] = "disgust";
        lowerCaseNames[ENGAGEMENT] = "engagement";
        lowerCaseNames[FEAR] = "fear";
        lowerCaseNames[JOY] = "joy";
        lowerCaseNames[SADNESS] = "sadness";
        lowerCaseNames[SURPRISE] = "surprise";
        lowerCaseNames[VALENCE] = "valence";

        //populate lower case array with expression names
        lowerCaseNames[ATTENTION] = "attention";
        lowerCaseNames[BROW_FURROW] = "brow_furrow";
        lowerCaseNames[BROW_RAISE] = "brow_raise";
        lowerCaseNames[CHEEK_RAISE] = "cheek_raise";
        lowerCaseNames[CHIN_RAISE] = "chin_raise";
        lowerCaseNames[DIMPLER] = "dimpler";
        lowerCaseNames[EYE_CLOSURE] = "eye_closure";
        lowerCaseNames[EYE_WIDEN] = "eye_widen";
        lowerCaseNames[INNER_BROW_RAISE] = "inner_brow_raise";
        lowerCaseNames[JAW_DROP] = "jaw_drop";
        lowerCaseNames[LID_TIGHTEN] = "lid_tighten";
        lowerCaseNames[LIP_DEPRESSOR] = "lip_depressor";
        lowerCaseNames[LIP_PRESS] = "lip_press";
        lowerCaseNames[LIP_PUCKER] = "lip_pucker";
        lowerCaseNames[LIP_STRETCH] = "lip_stretch";
        lowerCaseNames[LIP_SUCK] = "lip_suck";
        lowerCaseNames[MOUTH_OPEN] = "mouth_open";
        lowerCaseNames[NOSE_WRINKLE] = "nose_wrinkle";
        lowerCaseNames[SMILE] = "smile";
        lowerCaseNames[SMIRK] = "smirk";
        lowerCaseNames[UPPER_LIP_RAISE] = "upper_lip_raise";
        lowerCaseNames[YAW] = "yaw";
        lowerCaseNames[PITCH] = "pitch";
        lowerCaseNames[ROLL] = "roll";
        lowerCaseNames[INTER_OCULAR_DISTANCE] = "inter_ocular_distance";
        lowerCaseNames[BRIGHTNESS] = "brightness";

        lowerCaseNames[GENDER] = "gender";
        lowerCaseNames[AGE] = "age";
        lowerCaseNames[ETHNICITY] = "ethnicity";

        lowerCaseNames[TRUTH] = "truth";
        lowerCaseNames[FALSE] = "false";

    try
    {
    //use lowerCaseNames array to populate upperCaseNames array
    for (int n = 0; n < lowerCaseNames.length; n++) {
        upperCaseNames[n] = lowerCaseNames[n].replace("_", " ").toUpperCase();
    }
    }catch (NullPointerException e)
    {

    }


    }

    static String getMetricLowerCaseName(int index) {
        if (index >= 0 && index < lowerCaseNames.length) {
            return lowerCaseNames[index];
        } else {
            return "";
        }
    }

    static String getMetricUpperCaseName(int index) {
        if (index >= 0 && index < upperCaseNames.length) {
            return upperCaseNames[index];
        } else {
            return "";
        }
    }

    static int getTotalNumNumericMetrics() {
        return NUM_EMOTIONS + NUM_EXPRESSIONS + NUM_MEASUREMENTS + NUM_QUALITIES;
    }


    static int getTotalNumMetrics() {
        return NUM_ALL_METRICS;
    }

    static int[] getEmotionsIndexArray() {
        int[] toReturn = new int[NUM_EMOTIONS];
        for (int n = 0; n < toReturn.length; n++) {
            toReturn[n] = n + EMOTIONS_BASE;
        }
        return toReturn;
    }

    static int[] getExpressionsIndexArray() {
        int[] toReturn = new int[NUM_EXPRESSIONS];
        for (int n = 0; n < toReturn.length; n++) {
            toReturn[n] = n + EXPRESSIONS_BASE;
        }
        return toReturn;
    }

    static int[] getMeasurementsIndexArray() {
        int[] toReturn = new int[NUM_MEASUREMENTS];
        for (int n = 0; n < toReturn.length; n++) {
            toReturn[n] = n + MEASUREMENTS_BASE;
        }
        return toReturn;
    }

    static int[] getAppearanceIndexArray() {
        int[] toReturn = new int[NUM_APPEARANCES];
        for (int n = 0; n < toReturn.length; n++) {
            toReturn[n] = n + APPEARANCES_BASE;
        }
        return toReturn;
    }

    static int[] getQualitiesIndexArray() {
        int[] toReturn = new int[NUM_QUALITIES];
        for (int n = 0; n < toReturn.length; n++) {
            toReturn[n] = n + QUALITIES_BASE;
        }
        return toReturn;
    }

    static int[] getTruthIndexArray() {
        int[] toReturn = new int[NUM_TRUTH];
        for (int n = 0; n < toReturn.length; n++) {
            toReturn[n] = n + TRUTH_BASE;
        }
        return toReturn;
    }
}
