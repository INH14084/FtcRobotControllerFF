package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Good Autonomous", group = "robot", preselectTeleOp = "GoodAutonomous.java")

public class good_autonomous extends LinearOpMode {
    HardwareMap robot = new HardwareMap();
    ElapsedTime timer = new ElapsedTime();


    @Override
    public void runOpMode() {
        robot.initialize(hardwareMap);

        robot.clawServo.setPosition(1);

        telemetry.addData("Status","Initialized");
        telemetry.addData("Action", "Press Play to Start");
        telemetry.update();

        waitForStart();

        timer.reset();

        while (opModeIsActive() && (timer.milliseconds() < 5000)) {
            telemetry.addData("Timer", timer.milliseconds());
            telemetry.update();
        }
        robot.clawServo.setPosition(-1);

       timer.reset();
       while (opModeIsActive() && (timer.milliseconds() < 500)) {

           telemetry.addData("Operation", "Grab");
           telemetry.update();
        }

        robot.frontRightMotor.setPower(0);
        robot.frontLeftMotor.setPower(0);
        robot.backRightMotor.setPower(0);
        robot.backLeftMotor.setPower(0);
        robot.clawArm.setPower(.4);
        robot.slidePull.setPower(.4);

        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 500)) {

            telemetry.addData("Operation", "arming");
            telemetry.update();
        }

        robot.frontRightMotor.setPower(.5);
        robot.frontLeftMotor.setPower(-.5);
        robot.backRightMotor.setPower(-.5);
        robot.backLeftMotor.setPower(.5);


        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 2300)) {

            telemetry.addData("Operation", "Gaffing Left");
            telemetry.update();
        }


        robot.frontRightMotor.setPower(.5);
        robot.frontLeftMotor.setPower(.5);
        robot.backRightMotor.setPower(.5);
        robot.backLeftMotor.setPower(.5);


        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 2300)) {

            telemetry.addData("Operation", "Gaffing");
            telemetry.update();
        }

        //note, this covers roughly 2.8 feet in 2.7 seconds with current weight and batteries close to full charge
        //not exact measurement, but close enough to build autonomous using these numbers
        //however there is not enough info for strafe calculations

        robot.frontRightMotor.setPower(-.5);
        robot.frontLeftMotor.setPower(.5);
        robot.backRightMotor.setPower(.5);
        robot.backLeftMotor.setPower(-.5);


        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 2300)) {

            telemetry.addData("Operation", "Gaffing Right");
            telemetry.update();
        }

        robot.clawServo.setPosition(-1);

        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 500)) {

            telemetry.addData("Operation", "unclawing");
            telemetry.update();
        }

        robot.frontRightMotor.setPower(-.5);
        robot.frontLeftMotor.setPower(.5);
        robot.backRightMotor.setPower(.5);
        robot.backLeftMotor.setPower(-.5);


        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 2300)) {

            telemetry.addData("Operation", "Gaffing Right To hub");
            telemetry.update();
        }

        robot.frontRightMotor.setPower(0);
        robot.frontLeftMotor.setPower(0);
        robot.backRightMotor.setPower(0);
        robot.backLeftMotor.setPower(0);
        robot.clawArm.setPower(-.4);
        robot.slidePull.setPower(-.4);
                
        timer.reset();
        while (opModeIsActive() && (timer.milliseconds() < 500)) {

            telemetry.addData("Operation", "unarming");
            telemetry.update();
        }

    }
}
