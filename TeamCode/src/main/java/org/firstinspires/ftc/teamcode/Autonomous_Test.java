package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

@Autonomous(name = "Autonomous Test" , group = "robot", preselectTeleOp = "TeleOp_Test.java")
@Disabled

//IMU code works


public class Autonomous_Test extends LinearOpMode {
    HardwareMap robot   = new HardwareMap();
    ElapsedTime runtime = new ElapsedTime();
    ElapsedTime timer   = new ElapsedTime();

    boolean continueLoop = true;


    BNO055IMU imu;

    Orientation angles;
    Acceleration gravity;

    @Override
    public void runOpMode() {
        robot.initialize(hardwareMap);

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

        telemetry.addData("Status","Initialized");
        telemetry.addData("Action", "Press Play to Start");
        telemetry.update();

        waitForStart();

        runtime.reset();
        timer.reset();

        while (opModeIsActive() && (timer.milliseconds() < 3000)) {
            telemetry.addData("Timer", timer.milliseconds());
            telemetry.update();


        }

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        while (opModeIsActive() && (continueLoop)) {
            if (angles.firstAngle > 95) {
                telemetry.addData("Angle", "Too Big");
            } else if (angles.firstAngle < 85) {
                telemetry.addData("Angle", "Too Small");
            } else {
                continueLoop = false;
            }




            telemetry.update();
        }

        continueLoop = true;

        timer.reset();

        while (opModeIsActive() && (timer.milliseconds() < 3000)) {
            telemetry.addData("Angle", "Correct");
            telemetry.update();
        }


    }





    //----------------------
    //IMU Telemetry Updates
    //----------------------


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

}
