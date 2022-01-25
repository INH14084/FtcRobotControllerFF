package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp
@Disabled

public class RunToPosition_Test extends OpMode {
    HardwareMap robot = new HardwareMap();

    int position;
    int requestPosition;

    @Override
    public void init() {
        robot.initialize(hardwareMap);

        robot.testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.testMotor.setTargetPosition(0);

        robot.testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.testMotor.setPower(0);


    }

    @Override
    public void loop() {
        requestPosition = Math.round(gamepad1.right_stick_y) * 150;

        armMove();

        robot.testMotor.setPower(0);



    }

    public void armMove() {
        position = robot.testMotor.getCurrentPosition();

        robot.testMotor.setPower(0);

        /*
        if (!robot.spin.isBusy()) {
            if (gamepad1.dpad_down && !(position < -80)) {
                position = position - 40;

            } else if (gamepad1.dpad_up && !(position > 1000)) {
                position = position + 40;

            }
        */

         if (!robot.testMotor.isBusy()) {
             if (!(position < -80) && !(position > 680)) {
                 position = position + requestPosition;
             } else if (position < -80) {
                 position = -70;
             } else {
                 position = 650;
             }
         }

            robot.testMotor.setTargetPosition(position);

            robot.testMotor.setPower(1);

            telemetry.addData("Target Position", position);
            telemetry.addData("Actual Position", robot.testMotor.getCurrentPosition());
            telemetry.addData("Motor is busy = ", robot.testMotor.isBusy());
            telemetry.update();

        }



    }
