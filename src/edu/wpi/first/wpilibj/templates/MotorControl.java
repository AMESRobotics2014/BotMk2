package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DigitalOutput;
/**
 * This class is where all calls to the actual motor hardware should occur. They
 * should be methods callable from the main function.
 *
 * @author kolton.yager
 */
public class MotorControl {

    protected static Victor firstRightMotor;
   // protected static Victor secondRightMotor;
    protected static Victor firstLeftMotor;
   // protected static Victor secondLeftMotor;
    /*    protected static Victor PullBack, shooterMotor2;
    protected static Relay GrabWheel, grabberMotor;
    protected static Jaguar elevatorMotor;
    protected static Relay low, ratchet, clutch;*/
    protected static Relay high;
    protected static Relay low;
    protected static Relay raise;
    protected static Relay comp;
    protected static Victor grab, kick;
    protected static InputManager IM;
    protected static Communication Com;
  //protected static Relay compress;
    
    public void init() {
        firstRightMotor = new Victor(RobotMap.firstRightMotor);
        firstLeftMotor = new Victor(RobotMap.firstLeftMotor);
        high = new Relay(RobotMap.high);
        Com = new Communication();
        high.setDirection(Relay.Direction.kForward);
        low = new Relay(RobotMap.low);
        low.setDirection(Relay.Direction.kForward);
        grab = new Victor(1);
        raise = new Relay(2);
        comp = new Relay(7);
        comp.setDirection(Relay.Direction.kForward);
        kick = new Victor(7);
      //  compress = new Relay(4);
       // compress.setDirection(Relay.Direction.kForward);

        IM = new InputManager();
    }

    public void drive(double[] mv) {
        Com.RobotSpeed(mv[0]);
        Com.Otherspeed(mv[1]);
        firstRightMotor.set(limit(mv[0]));
        firstLeftMotor.set(limit(mv[1]));
        
    }
    public static double limit(double val) {
        if (val < -1) {
            val = -1;
        }

        if (val > 1) {
            val = 1;
        }

        return val;
    }
    public void transmission(boolean fast) {
        if (!fast) {
            low.set(Relay.Value.kOn);
            high.set(Relay.Value.kOff);
        } else if (fast) {
           // System.out.println("Command high");
            low.set(Relay.Value.kOff);
            high.set(Relay.Value.kOn);
        }
    }
    public void grabber(int dir){
        if(dir == 0){
            grab.set(0);
        }
        else if(dir == 1){
            grab.set(.3);
        }
        else if(dir == 2){
            grab.set(-.7);
        }
        else{
            grab.set(0);
        }
    }
    public void compressors(boolean on){
     //  comp.set(on);
        
        if(on){
            comp.set(Relay.Value.kOn);
        }
        else{
            comp.set(Relay.Value.kOff);
        }
    }
    public void raiser(int dir){
        if(dir == 0){
            raise.set(Relay.Value.kOff);
        }else if(dir == 1){
            raise.set(Relay.Value.kForward);
        }else if(dir == 2){
            raise.set(Relay.Value.kReverse);
        }else{
            raise.set(Relay.Value.kOff);
        }
    }
    public void kicker(int dir){
        if(dir == 0){
            kick.set(0);
        }else if(dir == 1){
            kick.set(.5);
        }else if(dir == 2){
            kick.set(-.5);
        }else{
            kick.set(0);
        }
    }
}
