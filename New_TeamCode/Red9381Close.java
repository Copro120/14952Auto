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

@Autonomous(name = "9381 tourney", group = "Android Studio")

public class Red9381Close extends LinearOpMode{
    
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
                
                /*robot.drive(robot.val(250,250,250,250),.1,true,5);
                
                robot.rotate(45);
                robot.drive(robot.val(161,161,161,161),.1,true,5);
                sleep(250);
                Linear.setPosition(.20);
                sleep(750);
                robot.drive(robot.val(-150,-150,-150,-150),.1,true,5);
                robot.rotate(0);
                robot.drive(robot.val(400,400,400,400),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(450,450,450,450),.1,true,5);*/
                robot.drive(robot.val(300,300,300,300),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(75,75,75,75),.1,true,5);
                Linear.setPosition(.2);
                sleep(750);
                robot.drive(robot.val(-75,-75,-75,-75),.1,true,5);
                robot.rotate(0);
                robot.drive(robot.val(200,200,200,200),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(400,400,400,400),.1,true,5);
                
            }
            
            if (camera_result == 2){
                
                /*robot.drive(robot.val(375,375,375,375),.1,true,5);
                Linear.setPosition(.2);
                sleep(750);
                robot.drive(robot.val(200,200,200,200),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(400,400,400,400),.1,true,5);*/
                robot.drive(robot.val(300,300,300,300),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(75,75,75,75),.1,true,5);
                Linear.setPosition(.2);
                sleep(750);
                robot.drive(robot.val(-75,-75,-75,-75),.1,true,5);
                robot.rotate(0);
                robot.drive(robot.val(200,200,200,200),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(400,400,400,400),.1,true,5);
            }
            
            if (camera_result == 3){
                
                robot.drive(robot.val(300,300,300,300),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(75,75,75,75),.1,true,5);
                Linear.setPosition(.2);
                sleep(750);
                robot.drive(robot.val(-75,-75,-75,-75),.1,true,5);
                robot.rotate(0);
                robot.drive(robot.val(200,200,200,200),.1,true,5);
                robot.rotate(-90);
                robot.drive(robot.val(400,400,400,400),.1,true,5);
                
            }
            
            telemetry.addData("camera", camera_result);
            telemetry.update();
        }
    }
}