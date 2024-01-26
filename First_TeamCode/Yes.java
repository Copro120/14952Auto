/* 
"Roses" = #ff0000;
"Violets" = #0000ff;

*/
// this code is very important, every program needs it.
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

// declares as TeleOp:
@TeleOp(name="14952 TeleOp", group="Linear Opmode")

public class Yes extends LinearOpMode {
    

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position
    
    private ElapsedTime runtime = new ElapsedTime();
    
    // wheel motors:
    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;
    // four stage:
    private DcMotor extension;
    // hang kits:
    private DcMotor leftHang;
    private DcMotor rightHang;
    // servo
    private Servo   servo;
    private Servo   servo2;
    private Servo   lservo;
    double  position = 0.90;
    double  position2 = 0;
    boolean rampUp = true;


    


    @Override
    public void runOpMode() {

        // Initialize hardware
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        extension = hardwareMap.get(DcMotor.class, "extension");
        leftHang = hardwareMap.get(DcMotor.class, "leftHang");
        rightHang = hardwareMap.get(DcMotor.class, "rightHang");
        servo = hardwareMap.get(Servo.class, "servo");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        lservo = hardwareMap.get(Servo.class, "linear");
        
    // this is an intresting function /
    //                               /
    //                              \/ no one knows what it does?
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftHang.setDirection(DcMotor.Direction.FORWARD);
        rightHang.setDirection(DcMotor.Direction.FORWARD);
        extension.setDirection(DcMotor.Direction.FORWARD);
        
        

        // Waits for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();
        
        
        servo.setPosition(0.90);
        servo2.setPosition(-0.2);
        


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x / 2;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = ( yaw + (axial + lateral));
            double rightFrontPower = (-yaw + (axial - lateral));
            double leftBackPower   = ( yaw + (axial - lateral));
            double rightBackPower  = (-yaw + (axial + lateral));

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            leftFrontPower = Range.clip(leftFrontPower, -.98, .98);
            rightFrontPower = Range.clip(rightFrontPower, -.98, .98);
            leftBackPower = Range.clip(leftBackPower, -.98, .98);
            rightBackPower = Range.clip(rightBackPower, -.98, .98);
            
            

            // This is test code:
            //
            // Uncomment this code to test motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.

            /*
            leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
            leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
            rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
            */

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);
           
            
            // Hanging Mechanism
            if (gamepad2.left_bumper) {
                leftHang.setPower(1.0);
                rightHang.setPower(1.0);
                
            }
            if (gamepad2.right_bumper) {
                leftHang.setPower(-1.0);
                rightHang.setPower(-1.0);
                
            }
            
            extension.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
            
            
            leftHang.setPower(0);
            rightHang.setPower(0);
            
            
            //Servo
            if (gamepad2.x) {
               // 
                if (position < 0.90) {
                    position = position + 0.01;
                    servo.setPosition(position);
                    sleep(CYCLE_MS);
                    idle();
                }
            }
            if (gamepad2.y) {
                //
                if (position > 0.65) {
                    position = position - 0.1;
                    servo.setPosition(position);
                    sleep(CYCLE_MS);
                    idle();
                }

            }
            
            //Servo2
            if (gamepad2.a) {
                servo2.setPosition(-0.2);
            }
            if (gamepad2.b) {
                servo2.setPosition(0.08);
            }
            //inear actuator
            if (gamepad1.right_bumper) {
               // lservo.setPosition(1.0);
            if (position2 < 0.60) {
                    position2 = position2 + 0.10;
                    lservo.setPosition(position2);
                    sleep(CYCLE_MS);
                    idle();
                }
            }
            if (gamepad1.left_bumper) {
                //lservo.setPosition(0.0);
            if (position2 > 0.0) {
                    position2 = position2 - 0.10;
                    lservo.setPosition(position2);
                    sleep(CYCLE_MS);
                    idle();
                }
            }
            
            /*
            // Extension
            while (gamepad2.a) {
                extension.setPower(0.8);
            }
            while (gamepad2.b) {
                extension.setPower(-0.4);
            }*/
            
            extension.setPower(0);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
        }

    }
    
}

