package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Beskar Red", group = "Android Studio")

public class RedAuton extends LinearOpMode{
    
    private final EncoderTest robot   = new EncoderTest();
    private final CameraDetection camera   = new CameraDetection();
    private Integer camera_result;
    
    private Servo Arm;
    private Servo Claw;
    private Servo Linear;
    
    
    
    @Override
    public void runOpMode() {
        robot.init(hardwareMap, telemetry);
        camera.initTfod(hardwareMap, telemetry, false);
        Arm = hardwareMap.get(Servo.class, "servo");
        Claw = hardwareMap.get(Servo.class, "servo2");
        Linear = hardwareMap.get(Servo.class, "linear");
        
        Linear.setPosition(0.62);
        Claw.setPosition(0.2);
        
        waitForStart();
        
        if (opModeIsActive()) {
            
            

            camera_result = camera.detect();
            
            if (camera_result == 1){
                robot.drive(robot.val(300,300,300,300),.1,true,5);
                robot.rotate(45);
                robot.drive(robot.val(125,125,125,125),.2,true,5);
                Linear.setPosition(0.2);
                sleep(750);
                robot.drive(robot.val(-200,-200,-200,-200),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(300,300,300,300),.1,true,5);
                robot.drive(robot.val(-225,225,225,-225),.1,true,5);
                Claw.setPosition(.3);
                sleep(1000);
                Arm.setPosition(0.70);
                sleep(2000);
                robot.drive(robot.val(200,200,200,200),.1,true,5);
                sleep(750);
                Claw.setPosition(0.08);
                sleep(2000);
                Arm.setPosition(.60);
                sleep(500);
                robot.drive(robot.val(-150,-150,-150,-150),.1,true,5);
                Arm.setPosition(.98);
                sleep(750);
                robot.drive(robot.val(350,-350,-350,350),.1,true,5);
                robot.drive(robot.val(200,200,200,200),.1,true,5);
                
            }
            
            if (camera_result == 2){
                
                robot.drive(robot.val(375,375,375,375),.1,true,5);
                Linear.setPosition(.2);
                sleep(750);
                robot.drive(robot.val(-130,-130,-130,-130),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(275,275,275,275),.1,true,5);
                sleep(250);
                Arm.setPosition(.70);
                sleep(1000);
                robot.drive(robot.val(205,205,205,205),.1,true,5);
                
                Claw.setPosition(0.08);
                sleep(2000);
                Arm.setPosition(.60);
                sleep(500);
                robot.drive(robot.val(-150,-150,-150,-150),.1,true,5);
                Arm.setPosition(.98);
                sleep(750);
                robot.drive(robot.val(375,-375,-375,375),.1,true,5);
                robot.drive(robot.val(250,250,250,250),.1,true,5);
                
            }
            
            if (camera_result == 3){
                
                robot.drive(robot.val(250,250,250,250),.1,true,5);
                robot.drive(robot.val(100,-100,-100,100),.15,true,5);
                robot.rotate(-45);
                robot.drive(robot.val(161,161,161,161),.1,true,5);
                sleep(250);
                Linear.setPosition(.20);
                sleep(750);
                robot.drive(robot.val(-150,-150,-150,-150),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(301,301,301,301),.1,true,5);
                robot.drive(robot.val(-130,130,130,-130),.1,true,5);
                sleep(250);
                Arm.setPosition(.70);
                sleep(700);
                robot.drive(robot.val(200,200,200,200),.1,true,5);
                sleep(500);
                Claw.setPosition(0.08);
                sleep(2000);
                Arm.setPosition(.60);
                sleep(500);
                robot.drive(robot.val(-150,-150,-150,-150),.1,true,5);
                Arm.setPosition(.98);
                sleep(750);
                robot.drive(robot.val(200,-200,-200,200),.1,true,5);
                robot.drive(robot.val(300,300,300,300),.1,true,5);
                sleep(250);
            }
            
            telemetry.addData("camera", camera_result);
            telemetry.update();
        }
    }
}