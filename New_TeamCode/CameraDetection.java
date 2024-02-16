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
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public class CameraDetection {
    private TfodProcessor tfod;
    private VisionPortal pixelVisionPortal;
    private final ElapsedTime runtime = new ElapsedTime();
    Telemetry telemetry;
    @SuppressLint("SdCardPath")
    public void initTfod(HardwareMap hwmap, Telemetry t, boolean red) {
        telemetry = t;
        final String[] LABELS = {"Pixel"};
        tfod = new TfodProcessor.Builder()
                .setModelLabels(LABELS)
                .setIsModelTensorFlow2(true)
                .setIsModelQuantized(true)
                .setModelInputSize(300)
                .setModelAspectRatio(16.0 / 9.0)
                .build();

        VisionPortal.Builder pixel_builder = new VisionPortal.Builder();


        pixel_builder.setCamera(hwmap.get(WebcamName.class, "Cam 1"));


        pixel_builder.enableLiveView(true);

        pixel_builder.addProcessor(tfod);

        pixelVisionPortal = pixel_builder.build();

        tfod.setMinResultConfidence(0.2f);
        pixelVisionPortal.setProcessorEnabled(tfod, true);
    }
    public Integer detect() {
        int first = 0;
        int second = 0;
        pixelVisionPortal.resumeStreaming();
        for (int i=0; i < 50; i++) {
            List<Recognition> currentRecognitions = tfod.getRecognitions();
            for (Recognition recognition : currentRecognitions) {
                double center_frame = recognition.getImageWidth() / 2.0;
                double center_pixel = (recognition.getLeft() + recognition.getRight()) / 2;
                if (center_pixel < center_frame) {first++;}
                else {second++;}
            }
            runtime.reset();
            while (runtime.seconds() < 0.01) {
                telemetry.update();
            }
        }
        pixelVisionPortal.stopStreaming();
        
        if (first > second) {return 1;}
        else if (first < second) {return 2;}
        else {return 3;}
    }
}
