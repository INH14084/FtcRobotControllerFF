package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;


@SuppressWarnings("DanglingJavadoc")
public class HardwareMap {
    //--Create Motors--//
    public DcMotor frontRightMotor = null;      //Control Hub Motor Port #0
    public DcMotor frontLeftMotor  = null;      //Control Hub Motor Port #1
    public DcMotor backRightMotor  = null;      //Control Hub Motor Port #2
    public DcMotor backLeftMotor   = null;      //Control Hub Motor Port #3
    //TODO May need to revert DcMotorEx to DcMotor in lines 16 and 40
    public DcMotorEx clawArm       = null; //Expansion Hub #3 Motor Port #0
    public DcMotor spin            = null; //Expansion Hub #3 Motor Port #1
    public DcMotor slidePull       = null; //Expansion Hub #3 Motor Port #2
    public DcMotorEx testMotor     = null; //Expansion Hub #3 Motor Port #3

    //--Create Servo--//
    public Servo clawServo         = null;      //Control Hub Servo Port #0
    public Servo rightTorque       = null;      //Control Hub Servo Port #1
    public Servo leftTorque        = null;      //Control Hub Servo Port #2


    //--Additional Variables--//
    com.qualcomm.robotcore.hardware.HardwareMap hardwareMap = null;
    //public ElapsedTime runtime     = new ElapsedTime();

    public HardwareMap() {

    }

    public void initialize(com.qualcomm.robotcore.hardware.HardwareMap hwMap) {
        hardwareMap     = hwMap;

        /**--Init Motor Info--*/
        frontRightMotor = hardwareMap.get(DcMotor.class  , "FrontRightDrive");
        frontLeftMotor  = hardwareMap.get(DcMotor.class  , "FrontLeftDrive");
        backRightMotor  = hardwareMap.get(DcMotor.class  , "BackRightDrive");
        backLeftMotor   = hardwareMap.get(DcMotor.class  , "BackLeftDrive");

        clawArm         = hardwareMap.get(DcMotorEx.class, "ClawArm");
        slidePull       = hardwareMap.get(DcMotor.class  , "SlidePull");

        spin            = hardwareMap.get(DcMotor.class, "Spin");

        testMotor       = hardwareMap.get(DcMotorEx.class, "TestMotor");

        /**--Init Servo Info--*/
        clawServo       = hardwareMap.get(Servo.class , "Claw");
        rightTorque     = hardwareMap.get(Servo.class , "rightTorque");
        leftTorque      = hardwareMap.get(Servo.class , "leftTorque");

        /**--Set Motor Direction--*/
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        clawArm.setDirection(DcMotor.Direction.FORWARD);
        slidePull.setDirection(DcMotor.Direction.FORWARD);

        spin.setDirection(DcMotor.Direction.FORWARD);

        testMotor.setDirection(DcMotor.Direction.FORWARD);


        /**--Set Motor Mode--*/
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //clawArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);           //No Encoder Use
        clawArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);           //Use With Encoder
        //slidePull.setMode(DcMotor.RunMode.RUN_USING_ENCODER);       //Remove comments or comment out code depending on needs
        slidePull.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);       //Depends if encoder is used later or even hooked up

        spin.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        testMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /**--Set Zero Power Behavior--*/
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        clawArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slidePull.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        spin.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        /**--Set Motor Power to Zero--*/
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);

        clawArm.setPower(0);
        slidePull.setPower(0);

        spin.setPower(0);

        testMotor.setPower(0);


    }
}
