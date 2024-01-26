package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name="PowerPlay TeleOp", group="Android Studio")

public class BeskarTeleOp2024 extends LinearOpMode {
    BeskarRobot2024 robot = new BeskarRobot2024();
    @Override
    public void runOpMode() {
        robot.init(hardwareMap, telemetry, gamepad1, gamepad2);
        robot.teleOpMode();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        double x = 0.7;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.addData("Status", "Started");
        telemetry.update();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.left_stick_button) {
                x = 1;
            }
            if (!gamepad1.left_stick_button) {
                x = 0.7;
            }
            robot.listen_for_drive_commands(x);
            telemetry.update();
        }
    }
}
