package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Arm extends SubsystemBase {
    double m_targetPosition = 0;
    double m_velocity = 0;
    

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

         m_armMotor.setInverted(true);
         // note motor counts rotations
         m_armMotor.setSmartCurrentLimit(Constants.ArmConstants.kCurrentLimit);
         m_armEncoder.setPositionConversionFactor(Constants.ArmConstants.kConversionFactor);
         // to get the value below, divide this value (0.5) by 60
         m_armEncoder.setVelocityConversionFactor(Constants.ArmConstants.kVelocityConversionFactor);
         m_armEncoder.setPosition(Constants.ArmConstants.kInitialAngle);
         //setup PID :)
         m_ArmPIDController.setP(Constants.ArmConstants.kP);
         m_ArmPIDController.setI(0);
         m_ArmPIDController.setD(0);
         

           


            
            
        } 

        
    

    

    public void setupMotorPID() {
         
    }
    
    public void goToAngle(double position){
        // sets position to target position; feedforward calculations for position and velocity
        this.m_targetPosition = position;
        Constants.ArmConstants.kFF = m_feedForward.calculate(position, m_velocity);
    }
        public void periodic() {
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
