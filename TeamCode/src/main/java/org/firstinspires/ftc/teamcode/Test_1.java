package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Test_1", group="robot")
@Disabled

public class Test_1 extends OpMode {
    HardwareMap robot = new HardwareMap();

//TODO Tune PID Coefficients


    public static PIDCoefficients pidCoeffs = new PIDCoefficients(0,0,0);
    public PIDCoefficients pidGains = new PIDCoefficients(0,0,0);

    ElapsedTime PIDTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);


    @Override
    public void init() {
        robot.initialize(hardwareMap);
        telemetry.addData("Status", "Initializing");
        telemetry.update();

    }

    @Override
    public void init_loop() {
        telemetry.addData("Right Bumper", gamepad1.right_bumper);
        telemetry.update();
    }

    @Override
    public void start() {
        telemetry.addData("Status", "Start");
        telemetry.update();

    }

    @Override
    public void loop() {
        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;

        //double clawPower;
        //clawPower = gamepad2.left_stick_y * 0.4;
//------------------
//TODO Test PID Code
//------------------
        double speed = gamepad2.left_stick_y * 0.4;
        telemetry.addData("Speed", speed);
        PID(speed);
//------------------
//
//------------------
        double spinPower;
        spinPower = gamepad2.right_trigger * 1;

        double liftPower;
        liftPower = gamepad2.right_stick_y * 0.4;


        if(gamepad1.left_bumper) {
            if(gamepad1.right_bumper) {
                frontLeftPower                 = -gamepad1.left_stick_y * 0.05;
                frontRightPower                = -gamepad1.right_stick_y * 0.05;
                backLeftPower                  = -gamepad1.left_stick_y * 0.05;
                backRightPower                 = -gamepad1.right_stick_y * 0.05;

                telemetry.addData("Drive Mode", "Slow Tank");

            } else {
                frontLeftPower                 = -gamepad1.left_stick_y * 0.1;
                frontRightPower                = -gamepad1.right_stick_y * 0.1;
                backLeftPower                  = -gamepad1.left_stick_y * 0.1;
                backRightPower                 = -gamepad1.right_stick_y * 0.1;

                telemetry.addData("Drive Mode", "Tank Drive");

            }
        } else if(gamepad1.right_bumper) {
            frontLeftPower                     = -gamepad1.left_stick_y * 0.05;
            frontRightPower                    = -gamepad1.right_stick_y * 0.05;
            backLeftPower                      = -gamepad1.right_stick_y * 0.05;
            backRightPower                     = -gamepad1.left_stick_y * 0.05;

            telemetry.addData("Drive Mode"    , "Sloth Drive");

        } else {
            frontLeftPower                     = -gamepad1.left_stick_y * 0.1;
            frontRightPower                    = -gamepad1.right_stick_y * 0.1;
            backLeftPower                      = -gamepad1.right_stick_y * 0.1;
            backRightPower                     = -gamepad1.left_stick_y * 0.1;

            telemetry.addData("Drive Mode", "Bean Drive");

        }

        if(gamepad2.dpad_up) {
            //telemetry.addData("Lemon", "true");
            robot.clawServo.setPosition(.5);
        }

        if(gamepad2.dpad_down) {
            //telemetry.addData("Lemon", "false");
            robot.clawServo.setPosition(0);
        }

        robot.frontLeftMotor.setPower(frontLeftPower);
        robot.frontRightMotor.setPower(frontRightPower);
        robot.backLeftMotor.setPower(backLeftPower);
        robot.backRightMotor.setPower(backRightPower);

        //robot.clawArm.setPower(clawPower); TODO May need removed
        robot.slidePull.setPower(liftPower);

        robot.spin.setPower(spinPower);

        telemetry.addData("Status", "Driver Controlled");
        telemetry.addData("Action", "Press Stop When Finished");
        telemetry.update();


    }

    //Add PID TUNING https://www.youtube.com/watch?v=FDRWcK-orJs

    //-----------------
    //PID TUNING CODE
    //-----------------

    //Coefficients need tuned

    double integral = 0;
    double lastError = 0;

    public void PID(double targetVelocity) {

        PIDTimer.reset();

        double currentVelocity = robot.clawArm.getVelocity();

        double error = targetVelocity - currentVelocity;

        integral += error + PIDTimer.time();

        double deltaError = error - lastError;
        double derivative = deltaError / PIDTimer.time();

        pidGains.p = pidCoeffs.p * error;
        pidGains.i = pidCoeffs.i * integral;
        pidGains.d = pidCoeffs.d * derivative;
        robot.clawArm.setVelocity(pidGains.p + pidGains.i + pidGains.d + targetVelocity);


    }


    @Override
    public void stop() {

    }

}

