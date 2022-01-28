package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="FF_TeleOp_1", group="robot")

public class FFTeleop1 extends OpMode {
    HardwareMap robot = new HardwareMap();

//    double spinDirection;

    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;

    double clawPower;
    double spinPower;
    double liftPower;

//    private boolean oldLeftBumper;
//    private boolean oldRightBumper;

    @Override
    public void init() {
        robot.initialize(hardwareMap);

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        robot.clawServo.setPosition(.75);

    }

    @Override
    public void init_loop() {

        /*
        //Right dpad is pressed to have motor spin correctly for red side
        //Left dpad is press to have motor spin correctly for blue side
        boolean newLeftBumper = gamepad2.dpad_left;
        boolean newRightBumper = gamepad2.dpad_right;

        /*Latch*/
        /*
        if (newLeftBumper && !oldLeftBumper) {
            spinDirection = 1;
        } else if (newRightBumper && !oldRightBumper) {
            spinDirection = -1;
        }

        oldLeftBumper = newLeftBumper;
        oldRightBumper = newRightBumper;

        if (spinDirection == -1) {
            telemetry.addData("Spin Direction", "Blue Side");
            telemetry.addData("Press GP2 DPadLeft", "to switch to Red Side");
        } else {
            telemetry.addData("Spin Direction", "Red Side");
            telemetry.addData("Press GP2 DPadRight", "to switch to Blue Side");
        }
        */

        telemetry.addData("Status", "Awaiting User Input");
        telemetry.addData("Action", "Press Play to Start");
        telemetry.update();
    }

    @Override
    public void start() {
        telemetry.addData("Status", "Starting");
        telemetry.update();

    }

    @SuppressWarnings("DanglingJavadoc")
    @Override
    public void loop() {
        //latch();

        /**Display Carousel Direction*/
        /*
        {
            if (spinDirection == 1) {
                telemetry.addData("Spin Direction", "Red Side");
            } else {
                telemetry.addData("Spin Direction", "Blue Side");
            }
        }
         */

        /**Arm Movement*/{
            clawPower = gamepad2.left_stick_y * 0.4;
            liftPower = gamepad2.right_stick_y * 0.4;
        }

        /**Carousel Movement*/{
            spinPower = gamepad2.right_trigger;
        }

        /**Servo Grabber Movement*/{
            if (gamepad2.dpad_up) {
                robot.clawServo.setPosition(.75);
            } else if (gamepad2.dpad_down) {
                robot.clawServo.setPosition(0);
            }
        }
//Todo test code
        /**Barrier Servo Controls*/ {
            if((gamepad1.left_stick_y * gamepad1.right_stick_y)>0) {
                if(-gamepad1.left_stick_y > 0.2){
                    robot.rightTorque.setPosition(0);
                    robot.leftTorque.setPosition(1);
                } else if (-gamepad1.left_stick_y < -0.2) {
                    robot.rightTorque.setPosition(1);
                    robot.leftTorque.setPosition(0);
                } else {
                    robot.rightTorque.setPosition(.5);
                    robot.leftTorque.setPosition(.5);
                }


            }
        }

        /**Drive Controls*/ {
            if (gamepad1.left_bumper) {

                //Slow Tank Mode
                if (gamepad1.right_bumper) {
                    frontLeftPower = -gamepad1.left_stick_y * .5;
                    frontRightPower = -gamepad1.right_stick_y * .5;
                    backLeftPower = -gamepad1.left_stick_y * .5;
                    backRightPower = -gamepad1.right_stick_y * .5;

                    telemetry.addData("Drive Mode", "Slow Tank Drive");

                    //Standard Tank Mode
                } else {
                    frontLeftPower = -gamepad1.left_stick_y;
                    frontRightPower = -gamepad1.right_stick_y;
                    backLeftPower = -gamepad1.left_stick_y;
                    backRightPower = -gamepad1.right_stick_y;

                    telemetry.addData("Drive Mode", "Tank Drive");

                }

                //Slow Mecanum Drive
            } else if (gamepad1.right_bumper) {
                frontLeftPower = -gamepad1.left_stick_y * 0.5;
                frontRightPower = -gamepad1.right_stick_y * 0.5;
                backLeftPower = -gamepad1.right_stick_y * 0.5;
                backRightPower = -gamepad1.left_stick_y * 0.5;

                telemetry.addData("Drive Mode", "Slow Mecanum Drive");

                //Standard Mecanum Drive
            } else {
                frontLeftPower = -gamepad1.left_stick_y;
                frontRightPower = -gamepad1.right_stick_y;
                backLeftPower = -gamepad1.right_stick_y;
                backRightPower = -gamepad1.left_stick_y;

                telemetry.addData("Drive Mode", "Mecanum Drive");

            }
        }

        /**Motor Control*/{
        robot.frontLeftMotor.setPower(frontLeftPower);
        robot.frontRightMotor.setPower(frontRightPower);
        robot.backLeftMotor.setPower(backLeftPower);
        robot.backRightMotor.setPower(backRightPower);

        robot.clawArm.setPower(clawPower);
        robot.slidePull.setPower(liftPower);

        //robot.spin.setPower(spinPower * spinDirection);
        robot.spin.setPower(gamepad2.right_trigger - gamepad2.left_trigger);

        robot.testMotor.setPower(clawPower);
        }

        /**Telemetry Status*/{
            telemetry.addData("Status", "Driver Controlled");
            telemetry.addData("Action", "Press Stop When Finished");
            telemetry.update();
        }
    }
/*
    public void latch() {
        boolean newLeftBumper = gamepad2.dpad_left;
        boolean newRightBumper = gamepad2.dpad_right;

        if (newLeftBumper && !oldLeftBumper) {
            spinDirection = 1;
        } else if (newRightBumper && !oldRightBumper) {
            spinDirection = -1;
        }

        oldLeftBumper = newLeftBumper;
        oldRightBumper = newRightBumper;
    }

 */
}
