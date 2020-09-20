package com.jolly.jothesh.imagedector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.affectiva.android.affdex.sdk.detector.PhotoDetector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity implements Detector.ImageListener {

    public static final String LOG_TAG = "jolly";
    public static final int PICK_IMAGE = 100;

    int min = 5;
    int max = 20;
    int random = new Random().nextInt((max - min) + 1) + min;

    Button bcam;
    ImageView imageView;
    TextView[] metricScoreTextViews;

    TextView angry,fear,joy,sad,suprise;

    LinearLayout metricsContainer;

    private static final int PICK_FILE_REQUEST = 1;

    PhotoDetector detector;
    Bitmap bitmap = null;
    Frame.BitmapFrame frame;

    String play;
    int i=1;

    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1889;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*if(i==1)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
            i=0;
        }*/

        angry = findViewById(R.id.anger);
        fear = findViewById(R.id.fear);
        sad = findViewById(R.id.sad);
        joy = findViewById(R.id.joy);
        suprise = findViewById(R.id.suprise);


        bcam = (Button)findViewById(R.id.cam);

        bcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                showFileChooser();
               // startActivityForResult(intent, 0);
            }
        });


        initUI();

        Log.e(LOG_TAG, "onCreate");

        try {
            loadInitialImage();
        } catch (IOException ioe) {
            Log.e(LOG_TAG, "loading initial image", ioe);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "onResume");

    }

    void loadInitialImage() throws IOException {
        if (bitmap == null) {
            bitmap = getBitmapFromAsset(this, "drawable/face.png");
        }
        setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, false);
    }

    void startDetector() {
        if (!detector.isRunning()) {
            detector.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG_TAG, "onPause");

    }

    void stopDetector() {
        if (detector.isRunning()) {
            detector.stop();
        }
    }

    void initUI() {
        metricsContainer = (LinearLayout) findViewById(R.id.metrics_container);
        metricScoreTextViews = MetricsPanelCreator.createScoresTextViews();
        MetricsPanelCreator.populateMetricsContainer(metricsContainer,metricScoreTextViews,this);

        imageView = (ImageView) findViewById(R.id.image_view);
    }



    public Bitmap getBitmapFromAsset(Context context, String filePath) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap;
        istr = assetManager.open(filePath);
        bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    public Bitmap getBitmapFromUri(Uri uri) throws FileNotFoundException {
        InputStream istr;
        Bitmap bitmap;
        istr = getContentResolver().openInputStream(uri);
        bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    public void select_new_image(View view) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    void setAndProcessBitmap(Frame.ROTATE rotation, boolean isExpectingFaceDetection) {
        if (bitmap == null) {
            return;
        }

        switch (rotation) {
            case BY_90_CCW:
                bitmap = Frame.rotateImage(bitmap,-90);
                break;
            case BY_90_CW:
                bitmap = Frame.rotateImage(bitmap,90);
                break;
            case BY_180:
                bitmap = Frame.rotateImage(bitmap,180);
                break;
            default:
                //keep bitmap as it is
        }

        frame = new Frame.BitmapFrame(bitmap, Frame.COLOR_FORMAT.UNKNOWN_TYPE);

        detector = new PhotoDetector(this,1, Detector.FaceDetectorMode.LARGE_FACES );
        detector.setDetectAllEmotions(true);
        detector.setDetectAllExpressions(true);
        detector.setDetectAllAppearances(true);
        detector.setImageListener(this);

        startDetector();
        detector.process(frame);
        stopDetector();

    }

    @SuppressWarnings("SuspiciousNameCombination")
    Bitmap drawCanvas(int width, int height, PointF[] points, Frame frame, Paint circlePaint) {
        if (width <= 0 || height <= 0) {
            return null;
        }

        Bitmap blackBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        blackBitmap.eraseColor(Color.BLACK);
        Canvas c = new Canvas(blackBitmap);

        Frame.ROTATE frameRot = frame.getTargetRotation();
        Bitmap bitmap;

        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int canvasWidth = c.getWidth();
        int canvasHeight = c.getHeight();
        int scaledWidth;
        int scaledHeight;
        int topOffset = 0;
        int leftOffset= 0;
        float radius = (float)canvasWidth/100f;

        if (frame instanceof Frame.BitmapFrame) {
            bitmap = ((Frame.BitmapFrame)frame).getBitmap();
        } else { //frame is ByteArrayFrame
            byte[] pixels = ((Frame.ByteArrayFrame)frame).getByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(pixels);
            bitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
        }

        if (frameRot == Frame.ROTATE.BY_90_CCW || frameRot == Frame.ROTATE.BY_90_CW) {
            int temp = frameWidth;
            frameWidth = frameHeight;
            frameHeight = temp;
        }

        float frameAspectRatio = (float)frameWidth/(float)frameHeight;
        float canvasAspectRatio = (float) canvasWidth/(float) canvasHeight;
        if (frameAspectRatio > canvasAspectRatio) { //width should be the same
            scaledWidth = canvasWidth;
            scaledHeight = (int)((float)canvasWidth / frameAspectRatio);
            topOffset = (canvasHeight - scaledHeight)/2;
        } else { //height should be the same
            scaledHeight = canvasHeight;
            scaledWidth = (int) ((float)canvasHeight*frameAspectRatio);
            leftOffset = (canvasWidth - scaledWidth)/2;
        }

        float scaling = (float)scaledWidth/(float)frame.getOriginalBitmapFrame().getWidth();

        Matrix matrix = new Matrix();
        matrix.postRotate((float)frameRot.toDouble());
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,frameWidth,frameHeight,matrix,false);
        c.drawBitmap(rotatedBitmap,null,new Rect(leftOffset,topOffset,leftOffset+scaledWidth,topOffset+scaledHeight),null);


        if (points != null) {
            //Save our own reference to the list of points, in case the previous reference is overwritten by the main thread.

            for (PointF point : points) {

                //transform from the camera coordinates to our screen coordinates
                //The camera preview is displayed as a mirror, so X pts have to be mirrored back.
                float x = (point.x * scaling) + leftOffset;
                float y = (point.y * scaling) + topOffset;

                c.drawCircle(x, y, radius, circlePaint);
            }
        }

        return blackBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(LOG_TAG, "onActivityForResult");


        if (resultCode == RESULT_OK ) {
            //Do stuff with the camara data result
            //Uri imageUri = data.getData();


            // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

            //setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, true);


            if(requestCode == PICK_FILE_REQUEST) {
                //Do stuff with the gallery data result
                Uri selectedFileUri = data.getData();
                String selectedFilePath = FilePath.getPath(this,selectedFileUri);
                bitmap = BitmapFactory.decodeFile(selectedFilePath);
               // bitmap = ((android.graphics.drawable.BitmapDrawable) imageView.getDrawable()).getBitmap();
               // bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, true);
                Toast.makeText(this, "No image selected.", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void play(View view) {

        Intent intent = new Intent(MainActivity.this, suggestions.class);
        intent.putExtra("play", play);
        startActivity(intent);

    }


    public void rotate_left(View view) {
        setAndProcessBitmap(Frame.ROTATE.BY_90_CCW, true);
    }

    public void rotate_right(View view) {
        setAndProcessBitmap(Frame.ROTATE.BY_90_CW,true);
    }

    public void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame image, float timestamp) {

        PointF[] points = null;

        if (faces != null && faces.size() > 0) {
            Face face = faces.get(0);
            setMetricTextViewText(face);
            points = face.getFacePoints();
        } else {
            for (int n = 0; n < MetricsManager.getTotalNumMetrics(); n++) {
                metricScoreTextViews[n].setText("___");
            }
        }

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        Bitmap imageBitmap = drawCanvas(imageView.getWidth(),imageView.getHeight(),points,image,circlePaint);
        if (imageBitmap != null)
            imageView.setImageBitmap(imageBitmap);
    }


    private void setMetricTextViewText(Face face) {
        // set the text for all the numeric metrics (scored or measured)
        for (int n = 0; n < 40 /*MetricsManager.getTotalNumNumericMetrics()*/; n++) {
            metricScoreTextViews[n].setText(String.format("%.3f", getScore(n, face)));
        }

        // set the text for the appearance metrics
        String textValue="";
        switch (face.appearance.getGender()) {
            case UNKNOWN:
                textValue = "unknown";
                break;
            case FEMALE:
                textValue = "female";
                break;
            case MALE:
                textValue = "male";
                break;
        }
        metricScoreTextViews[MetricsManager.GENDER].setText(textValue);

        switch (face.appearance.getAge()) {
            case AGE_UNKNOWN:
                textValue = "unknown";
                break;
            case AGE_UNDER_18:
                textValue = "under 18";
                break;
            case AGE_18_24:
                textValue = "18-24";
                break;
            case AGE_25_34:
                textValue = "25-34";
                break;
            case AGE_35_44:
                textValue = "35-44";
                break;
            case AGE_45_54:
                textValue = "45-54";
                break;
            case AGE_55_64:
                textValue = "55-64";
                break;
            case AGE_65_PLUS:
                textValue = "65+";
                break;
        }
        metricScoreTextViews[MetricsManager.AGE].setText(textValue);

        switch (face.appearance.getEthnicity()) {
            case UNKNOWN:
                textValue = "unknown";
                break;
            case CAUCASIAN:
                textValue = "caucasian";
                break;
            case BLACK_AFRICAN:
                textValue = "black african";
                break;
            case EAST_ASIAN:
                textValue = "east asian";
                break;
            case SOUTH_ASIAN:
                textValue = "south asian";
                break;
            case HISPANIC:
                textValue = "hispanic";
                break;
        }
        metricScoreTextViews[MetricsManager.ETHNICITY].setText(textValue);
    }

    float getScore(int metricCode, Face face) {

        float score = 0;

        switch (metricCode) {
            case MetricsManager.ANGER:
                score = face.emotions.getAnger();
                angry.setText(face.emotions.getAnger()+"");
                break;
            case MetricsManager.CONTEMPT:
                score = face.emotions.getContempt();
                break;
            case MetricsManager.DISGUST:
                score = face.emotions.getDisgust();
                break;
            case MetricsManager.FEAR:
                score = face.emotions.getFear();
                fear.setText(face.emotions.getFear()+"");
                break;
            case MetricsManager.JOY:
                score = face.emotions.getJoy();
                joy.setText(face.emotions.getJoy()+"");
                break;
            case MetricsManager.SADNESS:
                score = face.emotions.getSadness();
                sad.setText(face.emotions.getSadness()+"");
                break;
            case MetricsManager.SURPRISE:
                score = face.emotions.getSurprise();
                suprise.setText(face.emotions.getSurprise()+"");
                break;
            case MetricsManager.ATTENTION:
                score = face.expressions.getAttention();
                break;
            case MetricsManager.BROW_FURROW:
                score = face.expressions.getBrowFurrow();
                break;
            case MetricsManager.BROW_RAISE:
                score = face.expressions.getBrowRaise();
                break;
            case MetricsManager.CHEEK_RAISE:
                score = face.expressions.getCheekRaise();
                break;
            case MetricsManager.CHIN_RAISE:
                score = face.expressions.getChinRaise();
                break;
            case MetricsManager.DIMPLER:
                score = face.expressions.getDimpler();
                break;
            case MetricsManager.ENGAGEMENT:
                score = face.emotions.getEngagement();
                break;
            case MetricsManager.EYE_CLOSURE:
                score = face.expressions.getEyeClosure();
                break;
            case MetricsManager.EYE_WIDEN:
                score = face.expressions.getEyeWiden();
                break;
            case MetricsManager.INNER_BROW_RAISE:
                score = face.expressions.getInnerBrowRaise();
                break;
            case MetricsManager.JAW_DROP:
                score = face.expressions.getJawDrop();
                break;
            case MetricsManager.LID_TIGHTEN:
                score = face.expressions.getLidTighten();
                break;
            case MetricsManager.LIP_DEPRESSOR:
                score = face.expressions.getLipCornerDepressor();
                break;
            case MetricsManager.LIP_PRESS:
                score = face.expressions.getLipPress();
                break;
            case MetricsManager.LIP_PUCKER:
                score = face.expressions.getLipPucker();
                break;
            case MetricsManager.LIP_STRETCH:
                score = face.expressions.getLipStretch();
                break;
            case MetricsManager.LIP_SUCK:
                score = face.expressions.getLipSuck();
                break;
            case MetricsManager.MOUTH_OPEN:
                score = face.expressions.getMouthOpen();
                break;
            case MetricsManager.NOSE_WRINKLE:
                score = face.expressions.getNoseWrinkle();
                break;
            case MetricsManager.SMILE:
                score = face.expressions.getSmile();
                break;
            case MetricsManager.SMIRK:
                score = face.expressions.getSmirk();
                break;
            case MetricsManager.UPPER_LIP_RAISE:
                score = face.expressions.getUpperLipRaise();
                break;
            case MetricsManager.VALENCE:
                score = face.emotions.getValence();
                break;
            case MetricsManager.YAW:
                score = face.measurements.orientation.getYaw();
                break;
            case MetricsManager.ROLL:
                score = face.measurements.orientation.getRoll();
                break;
            case MetricsManager.PITCH:
                score = face.measurements.orientation.getPitch();
                break;
            case MetricsManager.INTER_OCULAR_DISTANCE:
                score = face.measurements.getInterocularDistance();
                break;
            case MetricsManager.BRIGHTNESS:
                score = face.qualities.getBrightness();
                break;
            case MetricsManager.TRUTH:
                if(face.expressions.getAttention()>50)
                {
                    score = 20+random ;
                    if(face.emotions.getFear()<20 )
                        score = 55+random ;
                    if(face.emotions.getFear()<20 &&  face.emotions.getJoy()>50  )
                        score = 41+random ;
                    if(face.emotions.getFear()<20 &&  face.emotions.getJoy()>50 && face.expressions.getBrowRaise()>50)
                        score = 35+random ;
                    if(face.emotions.getFear()<20 &&  face.emotions.getJoy()>50 && face.expressions.getBrowRaise()>50 &&face.emotions.getSadness()<30  )
                        score = 45+random ;
                    if(face.emotions.getFear()<5.0 &&  face.emotions.getJoy()>50 && face.expressions.getBrowRaise()>50 &&face.emotions.getSadness()<30 &&face.expressions.getMouthOpen()>50 )
                        score = 65+random ;

                }
                break;
            case MetricsManager.FALSE:
                if(face.expressions.getAttention()>50)
                {
                    score = 35+random ;
                    if(face.emotions.getFear()>0.7 )
                        score = 35+random ;
                    if(face.emotions.getFear()>0.7 && face.emotions.getSadness()>30 )
                        score = 45+random ;
                    if(face.emotions.getFear()>0.7 && face.emotions.getSadness()>30 && face.emotions.getJoy()>50 )
                        score = 55+random ;
                    if(face.emotions.getFear()>0.7 && face.emotions.getSadness()>30 && face.emotions.getJoy()>50 && face.expressions.getBrowRaise()<50 )
                        score = 65+random ;
                    if(face.emotions.getFear()>0.7 && face.emotions.getSadness()>30 && face.emotions.getJoy()>50 && face.expressions.getBrowRaise()<50 && face.expressions.getMouthOpen()<10)
                        score = 75+random ;

                }
                break;
            default:    
                score = Float.NaN;
                break;
        }

        if(face.emotions.getAnger() > face.emotions.getJoy() && face.emotions.getAnger() > face.emotions.getSurprise()  )
        {
            Toast.makeText(getApplicationContext(),"anger",Toast.LENGTH_SHORT).show();
            play="anger";
        }else {
            if(face.emotions.getAnger() < face.emotions.getJoy() && face.emotions.getAnger() < face.emotions.getSurprise()  )
            {
                Toast.makeText(getApplicationContext(),"joy & surprised",Toast.LENGTH_SHORT).show();
                play="joy";
            }else {
                if(face.emotions.getSadness() > face.emotions.getJoy() && face.emotions.getSadness() > face.emotions.getSurprise()  )
                {
                    Toast.makeText(getApplicationContext(),"sad",Toast.LENGTH_SHORT).show();
                    play="sad";
                }else {
                    if(face.emotions.getFear() > face.emotions.getJoy() && face.emotions.getFear() > face.emotions.getSurprise()  )
                    {
                        Toast.makeText(getApplicationContext(),"fear",Toast.LENGTH_SHORT).show();
                        play="fear";
                    }
                }
            }
        }



        return score;
    }


}