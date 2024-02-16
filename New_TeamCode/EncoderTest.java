package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.Collection;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.Range; 
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class EncoderTest {

    private ElapsedTime runtime = new ElapsedTime();
    
    private DcMotorEx leftFrontDrive;
    private DcMotorEx leftBackDrive;
    private DcMotorEx rightFrontDrive;
    private DcMotorEx rightBackDrive;
    private Servo Arm;
    private Servo Claw;
    private Servo Linear;
    
    HardwareMap hardwareMap;
    Telemetry telemetry;
    Gamepad GamePad1;
    Gamepad GamePad2;
    public final Gyro gyro             = new Gyro();
    public double target               = 0;
    
    public EncoderTest() {

    }
    
    public void init(HardwareMap hwmap, Telemetry t) {
        hardwareMap = hwmap;
        telemetry = t;
        gyro.init_gyro(hardwareMap);
    
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, "left_front_drive");
        leftBackDrive  = hardwareMap.get(DcMotorEx.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "right_back_drive");
        Arm = hardwareMap.get(Servo.class, "servo");
        Claw = hardwareMap.get(Servo.class, "servo2");
        Linear = hardwareMap.get(Servo.class, "linear");
        
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        
        leftFrontDrive.setTargetPositionTolerance(100);
        leftBackDrive.setTargetPositionTolerance(100);
        rightFrontDrive.setTargetPositionTolerance(100);
        rightBackDrive.setTargetPositionTolerance(100);
        
        leftFrontDrive .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive  .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
    }
    
    public void teleOpMode() {
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    
    public void AutonMode() {
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
        
        
    public void rotate(double angle) {
        teleOpMode();
        target = angle;
        if (angle > gyro.get_heading()){
            rotate_right(angle);
        }
        else if (angle < gyro.get_heading()) {
            rotate_left(angle);
        }
        AutonMode();
    }
    private void rotate_right(double angle) {
        while (gyro.get_heading() <= angle) {
            double power = (Math.abs((gyro.get_heading() - angle) / 100) + 0.01);
            if (power > .5) {
                power = .25;
            }
            if (power < .1) {
                power = .1;
            }
            rightFrontDrive.setPower(power);
            leftFrontDrive.setPower(-power);
            rightBackDrive.setPower(power);
            leftBackDrive.setPower(-power);
            telemetry.addData("heading", gyro.get_heading());
            telemetry.update();
        }
        rightFrontDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
        leftBackDrive.setPower(0);
    }
    private void rotate_left(double angle){
        while (gyro.get_heading() >= angle) {
            double power = (Math.abs((gyro.get_heading() - angle) / 100) + 0.01);
            if (power > .25) {
                power = .25;
            }
            if (power < .2) {
                power = .2;
            }
            rightFrontDrive.setPower(-power);
            leftFrontDrive.setPower(power);
            rightBackDrive.setPower(-power);
            leftBackDrive.setPower(power);
            telemetry.addData("heading", gyro.get_heading());
            telemetry.update();
        }
        rightFrontDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
        leftBackDrive.setPower(0);
    }

    public void drive(List<Integer> values, double original_power, boolean accelerating, double time_out) {
        runtime.reset();
        List<DcMotor> motors = Collections.unmodifiableList(
                Arrays.asList(leftFrontDrive,
                        leftBackDrive,
                        rightFrontDrive,
                        rightBackDrive));
        for (int i = 0; i < motors.size(); i++) {
            DcMotor motor = motors.get(i);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(values.get(i));
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (!accelerating) {
                motor.setPower(original_power);
            }
            else {
                motor.setPower(0.1);
            }
        }
        while (motors_running(motors) && runtime.seconds() < time_out){
            double absolute = gyro.get_heading();
            if (accelerating) {
                for (int i = 0; i < motors.size(); i++) {
                    DcMotor motor = motors.get(i);
                    telemetry.addData(motor.getDeviceName(), motor.getCurrentPosition());
                    float value = values.get(i);
                    // float current = motor.getCurrentPosition();

                    double new_power;
                    if (value < 0) {
                        if (i % 2 == 0) {
                            new_power = original_power + (((absolute - target) / 100));
                        }
                        else {
                            new_power = original_power - (((absolute - target) / 100));
                        }

                    }
                    else {
                        if (i % 2 == 0) {
                            new_power = original_power - (((absolute - target) / 100));
                        }
                        else {
                            new_power = original_power + (((absolute - target) / 100));
                        }
                    }
                    motor.setPower(new_power);

                    //
                }
            }
            telemetry.update();
        }
        for (DcMotor motor : motors) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }
    
    public List<Integer> val(int v1, int v2, int v3, int v4) {
        return Collections.unmodifiableList(Arrays.asList(v1, v2, v3, v4));
    }

    public boolean motors_running(List<DcMotor> motors) {
        boolean running = false;
        for (DcMotor motor : motors) {
            telemetry.addData(motor.getDeviceName(), motor.getCurrentPosition());
            if (motor.isBusy()) {
                running = true;
            }
        }
        return running;
    }
}
