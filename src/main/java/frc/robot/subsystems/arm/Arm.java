package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

 /*
  * functionality/specs/notes: 
  - one single motor turns the arm
  - when robot is first turned on, set the arm encoder to a specific angle

  - make arm go to a specific target angle
  double motorTargetAngle = 90.0;
  

  - be able to get the current angle of the motor

  - be able to check whether or not motor is at target angle // note put proportion in constants; power = error(distance) x P (constant)
boolean motorReachesTargetAngle;
if (motorReachesTargetAngle) {
    System.out.println(motorReachesTargetAngle)
}

  - convention: arm flat - 0 degrees, arm bent backwards - ~150 degrees
  double armFlat = 0.0;
  double armBentBackwards = 150.0;
  */

public class Arm extends SubsystemBase {
    double m_targetPosition = 0;
    double m_velocity = 0;
    

    // put motors & stuff here
    //Initializing motors; defining encoders; defining PID controllers; defining feedforward
    CANSparkMax m_armMotor = new CANSparkMax(0, MotorType.kBrushless);
    RelativeEncoder m_armEncoder = m_armMotor.getEncoder();
    SparkMaxPIDController m_ArmPIDController = m_armMotor.getPIDController();
    ArmFeedforward m_feedForward = new ArmFeedforward(Constants.ArmConstants.kS, Constants.ArmConstants.kV, Constants.ArmConstants.kG, Constants.ArmConstants.kA);
    

    public Arm() {
        // calculations for feedforward
        setupMotors();
        m_feedForward.calculate(m_targetPosition, m_targetPosition);
    }

    public void setupMotors() {
        // set up motors / runs when motor first turns on
        /*
         * 
         (put all constants in Constants.java)
         - set motor inversion
         - set smart current limit (usually 40 is good)
         - set position & velocity conversion factor
         */

         m_armMotor.setInverted(true);
         // note motor counts rotations
         m_armMotor.setSmartCurrentLimit(Constants.ArmConstants.kCurrentLimit);
         m_armEncoder.setPositionConversionFactor(Constants.ArmConstants.kConversionFactor);
         // to get the value below, divide this value (0.5) by 60
         m_armEncoder.setVelocityConversionFactor(Constants.ArmConstants.kVelocityConversionFactor);
// thingy for encoder angle
         m_armEncoder.setPosition(Constants.ArmConstants.kInitialAngle);
         //setup PID :)
         m_ArmPIDController.setP(Constants.ArmConstants.kP);
         m_ArmPIDController.setI(0);
         m_ArmPIDController.setD(0);
         

           


            
            
        } 

        
    

    

    public void setupMotorPID() {
        // setup motors pid
        /*
         * (put all PID constants in Constants.java)
         
         (ask jason once you get here if you don't understand)
         
         - set P, I, D values of the motor
         - set ArmFeedforward values
         */
    }
    
    public void goToAngle(double position){
        // sets position to target position; feedforward calculations for position and velocity
        this.m_targetPosition = position;
        Constants.ArmConstants.kFF = m_feedForward.calculate(position, m_velocity);
    }
        public void periodic() {
        // code inside here will run repeatedly while the robot is on
        // gets velocity + more calculations for PID
        m_ArmPIDController.setReference(this.m_targetPosition, ControlType.kPosition, 0, Constants.ArmConstants.kFF);
        this.m_velocity = m_armEncoder.getVelocity();

    }
    ;
    
        

    
   public boolean isAtAngle(){
    // checks if position is good or not
    if (m_armEncoder.getPosition()<m_targetPosition+Constants.ArmConstants.kAngleError && m_armEncoder.getPosition()>m_targetPosition-Constants.ArmConstants.kAngleError) {
        return true;
    }
    else {
        return false;
    }
   }
   
}
