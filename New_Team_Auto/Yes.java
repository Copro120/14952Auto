/* 
"Roses" = #ff0000;
"Violets" = #0000ff;
Syntax is sweet,
Boolean's are true.
Everything is broken,
What did you do.
*/
// this code is very important, every program needs it, Maybe.
package org.firstinspires.ftc.teamcode;                         // the full package.
//                                                                 _
import com.qualcomm.robotcore.eventloop.opmode.Disabled;        //  \ 
import com.qualcomm.robotcore.util.Range;                       //  |
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;    //  \
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;          //   >individual libraries from FTC package.
import com.qualcomm.robotcore.hardware.DcMotor;                 //  /
import com.qualcomm.robotcore.util.ElapsedTime;                 //  |
import com.qualcomm.robotcore.hardware.Servo;                   // _/

@TeleOp(name="14952 TeleOp", group="Linear Opmode")             // declares as TeleOp:

public class Yes extends LinearOpMode {
    
    
    private ElapsedTime runtime = new ElapsedTime();    // This is the elapsed runtime of the program.
    
    // wheel motors:
    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;
    // four stage:
    private DcMotor extension;
    // hang kits:
    private DcMotor leftHang; // I think that left or right is based on FPV.
    private DcMotor rightHang;
    // servos
    private Servo   servo;  // upper arm servo.
    private Servo   servo2; // lower arm servo / claw.
    private Servo   lservo; // linear servo.
    // these are for the servos slower movement:
    double  servo_position = 0.90;
    double  servo2_position = 0;  // linear servo actually.
    
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
    //                              \/ 
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftHang.setDirection(DcMotor.Direction.FORWARD);
        rightHang.setDirection(DcMotor.Direction.FORWARD);
        extension.setDirection(DcMotor.Direction.FORWARD);
        

        
        telemetry.addData("Status", "Initialized"); // Telemetry just shows the current value of the motors on the driver hub.
        telemetry.update();                         // It's not necessary so we can delete it for preformance purposes.


        waitForStart(); // Waits for the game to start (driver presses PLAY). bet you couldn't tell.
        runtime.reset();
        

        servo.setPosition(0.90); // This is to help guarantee that the servo is in the correct position.
        servo2.setPosition(-0.2);// Same here.
        


        // this is the main loop that runs in the opmode.
        while (opModeIsActive()) {

            //controler 1 left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing the stick forward gives a negative value.
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x / 2;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = ( yaw + (axial + lateral));
            double rightFrontPower = (-yaw + (axial - lateral));    // By the way this code here is a great example of good optimization.
            double leftBackPower   = ( yaw + (axial - lateral));    // Because instead of trying to set the power level sevral times
            double rightBackPower  = (-yaw + (axial + lateral));    // It does the math first and then sets the power level.
                                                                    
                                                                    
            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            leftFrontPower = Range.clip(leftFrontPower, -.98, .98);
            rightFrontPower = Range.clip(rightFrontPower, -.98, .98);
            leftBackPower = Range.clip(leftBackPower, -.98, .98);
            rightBackPower = Range.clip(rightBackPower, -.98, .98);
            
            

            // This is test code: // this is outdated. // Don't worry about it.//
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
           
            
            // Hanging Mechanism                           // <-- If there is time, set a safety cap on this.
            if (gamepad2.left_bumper) {
                leftHang.setPower(1.0);
                rightHang.setPower(1.0);
        
            }
            if (gamepad2.right_bumper) {
                leftHang.setPower(-1.0);
                rightHang.setPower(-1.0);
                
            }
            else {
            leftHang.setPower(0);
            rightHang.setPower(0);
        }
            
            // upper arm servo
            if (gamepad2.x) {
               // servo down
                if (servo_position < 0.90) {
                    servo_position = servo_position + 0.01; // I need to check this to make an anjustment.
                    servo.setPosition(servo_position);
                    sleep(50);
                    idle();
                }
            }
            if (gamepad2.y) {
                // servo up
                if (servo_position > 0.65) {
                    servo_position = servo_position - 0.1; // This one too.
                    servo.setPosition(servo_position);
                    sleep(50);
                    idle();
                }

            }
            
            // lower arm servo / claw
            if (gamepad2.a) {
                servo2.setPosition(-0.2);        // Between the upper servo and the lower servo, the upper servo moves smoothly
            }                                    // But not the lower servo.
            if (gamepad2.b) {
                servo2.setPosition(0.08);
            }

            // Lnear actuator
            if (gamepad1.right_bumper) {                            // something intresting about the linear servo is that                                                               
            if (servo2_position < 0.60) {                           // the position of the servo needs to be moved actively
                servo2_position = servo2_position + 0.10;           // unlike the regular servos which move to position immediately.
                    lservo.setPosition(servo2_position);
                    sleep(50);
                    idle();
                }
            }
            if (gamepad1.left_bumper) {
                //lservo.setPosition(0.0);
            if (servo2_position > 0.0) {
                servo2_position = servo2_position - 0.10;
                    lservo.setPosition(servo2_position);
                    sleep(50);
                    idle();
                }
            }
            
            /*
            // Extension
            while (gamepad2.a) {
                extension.setPower(0.8);
            }
            while (gamepad2.b) {
                extension.setPower(-0.4);                                           // I'll egnore this. // For now.
            }*/
            extension.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
            
            extension.setPower(0);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
        }

    }
    
}

