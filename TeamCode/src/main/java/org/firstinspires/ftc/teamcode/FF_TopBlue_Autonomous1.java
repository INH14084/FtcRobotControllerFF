package org.firstinspires.ftc.teamcode;

//import statements added automatically

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

/* Plan
* Start In front of barcode farthest from spectators blue side
* use tensor flow to check where the duck/TSE is
* record location
* dont touch duck/TeamScoringElement
* place preload on correct level based on recorded location
*   requires using encoders on both pulley and clawarm motors + imu
* ?spin carousel?
* option to collect freight
*   requires going fast + potentially imu
* navigate to either alliance storage or warehouse - potentially using vuforia
* park completely
* enter teleop
* */

@Autonomous(name = "Top Blue Autonomous" , group = "robot", preselectTeleOp = "TeleOp_Test.java")
public class FF_TopBlue_Autonomous1 extends LinearOpMode {
    //Define all variables
    HardwareMap robot = new HardwareMap();

    boolean continueLoop = true;

    BNO055IMU imu;

    Orientation angles;
    Acceleration gravity;

    @Override
    //Run Op Mode loop
    public void runOpMode() {
        //Initialize Robot
        robot.initialize(hardwareMap);


        //Init Vuforia + TensorFlow


        //Init IMU

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample OPMode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "IMU");
        imu.initialize(parameters);

        composeTelemetry();



        //Add Telemetry text to Driver Station Screen
        telemetry.addData("Status","Initialized");
        telemetry.addData("Action", "Press Play to Start");
        telemetry.update();

        //Wait for user to push start
        waitForStart();

        //Run Autonomous code

        //Detect and Record Duck/TSE Position

        //Deliver preload freight

        //Detect Location using Vuforia

        //Spin Carousel

        //Park


    }

    //Add any other necessary loops here i.e. IMU init, vuforia init, etc.


        //IMU init loop
    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(() -> {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        });

        telemetry.addLine()
                .addData("status", () -> imu.getSystemStatus().toShortString())
                .addData("calibrated", () -> imu.getCalibrationStatus().toString());

        telemetry.addLine()
                .addData("heading", () -> formatAngle(angles.angleUnit, angles.firstAngle))
                .addData("roll", () -> formatAngle(angles.angleUnit, angles.secondAngle))
                .addData("pitch", () -> formatAngle(angles.angleUnit, angles.thirdAngle));

        telemetry.addLine()
                .addData("gravity", () -> gravity.toString())
                .addData("mag", () -> String.format(Locale.getDefault(), "%.3f",
                        Math.sqrt(gravity.xAccel*gravity.xAccel
                                + gravity.yAccel*gravity.yAccel
                                + gravity.zAccel*gravity.zAccel)));
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }


    //Closing Bracket

}
