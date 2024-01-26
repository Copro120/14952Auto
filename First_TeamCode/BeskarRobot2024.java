package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BeskarRobot2024 {
    public DcMotorEx front_right;
    public DcMotorEx front_left;
    public DcMotorEx back_right;
    public DcMotorEx back_left;
    HardwareMap hwMap;
    Telemetry telemetry;
    Gamepad GamePad1;
    Gamepad GamePad2;
    public final Gyro gyro             = new Gyro();
    public final ElapsedTime runtime   = new ElapsedTime();
    public double target               = 0;

    public BeskarRobot2024(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, Telemetry t, Gamepad g1, Gamepad g2) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        GamePad1 = g1;
        GamePad2 = g2;
        telemetry = t;

        gyro.init_gyro(hwMap);

        runtime.reset();

        // Define and Initialize Motors
        front_right    = hwMap.get(DcMotorEx.class,        "right_front_drive");
        front_left     = hwMap.get(DcMotorEx.class,        "left_front_drive");
        back_right     = hwMap.get(DcMotorEx.class,        "right_back_drive");
        back_left      = hwMap.get(DcMotorEx.class,        "left_back_drive");

        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);

        front_left .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left  .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        front_left.setTargetPositionTolerance(20);
        front_right.setTargetPositionTolerance(20);
        back_left.setTargetPositionTolerance(20);
        back_right.setTargetPositionTolerance(20);
    }

    // ====================
    // Section: Robot Modes
    // ====================

    public void teleOpMode() {
        front_left .setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left  .setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right .setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void autonMode() {
        front_left .setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_left  .setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_right .setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // ======================
    // Section: TeleOp Motion
    // ======================

    public void listen_for_drive_commands(double x) {
        double v = -GamePad1.left_stick_y;
        double h = GamePad1.left_stick_x;
        double p = GamePad1.right_stick_x / 2;

        double fr = (-p + (v - h) * x);
        double br = (-p + (v + h) * x);
        double fl = ( p + (v + h) * x);
        double bl = ( p + (v - h) * x);

        fr = Range.clip(fr, -1, 1);
        fl = Range.clip(fl, -1, 1);
        br = Range.clip(br, -1, 1);
        bl = Range.clip(bl, -1, 1);

        front_left .setPower(fl);
        back_left  .setPower(bl);
        front_right.setPower(fr);
        back_right .setPower(br);
    }

    // ==========================
    // Section: Autonomous Motion
    // ==========================

    public void rotate(double angle) {
        target = angle;
        if (angle > gyro.get_heading()){
            rotate_right(angle);
        }
        else if (angle < gyro.get_heading()) {
            rotate_left(angle);
        }
    }
    private void rotate_right(double angle) {
        teleOpMode();
        while (gyro.get_heading() <= angle) {
            double power = (Math.abs((gyro.get_heading() - angle) / 100) + 0.01);
            if (power > .5) {
                power = .5;
            }
            if (power < .15) {
                power = .15;
            }
            power += .1;
            front_right.setPower(power);
            front_left.setPower(-power);
            back_right.setPower(power);
            back_left.setPower(-power);
            telemetry.addData("heading", gyro.get_heading());
            telemetry.update();
        }
        front_right.setPower(0);
        front_left.setPower(0);
        back_right.setPower(0);
        back_left.setPower(0);
        autonMode();
    }
    private void rotate_left(double angle){
        teleOpMode();
        while (gyro.get_heading() >= angle) {
            double power = (Math.abs((gyro.get_heading() - angle) / 100) + 0.01);
            if (power > .5) {
                power = .5;
            }
            if (power < .15) {
                power = .15;
            }
            power += .1;
            front_right.setPower(-power);
            front_left.setPower(power);
            back_right.setPower(-power);
            back_left.setPower(power);
            telemetry.addData("heading", gyro.get_heading());
            telemetry.update();
        }
        front_right.setPower(0);
        front_left.setPower(0);
        back_right.setPower(0);
        back_left.setPower(0);
        autonMode();
    }

    public void drive(List<Integer> values, double original_power, boolean accelerating, double time_out) {
        runtime.reset();
        List<DcMotor> motors = Collections.unmodifiableList(
                Arrays.asList(front_right,
                        front_left,
                        back_right,
                        back_left));
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
            if (motor.isBusy()) {
                running = true;
            }
        }
        return running;
    }
    public List<Double> power_val(double v1, double v2, double v3, double v4) {
        return Collections.unmodifiableList(Arrays.asList(v1, v2, v3, v4));
    }
}

