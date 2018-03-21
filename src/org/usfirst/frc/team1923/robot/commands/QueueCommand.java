package org.usfirst.frc.team1923.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

public class QueueCommand extends Command {

    private Command command;
    private QueueCommandConditional conditional;

    private boolean executed;

    public QueueCommand(Command command, QueueCommandConditional conditional) {
        if (command == null) {
            throw new NullPointerException("Command cannot be null.");
        }

        this.command = command;
        this.conditional = conditional;

        try {
            Field requirementsField = Command.class.getDeclaredField("m_requirements");
            requirementsField.setAccessible(true);

            Object requirements = requirementsField.get(command);

            Field setField = requirements.getClass().getDeclaredField("m_set");
            setField.setAccessible(true);

            Vector subsystems = (Vector) setField.get(requirements);

            for (Object subsystem : subsystems) {
                this.requires((Subsystem) subsystem);
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException("Cannot retrieve " + command.getClass().getName() + "'s dependencies.");
        }
    }

    @Override
    protected void initialize() {
        this.executed = false;
    }

    @Override
    protected void execute() {
        if (this.conditional.isTrue() && !this.executed) {
            try {
                Method clearRequirements = Command.class.getDeclaredMethod("clearRequirements");

                clearRequirements.setAccessible(true);
                clearRequirements.invoke(this.command);
            } catch (Exception e) {
                e.printStackTrace();

                throw new RuntimeException("Cannot clear " + this.command.getClass().getName() + "'s requirements.");
            }

            this.command.start();
            this.executed = true;
        }
    }

    @Override
    protected boolean isFinished() {
        return this.executed && !this.command.isRunning();
    }

    @Override
    protected void end() {
        if (this.command.isRunning()) {
            this.command.cancel();
        }
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    public interface QueueCommandConditional {

        public boolean isTrue();

    }

}
