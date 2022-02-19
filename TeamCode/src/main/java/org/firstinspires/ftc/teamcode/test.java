package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.Tfod;

@Autonomous(name = "asdf (Blocks to Java)")
public class test extends LinearOpMode {
    HardwareMap robot = new HardwareMap();
    ElapsedTime runtime = new ElapsedTime();
    ElapsedTime timer = new ElapsedTime();
    private VuforiaCurrentGame vuforiaFreightFrenzy;
    private Tfod tfod;


    Recognition recognition;


    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        List<Recognition> recognitions;
        int index;
        robot.initialize(hardwareMap);
        vuforiaFreightFrenzy = new VuforiaCurrentGame();
        tfod = new Tfod();

        // Sample TFOD Op Mode
        // Initialize Vuforia.
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
        robot.clawArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.clawArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.clawServo.setPosition(.75);
        // Enable following block to zoom in on target.
        tfod.setZoom(1, 16 / 9);
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        // Wait for start command from Driver Station.
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            timer.reset();
            while ((opModeIsActive()) && (timer.milliseconds() < 5000)) {
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
                telemetry.update();
            }
            // all numbers in this section have 0 testing and may be very, very janky.
            // this should be working for the cloesest to the warehouse red side.
            if ((opModeIsActive()) && (recognition.getLabel().equals("Duck") || recognition.getLabel().equals("Cube") || recognition.getLabel().equals("Ball"))) {
                timer.reset();
                if (recognition.getRight() < 200) {
                // the top
                    while (timer.milliseconds() < 5000) {
                        robot.frontLeftMotor.setPower(-.5);
                        robot.frontRightMotor.setPower(.5);
                        robot.backLeftMotor.setPower(.5);
                        robot.backRightMotor.setPower(-.5);
                    }

                    while ((timer.milliseconds() > 6000) && (timer.milliseconds() < 12000)) {
                        robot.clawArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        robot.clawArm.setTargetPosition(-1000);
                        robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                        robot.clawArm.setPower(1);

                        sleep(3000);

                        robot.clawArm.setPower(0);


                        sleep(1000);

                        robot.clawArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        robot.clawArm.setTargetPosition(100);
                        robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                        robot.clawArm.setPower(1);

                        sleep(3000);

                        robot.clawServo.setPosition(0);

                        robot.clawArm.setPower(0);
                    }

                } else if (recognition.getRight() > 600) {
                // the bottom
                    while (timer.milliseconds() < 5000) {
                        robot.frontLeftMotor.setPower(-0.5);
                        robot.frontRightMotor.setPower(.5);
                        robot.backLeftMotor.setPower(.5);
                        robot.backRightMotor.setPower(-.5);
                    }

                    while ((timer.milliseconds() > 6000) && (timer.milliseconds() < 12000)) {
                        robot.clawArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        robot.clawArm.setTargetPosition(-1000);
                        robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                        robot.clawArm.setPower(1);

                        sleep(3000);

                        robot.clawArm.setPower(0);


                        sleep(1000);

                        robot.clawArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        robot.clawArm.setTargetPosition(750);
                        robot.clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                        robot.clawArm.setPower(1);

                        sleep(3000);

                        robot.clawServo.setPosition(0);

                        robot.clawArm.setPower(0);
                    }

                } else {
                // the middle
                    while (timer.milliseconds() < 5000) {
                        robot.frontLeftMotor.setPower(-.5);
                        robot.frontRightMotor.setPower(.5);
                        robot.backLeftMotor.setPower(.5);
                        robot.backRightMotor.setPower(-.5);
                    }

                    while ((timer.milliseconds() > 6000) && (timer.milliseconds() < 12000)) {
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
                    }
                }

            }
            while ((timer.milliseconds() > 12000) && (timer.milliseconds() < 14000)){
                robot.frontLeftMotor.setPower(-.5);
                robot.frontRightMotor.setPower(.5);
                robot.backLeftMotor.setPower(-.5);
                robot.backRightMotor.setPower(.5);

            }
            while ((timer.milliseconds() < 14000) && (timer.milliseconds() < 18000)) {
                robot.frontLeftMotor.setPower(-.5);
                robot.frontRightMotor.setPower(.5);
                robot.backLeftMotor.setPower(.5);
                robot.backRightMotor.setPower(-.5);
                robot.spin.setPower(.5);
            }
            while ((timer.milliseconds() < 18000) && (timer.milliseconds() < 19000)) {
                robot.frontLeftMotor.setPower(-.5);
                robot.frontRightMotor.setPower(.5);
                robot.backLeftMotor.setPower(.5);
                robot.backRightMotor.setPower(-.5);
                //dont know what the motors should be for backwards
            }
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
    }
}