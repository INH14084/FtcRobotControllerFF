package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.Tfod;

import java.util.List;
import java.util.Locale;

@SuppressWarnings({"DanglingJavadoc", "IntegerDivisionInFloatingPointContext", "FieldCanBeLocal"})
@Autonomous(name= "Blue1")
@Disabled

public class Blue1 extends LinearOpMode {
    HardwareMap robot = new HardwareMap();
    ElapsedTime runtime = new ElapsedTime();
    ElapsedTime timer = new ElapsedTime();
    private VuforiaCurrentGame vuforiaFreightFrenzy;
    private Tfod tfod;
    private DistanceSensor sensorRange;

    int hubLevel;
    double startLocation;
    int extraTime;

    double strafePower = -1;

    boolean continueLoop = true;


    BNO055IMU imu;

    Orientation angles;
    Acceleration gravity;


    Recognition recognition;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        telemetry.addData("Action","Set Robot Location");
        telemetry.addData("Hold A for Top", "Hold B for Bottom");
        telemetry.update();

        sleep(5000);

        if(gamepad1.a) {
            startLocation = -1;
            telemetry.addData("Location Set","Top");
        } else {
            startLocation = 1;
            telemetry.addData("Location Set","Bottom");
        }
        telemetry.update();


        robot.clawServo.setPosition(.75);
        sleep(1000);



        List<Recognition> recognitions;
        int index;
        robot.initialize(hardwareMap);
        vuforiaFreightFrenzy = new VuforiaCurrentGame();
        tfod = new Tfod();

        telemetry.addData("Status", "Initializing IMU");
        telemetry.update();

        /** Imu Init */
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

        telemetry.addData("IMU", "Initialized");
        telemetry.addData("Status", "Beginning Vuforia Init");
        telemetry.update();

        /**Initialize Vuforia.*/
        vuforiaFreightFrenzy.initialize(
                "", // vuforiaLicenseKey
                hardwareMap.get(WebcamName.class, "Webcam 1"), // cameraName
                "", // webcamCalibrationFilename
                false, // useExtendedTracking
                false, // enableCameraMonitoring
                VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE, // cameraMonitorFeedback
                0, // dx
                0, // dy
                0, // dz
                AxesOrder.XZY, // axesOrder
                90, // firstAngle
                90, // secondAngle
                0, // thirdAngle
                true); // useCompetitionFieldTargetLocations
        tfod.useDefaultModel();
        // Set min confidence threshold to 0.7
        tfod.initialize(vuforiaFreightFrenzy, (float) 0.7, true, true);
        // Initialize TFOD before waitForStart.
        // Activate TFOD here so the object detection labels are visible
        // in the Camera Stream preview window on the Driver Station.
        tfod.activate();
        // Enable following block to zoom in on target.
        tfod.setZoom(1, 16 / 9);

        telemetry.addData("Vuforia", "Initialized");
        telemetry.addData("Status", "Distance Sensor Init");
        telemetry.update();

        sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");

        telemetry.addData("Status", "Robot Initialized");
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        // Wait for start command from Driver Station.
        waitForStart();

        if (opModeIsActive()) {
            // Put run blocks here.
            timer.reset();
            while ((opModeIsActive())&&(timer.milliseconds() < 3000)) {
                // Put loop blocks here.
                // Get a list of recognitions from TFOD.
                recognitions = tfod.getRecognitions();
                // If list is empty, inform the user. Otherwise, go
                // through list and display info for each recognition.
                if (recognitions.size() == 0) {
                    telemetry.addData("TFOD", "No items detected.");
                } else {
                    index = 0;
                    // Iterate through list and call a function to
                    // display info for each recognized object.
                    for (Recognition recognition_item : recognitions) {
                        recognition = recognition_item;
                        // Display info.
                        displayInfo(index);
                        // Increment index.
                        index = index + 1;
                    }
                }
                telemetry.addData("Hub Level", hubLevel);
                telemetry.update();
            } /** check recognitions*/


            /**Strafe to Position*/
            timer.reset();

            while ((opModeIsActive())&&(timer.milliseconds()<1000)) {
                robot.frontRightMotor.setPower(strafePower * startLocation);
                robot.frontLeftMotor.setPower(-strafePower * startLocation);
                robot.backRightMotor.setPower(-strafePower * startLocation);
                robot.backLeftMotor.setPower(strafePower * startLocation);

            }

            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);

