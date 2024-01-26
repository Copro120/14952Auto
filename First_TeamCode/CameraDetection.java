package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public class CameraDetection {
    private TfodProcessor tfod;
    private AprilTagProcessor april;
    private VisionPortal pixelVisionPortal;
    private VisionPortal aprilVisionPortal;
    private final ElapsedTime runtime = new ElapsedTime();
    Telemetry telemetry;
    @SuppressLint("SdCardPath")
    public void initTfod(HardwareMap hwmap, Telemetry t, boolean red) {
        telemetry = t;
        final String[] LABELS = {"Pixel"};
        tfod = new TfodProcessor.Builder()
                //.setModelFileName("/sdcard/FIRST/tflitemodels/BLUE_TSE.tflite")
                .setModelLabels(LABELS)
                .setIsModelTensorFlow2(true)
                .setIsModelQuantized(true)
                .setModelInputSize(300)
                .setModelAspectRatio(16.0 / 9.0)
                .build();
        april = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();
        VisionPortal.Builder pixel_builder = new VisionPortal.Builder();
        VisionPortal.Builder april_builder = new VisionPortal.Builder();

        pixel_builder.setCamera(hwmap.get(WebcamName.class, "Cam 1"));
        april_builder.setCamera(hwmap.get(WebcamName.class, "Cam 2"));

        pixel_builder.enableLiveView(false);
        april_builder.enableLiveView(true);
        pixel_builder.addProcessor(tfod);
        april_builder.addProcessor(april);
        pixelVisionPortal = pixel_builder.build();
        aprilVisionPortal = april_builder.build();
        tfod.setMinResultConfidence(0.2f);
        pixelVisionPortal.setProcessorEnabled(tfod, true);
    }
    public Integer detect() {
        int first = 0;
        int second = 0;
        int third = 0;
        pixelVisionPortal.resumeStreaming();
        for (int i=0; i < 50; i++) {
            List<Recognition> currentRecognitions = tfod.getRecognitions();
            for (Recognition recognition : currentRecognitions) {
                double y = (recognition.getLeft() + recognition.getRight()) / 2;
                double threshold = recognition.getImageWidth() / 2.0;
                if (y < threshold) {first++;}
                else {second++;}
            }
            runtime.reset();
            while (runtime.seconds() < 0.01) {
                telemetry.update();
            }
        }
        pixelVisionPortal.stopStreaming();
        if (first > second && first > third) {return 1;}
        else if (second > first && second > third) {return 2;}
        else {return 3;}
    }
}
