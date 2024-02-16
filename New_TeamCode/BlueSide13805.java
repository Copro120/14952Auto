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

@Autonomous(name = "13805 Blue", group = "Android Studio")

public class BlueSide13805 extends LinearOpMode{
    
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
                robot.drive(robot.val(315,315,315,315),.1,true,5);
                robot.rotate(90);
                robot.drive(robot.val(115,115,115,115),.1,true,5);
                Linear.setPosition(0.2);
                sleep(750);
                robot.drive(robot.val(-110,-110,-110,-110),.1,true,5);
                robot.drive(robot.val(-200,200,200,-200),.1,true,5);
                robot.drive(robot.val(275,275,275,275),.1,true,5);
                robot.drive(robot.val(50,-50,-50,50),.1,true,5);
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
                robot.drive(robot.val(-50,-50,-50,-50),.1,true,5);
                Arm.setPosition(.98);
                
            }
            
            if (camera_result == 2){
                
                robot.drive(robot.val(365,365,365,365),.1,true,5);
                Linear.setPosition(.2);
                sleep(750);
                robot.drive(robot.val(-150,-150,-150,-150),.1,true,5);
                robot.rotate(90);
                robot.drive(robot.val(275,275,275,275),.1,true,5);
                sleep(250);
                Arm.setPosition(.71);
                sleep(1000);
                robot.drive(robot.val(185,185,185,185),.1,true,5);
                
                Claw.setPosition(0.08);
                sleep(2000);
                Arm.setPosition(.60);
                sleep(500);
                robot.drive(robot.val(-50,-50,-50,-50),.1,true,5);
                Arm.setPosition(.98);
                
            }
            
            if (camera_result == 3){
                
                robot.drive(robot.val(245,245,245,245),.1,true,5);
                sleep(250);
                robot.rotate(-90);
                robot.drive(robot.val(150,150,150,150),.1,true,5);
                sleep(250);
                Linear.setPosition(.20);
                sleep(750);
                robot.drive(robot.val(-200,-200,-200,-200),.1,true,5);
                robot.rotate(90);
                robot.drive(robot.val(150,150,150,150),.1,true,5);
                sleep(250);
                Arm.setPosition(.71);
                sleep(700);
                robot.drive(robot.val(210,210,210,210),.1,true,5);
                sleep(500);
                Claw.setPosition(0.08);
                sleep(2000);
                Arm.setPosition(.60);
                sleep(500);
                robot.drive(robot.val(-50,-50,-50,-50),.1,true,5);
                Arm.setPosition(.98);
                
            }
            
            telemetry.addData("camera", camera_result);
            telemetry.update();
        }
    }
}