            sleep(500);

            /**Position Claw*/

            robot.clawArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.clawArm.setTargetPosition(-1000);
            robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.clawArm.setPower(1);
            sleep(500);

            robot.clawArm.setPower(0);
            sleep(250);

            robot.clawArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.clawArm.setTargetPosition(hubLevel);
            robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.clawArm.setPower(1);
            sleep(500);

            robot.clawArm.setPower(0);

            sleep(250);

            //Put Cube in Hub
            robot.frontRightMotor.setPower(.25);
            robot.frontLeftMotor.setPower(.25);
            robot.backRightMotor.setPower(.25);
            robot.backLeftMotor.setPower(.25);

            sleep(750 + extraTime);

            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            sleep(250);

            //Deposit Cube
            robot.clawServo.setPosition(0);

            sleep(1000);

            //Exit Hub
            robot.frontRightMotor.setPower(-.25);
            robot.frontLeftMotor.setPower(-.25);
            robot.backRightMotor.setPower(-.25);
            robot.backLeftMotor.setPower(-.25);

            sleep(750 + extraTime);

            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            sleep(250);

            //Raise Arm
            robot.clawArm.setTargetPosition(800);
            robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.clawArm.setPower(1);
            sleep(500);

            robot.clawArm.setPower(0);

            sleep(1000);
            /**Rotate*/

            imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);



            while (opModeIsActive() && (continueLoop)) {
                if (angles.firstAngle > 95) {

                    telemetry.addData("Angle", "Too Big");
                    robot.frontRightMotor.setPower(.5);
                    robot.frontLeftMotor.setPower(.5);
                    robot.backRightMotor.setPower(.5);
                    robot.backLeftMotor.setPower(.5);

                } else if (angles.firstAngle < 85) {

                    telemetry.addData("Angle", "Too Small");
                    robot.frontRightMotor.setPower(-.5);
                    robot.frontLeftMotor.setPower(-.5);
                    robot.backRightMotor.setPower(-.5);
                    robot.backLeftMotor.setPower(-.5);

                } else {
                    continueLoop = false;
                    robot.frontRightMotor.setPower(0);
                    robot.frontLeftMotor.setPower(0);
                    robot.backRightMotor.setPower(0);
                    robot.backLeftMotor.setPower(0);
                }
            }

            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            continueLoop = true;

            sleep(500);

            /**Drive to Carousel*/
            while ((opModeIsActive()) && (continueLoop)) {
                if (sensorRange.getDistance(DistanceUnit.METER) < .1) {
                    telemetry.addData("Distance", "Too Close");
                    robot.frontRightMotor.setPower(-.25);
                    robot.frontLeftMotor.setPower(-.25);
                    robot.backRightMotor.setPower(-.25);
                    robot.backLeftMotor.setPower(-.25);
                } else if ((sensorRange.getDistance(DistanceUnit.METER) > .3) && (sensorRange.getDistance(DistanceUnit.METER) < 1)) {
                    telemetry.addData("Distance", "Too Far");
                    robot.frontRightMotor.setPower(.5);
                    robot.frontLeftMotor.setPower(.5);
                    robot.backRightMotor.setPower(.5);
                    robot.backLeftMotor.setPower(.5);
                }else if(sensorRange.getDistance(DistanceUnit.METER) > 1){
                    telemetry.addData("Distance", "Too Far");
                    robot.frontRightMotor.setPower(1);
                    robot.frontLeftMotor.setPower(1);
                    robot.backRightMotor.setPower(1);
                    robot.backLeftMotor.setPower(1);
                }else {
                    continueLoop = false;
                    telemetry.addData("Distance", "Correct");
                    robot.frontRightMotor.setPower(0);
                    robot.frontLeftMotor.setPower(0);
                    robot.backRightMotor.setPower(0);
                    robot.backLeftMotor.setPower(0);
                }
                telemetry.update();
            }

            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            sleep(500);

            /**Spin Carousel*/
            robot.spin.setPower(-1);

            sleep(2000);

            robot.spin.setPower(0);

            sleep(500);

            /**Rotate*/

            while (opModeIsActive() && (continueLoop)) {
                if (angles.firstAngle > 185) {

                    telemetry.addData("Angle", "Too Big");
                    robot.frontRightMotor.setPower(.5);
                    robot.frontLeftMotor.setPower(.5);
                    robot.backRightMotor.setPower(.5);
                    robot.backLeftMotor.setPower(.5);

                } else if (angles.firstAngle < 175) {

                    telemetry.addData("Angle", "Too Small");
                    robot.frontRightMotor.setPower(-.5);
                    robot.frontLeftMotor.setPower(-.5);
                    robot.backRightMotor.setPower(-.5);
                    robot.backLeftMotor.setPower(-.5);

                } else {
                    continueLoop = false;
                    robot.frontRightMotor.setPower(0);
                    robot.frontLeftMotor.setPower(0);
                    robot.backRightMotor.setPower(0);
                    robot.backLeftMotor.setPower(0);
                }
            }

            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);
            continueLoop = true;
            sleep(500);

            /**Park*/
            while(opModeIsActive() && continueLoop) {
                if (sensorRange.getDistance(DistanceUnit.METER) < .8) {
                    telemetry.addData("Distance", "Too Close");
                    robot.frontRightMotor.setPower(0);
                    robot.frontLeftMotor.setPower(0);
                    robot.backRightMotor.setPower(0);
                    robot.backLeftMotor.setPower(0);
                } else if (sensorRange.getDistance(DistanceUnit.METER) > 1) {
                    telemetry.addData("Distance", "Too Far");
                    robot.frontRightMotor.setPower(0);
                    robot.frontLeftMotor.setPower(0);
                    robot.backRightMotor.setPower(0);
                    robot.backLeftMotor.setPower(0);
                } else {
                    continueLoop = false;
                    telemetry.addData("Distance", "Correct");
                    robot.frontRightMotor.setPower(0);
                    robot.frontLeftMotor.setPower(0);
                    robot.backRightMotor.setPower(0);
                    robot.backLeftMotor.setPower(0);
                }
                telemetry.update();
            }
            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);

            continueLoop = true;

            sleep(500);
            timer.reset();

            while ((opModeIsActive())&&(timer.milliseconds()<1000)) {
                robot.frontRightMotor.setPower(strafePower);
                robot.frontLeftMotor.setPower(-strafePower);
                robot.backRightMotor.setPower(-strafePower);
                robot.backLeftMotor.setPower(strafePower);

            }

            robot.frontRightMotor.setPower(0);
            robot.frontLeftMotor.setPower(0);
            robot.backRightMotor.setPower(0);
            robot.backLeftMotor.setPower(0);




        }



        // Deactivate TFOD.
        tfod.deactivate();

        vuforiaFreightFrenzy.close();
        tfod.close();
    }

    /**
     * Display info (using telemetry) for a recognized object.
     */
    private void displayInfo(int i) {
        // Display label info.
        // Display the label and index number for the recognition.
        telemetry.addData("label " + i, recognition.getLabel());
        // Display upper corner info.
        // Display the location of the top left corner
        // of the detection boundary for the recognition
        telemetry.addData("Left, Top " + i, Double.parseDouble(JavaUtil.formatNumber(recognition.getLeft(), 0)) + ", " + Double.parseDouble(JavaUtil.formatNumber(recognition.getTop(), 0)));
        // Display lower corner info.
        // Display the location of the bottom right corner
        // of the detection boundary for the recognition
        telemetry.addData("Right, Bottom " + i, Double.parseDouble(JavaUtil.formatNumber(recognition.getRight(), 0)) + ", " + Double.parseDouble(JavaUtil.formatNumber(recognition.getBottom(), 0)));

        if (recognition.getLabel().equals("Duck") || recognition.getLabel().equals("Cube") || recognition.getLabel().equals("Ball")) {
            timer.reset();
            if (recognition.getRight() < 200) {
                //top level
                hubLevel = 500;
                extraTime = 0;
            } else if (recognition.getRight() > 600) {
                //bottom level
                hubLevel = 200;
                extraTime = 0;
            } else {
                //middle level
                hubLevel = 350;
                extraTime = 0;
            }
        }
    }

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


