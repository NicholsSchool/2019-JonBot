package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPaths extends CommandGroup
{
    public AutoPaths()
    {
        addSequential(new EncoderMove(124, 0.5));
        addSequential(new TurnToAngle(-95, 0.5));
        addSequential(new EncoderMove(128, 0.5));
        addSequential(new TurnToAngle(88, 0.5));
        addSequential(new EncoderMove(110, 0.5));
        addSequential(new EncoderMove(-110, -0.5));
        addSequential(new TurnToAngle(-90, 0.5));
        addSequential(new EncoderMove(372, 0.5));
        addSequential(new TurnToAngle(-30, 0.5));
        addSequential(new EncoderMove(63, 0.5));
        addSequential(new TurnToAngle(30, 0.5));
        addSequential(new EncoderMove(130, 0.5));
    }
}