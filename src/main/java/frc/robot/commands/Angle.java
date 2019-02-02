package frc.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
//import sun.management.Sensor;
import frc.robot.subsystems.DriveTrain;

//import com.sun.org.apache.xalan.internal.templates.Constants;

public class Angle extends Command{
    double desiredAngle;
    double lSpeed;
    double rSpeed;



    public Angle(double angle) {
        requires(Robot.driveTrain);
        desiredAngle = angle;

        if(desiredAngle>0){
            lSpeed = 1.0;
            rSpeed = 0;
        }
        else{
            lSpeed =0;
            rSpeed= 1.0;
        }
    }
    @Override
    protected void initialize(){
        Robot.navX.reset();
        System.out.println("running command 1");

    }
    @Override
    protected void execute(){
        Robot.driveTrain.move(1,0);
        System.out.println("running command 2");

    }
    @Override
    protected boolean isFinished() {
        double currentAngle = Robot.navX.getAngle();
        System.out.println("running command 3");
        return (currentAngle < desiredAngle + 5
        && currentAngle > desiredAngle - 5);
    }
    @Override
    protected void end(){
        Robot.driveTrain.stop();
        Robot.driveTrain.resetEncoders();
        System.out.println("running command 4");
    }
    @Override
    protected void interrupted(){
        end();
        System.out.println("running command 5");
    }


   }
