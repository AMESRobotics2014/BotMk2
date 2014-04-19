/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//___
// | |_  o  _     o  _    _|_|_  _    __  _  o __     _  |  _  _  _
// | | | | _>     | _>     |_| |(/_   |||(_| | | |   (_  | (_|_> _>
//The main class is under control of Ali Nazzal & Ben Rose. DO NOT EDIT WITHOUT EXPLICIT PERMISSION!
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;

/**
 * This class connects the data from all the other classes and defines the
 * overall flow of the robot program.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * @author AliNazzal, BenRose
 */
public class RobotMain extends IterativeRobot {

    private static MotorControl MC;
    private static RobotMap R;
    private static ImageProcessing IP;
    private static InputManager IM;
    private static MasterTimer MT;
    private static Communication Com;
    protected static Watchdog wd;
    boolean turbo, manualControl;
    boolean shiftSTR;
    public static boolean start;
    static byte mode;

    public void robotInit() {
        MC = new MotorControl();
        MC.init();
        wd = Watchdog.getInstance();
        wd.setExpiration(.5);
        wd.setEnabled(true);
        turbo = false;
        shiftSTR = false;
        MT = new MasterTimer();
        MT.start();
        MT.Init();
        IM = new InputManager();
        IM.init();
       // Com = new Communication();
        //Com.init();
       // IP = new ImageProcessing();
        wd = Watchdog.getInstance();
        wd.setExpiration(0.5);
        wd.feed();
        MC.compressors(false);
        mode = 1;//1 for drive, 2 for pickup, 3 for carry

        start = true;
    }

    public void autonomousPeriodic() {
        //from 14ft

        while (isEnabled()) {
            wd.feed();
            if (start) {
                MT.Freset();
            }
            start = false;
            System.out.println("Time:" + MT.gdt(5));
            wd.feed();
            if (MT.gdt(5) >= 0 & MT.gdt(5) <= 2.5) {
                System.out.println("Autonomous foreward");
                MC.firstRightMotor.set(-.6);
                MC.firstLeftMotor.set(.6);
            } else if (MT.gdt(5) >= 2.6 & MT.gdt(5) <= 9.9) {
                {
                    MC.firstLeftMotor.set(0);
                    MC.firstRightMotor.set(0);
                }
                wd.feed();
            }
        }
    }

    public void teleopPeriodic() {
        MT.Freset();
        while (true && isOperatorControl() && isEnabled()) {
            wd.feed();

            if (IM.Pressure.get()) {
               // System.out.println("Stopped compressing");
                MC.compressors(false);
            }
            else {
               // System.out.println("Compressing");
                MC.compressors(true);
            }

            
            Event.Alwaysrun();
            /*if (IM.L1.getState()) {
             System.out.println("comp on");
             MC.compressors(true);
             } else {
             MC.compressors(false);
             System.out.println("comp off");
             }*/
            Event.m_Shift();
            Event.m_Grab();
            //Event.m_Kick();
            MT.Freset();
        }
    }

    //These are unesscessary but more intuitive
    public static void enterDrive() {
        mode = 1;
    }

    public static void enterPickup() {
        mode = 2;
    }

    public static void enterCarry() {
        mode = 3;
    }

    public static void enterManual() {
        mode = 4;
    }

    public static void autoinit(boolean start) {
        if (start) {
            MT.Freset();
        }
    }

    public static void cycleMode() {
        if (mode != 3) {
            mode++;
        } else {
            mode = 1;
        }
    }

    public static class Event {

        static boolean firing = false;
        //Sorted into scripted events and manual events by prefix s and m

        public static void Alwaysrun() {
            wd.feed();
            if (IM.SettingsL.getState() & MT.gdt(7) >= .2) {
                MT.sc(7);
                cycleMode();
            }
            //    DashboardPost();
            MC.drive(IM.getFinalAxis());
            if (IM.R1.getState()) {
                enterDrive();
            }
            if (IM.L2.getState()) {
                enterPickup();
            }
            if (IM.R2.getState()) {
                enterCarry();
            }
            if (MT.gdt(1) >= 16) {
                MT.sc(1);
                //         Event.Debug();
            }
            wd.feed();
        }

        public static void m_Shift() {
            if (IM.FaceBott.getState()) {
               // System.out.println("Shift high");
                MC.transmission(true);
            } else if (IM.FaceRight.getState()) {
                MC.transmission(false);
            }
        }

        public static void m_Grab() {
            if (IM.L1.getState()) {
                //MC.grabber(1);
                MC.raiser(1);
            }
             else if (IM.R1.getState()) {
                MC.raiser(2);
            }
            else {
                MC.raiser(0);
            }
            if (IM.L2.getState()) {
                MC.grabber(1);
            }else if (IM.R2.getState()) {
                MC.grabber(2);
            }else if(IM.FaceLeft.getState()){
                MC.grabber(0);
            }
            
        }

        public static void m_Kick() {
            if (IM.FaceBott.getState()) {
                MC.kicker(1);
            }
            if (IM.FaceTop.getState()) {
                MC.kicker(2);
            } else {
                MC.kicker(0);
            }
        }
    }
}