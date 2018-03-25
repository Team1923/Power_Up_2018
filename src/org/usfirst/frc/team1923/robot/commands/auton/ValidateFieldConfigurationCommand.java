package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;

/**
 * Validate that the GameSpecificMessage is within an Autonomous mode's field configuration settings
 * at the time of execution. If not, the command will not finish.
 */
public class ValidateFieldConfigurationCommand extends Command {

    private Autonomous.FieldConfiguration[] fieldConfigurations;
    private boolean validated;

    public ValidateFieldConfigurationCommand(Command command) {
        if (!command.getClass().isAnnotationPresent(Autonomous.class)) {
            throw new IllegalArgumentException(command.getClass().getSimpleName() + " does not have an @Autonomous annotation.");
        }

        this.fieldConfigurations = command.getClass().getAnnotation(Autonomous.class).fieldConfigurations();
    }

    @Override
    protected void execute() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        Autonomous.FieldConfiguration fieldConfiguration;

        try {
            fieldConfiguration = Autonomous.FieldConfiguration.valueOf(gameData);
        } catch(IllegalArgumentException e) {
            return;
        }

        for (Autonomous.FieldConfiguration configuration : this.fieldConfigurations) {
            if (configuration == fieldConfiguration) {
                this.validated = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return this.validated;
    }

}
