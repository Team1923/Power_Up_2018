package org.usfirst.frc.team1923.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class QueueCommand extends Command {

    private Command command;
    private QueueCommandConditional conditional;

    private boolean initialized;

    public QueueCommand(Command command, QueueCommandConditional conditional) {
        this.command = command;
        this.conditional = conditional;
    }

    @Override
    protected void initialize() {
        this.initialized = false;
    }

    @Override
    protected void execute() {
        if (this.conditional.isTrue() && !this.initialized) {
            this.command.start();
            this.initialized = true;
        }
    }

    @Override
    protected boolean isFinished() {
        return this.initialized && !this.command.isRunning();
    }

    @Override
    protected void end() {
        this.command.cancel();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    public interface QueueCommandConditional {

        public boolean isTrue();

    }

}
