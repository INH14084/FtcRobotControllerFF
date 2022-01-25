package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="TeleOp Test", group="robot")
@Disabled

public class TeleOp_Test extends OpMode {
    HardwareMap robot = new HardwareMap();

    double spinDirection;
    private boolean oldLeftBumper;
    private boolean oldRightBumper;

    boolean aPress;
    boolean wait;




    @Override
    public void init() {
        robot.initialize(hardwareMap);

        aPress = false;

        wait = true;

        robot.clawServo.setPosition(0);

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        robot.testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.testMotor.setTargetPosition(0);

        robot.testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.testMotor.setPower(0);



        robot.slidePull.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.slidePull.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.slidePull.setTargetPosition(0);

        robot.slidePull.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.slidePull.setPower(0);

    }

    @Override
    public void init_loop() {
        //Right bumper is pressed to have motor spin correctly for red side
        //Left bumper is press to have motor spin correctly for blue side
        boolean newLeftBumper = gamepad1.left_bumper;
        boolean newRightBumper = gamepad1.right_bumper;

        if (newLeftBumper && !oldLeftBumper) {
            spinDirection = 1;
        } else if (newRightBumper && !oldRightBumper) {
            spinDirection = -1;
        }
        oldLeftBumper = newLeftBumper;
        oldRightBumper = newRightBumper;

        if (spinDirection == -1) {
            telemetry.addData("Spin Direction", "Red Side");
            telemetry.addData("Press GP1 LeftBumper", "to switch to Blue Side");
        } else {
            telemetry.addData("Spin Direction", "Blue Side");
            telemetry.addData("Press GP1 RightBumper", "to switch to Red Side");
        }

        telemetry.addData("Status", "Awaiting User Input");
        telemetry.addData("Action", "Press Play to Start");
        telemetry.update();
    }

    @Override
    public void start() {
        telemetry.addData("Status", "Starting");
        telemetry.update();

    }

    @Override
    public void loop() {
        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;

        double clawPower;
        clawPower = gamepad2.left_stick_y * 0.4;

        double spinPower;
        spinPower = gamepad2.right_trigger * 0.1;

        double liftPower;
        liftPower = gamepad2.right_stick_y * 0.4;


        if(gamepad1.left_bumper) {
            if(gamepad1.right_bumper) {
                frontLeftPower                 = -gamepad1.left_stick_y * .5;
                frontRightPower                = -gamepad1.right_stick_y * .5;
                backLeftPower                  = -gamepad1.left_stick_y * .5;
                backRightPower                 = -gamepad1.right_stick_y * .5;

                telemetry.addData("Drive Mode", "Slow Tank");

            } else {
                frontLeftPower                 = -gamepad1.left_stick_y;
                frontRightPower                = -gamepad1.right_stick_y;
                backLeftPower                  = -gamepad1.left_stick_y;
                backRightPower                 = -gamepad1.right_stick_y;

                telemetry.addData("Drive Mode", "Tank Drive");

            }
        } else if(gamepad1.right_bumper) {
            frontLeftPower                     = -gamepad1.left_stick_y * 0.5;
            frontRightPower                    = -gamepad1.right_stick_y * 0.5;
            backLeftPower                      = -gamepad1.right_stick_y * 0.5;
            backRightPower                     = -gamepad1.left_stick_y * 0.5;

            telemetry.addData("Drive Mode"    , "Sloth Drive");

        } else {
            frontLeftPower                     = -gamepad1.left_stick_y;
            frontRightPower                    = -gamepad1.right_stick_y;
            backLeftPower                      = -gamepad1.right_stick_y;
            backRightPower                     = -gamepad1.left_stick_y;

            telemetry.addData("Drive Mode", "Bean Drive");

        }

        /*

        if(gamepad2.a) {
            //telemetry.addData("Lemon", "true");
            robot.clawServo.setPosition(.55);
        }

        if(gamepad2.b) {
            //telemetry.addData("Lemon", "false");
            robot.clawServo.setPosition(0);
        }

        */

        if (gamepad2.a) {
            clawGrab();
        }

        robot.frontLeftMotor.setPower(frontLeftPower);
        robot.frontRightMotor.setPower(frontRightPower);
        robot.backLeftMotor.setPower(backLeftPower);
        robot.backRightMotor.setPower(backRightPower);

        robot.clawArm.setPower(clawPower);
        robot.slidePull.setPower(liftPower);

        robot.spin.setPower(spinPower * spinDirection);

        slideMove();
        robot.slidePull.setPower(0);

        armMove();
        robot.testMotor.setPower(0);

        if (spinDirection == -1) {
            telemetry.addData("Spin Direction", "Red Side");
        } else {
            telemetry.addData("Spin Direction", "Blue Side");
        }

        telemetry.addData("Status", "Driver Controlled");
        telemetry.addData("Action", "Press Stop When Finished");
        telemetry.update();


    }

    //Add PID TUNING https://www.youtube.com/watch?v=FDRWcK-orJs

    public void clawGrab() {

        if (aPress) {
            robot.clawServo.setPosition(.55);
            aPress = false;
        } else {
            robot.clawServo.setPosition(0);
            aPress = true;
        }


    }

    public void slideMove() {
        int positionSlide = robot.slidePull.getCurrentPosition();

        robot.slidePull.setPower(0);

        if (!robot.slidePull.isBusy()) {
            if (gamepad2.x && !(positionSlide < -1)) {
                positionSlide = positionSlide - 10;

            } else if (gamepad2.y && !(positionSlide > 2000)) {
                positionSlide = positionSlide + 10;
            }

            robot.slidePull.setTargetPosition(positionSlide);

            robot.slidePull.setPower(1);


        }

        telemetry.addData("Pully Position", robot.slidePull.getCurrentPosition());
        telemetry.addData("Pully Motor is Busy = ", robot.slidePull.isBusy());
    }

    public void armMove() {
        int positionArm = robot.testMotor.getCurrentPosition();

        robot.testMotor.setPower(0);

        if (!robot.testMotor.isBusy()) {
            if (gamepad2.dpad_down && !(positionArm < -51)) {
                positionArm = positionArm - 50;

            } else if (gamepad2.dpad_up && !(positionArm > 1000)) {
                positionArm = positionArm + 50;

            }
            robot.testMotor.setTargetPosition(positionArm);

            robot.testMotor.setPower(.25);


        }

        telemetry.addData("Arm Position", robot.testMotor.getCurrentPosition());
        telemetry.addData("Arm Motor is busy = ", robot.testMotor.isBusy());


    }



    @Override
    public void stop() {
        //TODO make sure all variables get set to 0 here so the robot actually stops
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }

}

