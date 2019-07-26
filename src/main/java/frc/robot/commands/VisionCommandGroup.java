package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.sensors.Vision;

public class VisionCommandGroup extends CommandGroup
{

    private double angleToLine;
    private double distanceToLine;
    private double angleToWall;
    public VisionCommandGroup(double speed)
    {
        System.out.println("Running Vision");
        angleToLine = Vision.angleToLine;
        addSequential(new TurnToAngle(angleToLine, speed));
        distanceToLine = Vision.distanceToLine;
        angleToWall = Vision.angleToWall;
        addSequential(new EncoderMove(speed, distanceToLine));
        addSequential(new TurnToAngle(angleToWall, speed));
    }
}