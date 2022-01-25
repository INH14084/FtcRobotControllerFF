package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "FFStrafeRight", group = "robot", preselectTeleOp = "FFTeleop1.java")
@Disabled


public class FFStrafeRight extends LinearOpMode {
    HardwareMap robot = new HardwareMap();
    ElapsedTime timer = new ElapsedTime();


    @Override
    public void runOpMode() {
        robot.initialize(hardwareMap);

        robot.clawServo.setPosition(0);

        telemetry.addData("Status","Initialized");
        telemetry.addData("Action", "Press Play to Start");
        telemetry.update();

        waitForStart();

        timer.reset();

        while (opModeIsActive() && (timer.milliseconds() < 5000)) {
            telemetry.addData("Timer", timer.milliseconds());
            telemetry.update();
        }

        robot.frontRightMotor.setPower(.5);
        robot.frontLeftMotor.setPower(.5);
        robot.backRightMotor.setPower(.5);
        robot.backLeftMotor.setPower(.5);


        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 5000)) {

            telemetry.addData("Operation", "Drive Forward");
            telemetry.update();
        }

        robot.frontRightMotor.setPower(0);
        robot.frontLeftMotor.setPower(0);
        robot.backRightMotor.setPower(0);
        robot.backLeftMotor.setPower(0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //strafe
        robot.frontRightMotor.setPower(0);
        robot.frontLeftMotor.setPower(0);
        robot.backRightMotor.setPower(0);
        robot.backLeftMotor.setPower(0);

        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 5000)) {
            telemetry.addData("Operation", "Strafe");
            telemetry.update();
        }
    }

}
