package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Encoder Test", group="Autonomous")

public class EncoderTest extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareMap robot   = new HardwareMap();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.initialize(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.clawArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.clawArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.clawServo.setPosition(.75);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.clawArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.clawArm.setTargetPosition(-1000);
        robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.clawArm.setPower(1);

        sleep(3000);

        robot.clawArm.setPower(0);


        sleep(1000);

        robot.clawArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.clawArm.setTargetPosition(350);
        robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.clawArm.setPower(1);

        sleep(3000);

        robot.clawServo.setPosition(0);

        robot.clawArm.setPower(0);

        // Step through each leg of the path,
    }
}
