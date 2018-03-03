package org.usfirst.frc.team1923.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class QueueCommand extends Command {

    private Command command;
    private QueueCommandConditional conditional;

    private boolean finished;

    public QueueCommand(Command command, QueueCommandConditional conditional) {
        this.command = command;
        this.conditional = conditional;
    }

    @Override
    protected void initialize() {
        this.finished = false;
    }

    @Override
    protected void execute() {
        if (this.conditional.isTrue()) {
            this.command.start();
        }
    }

    @Override
    protected boolean isFinished() {
        return this.finished;
    }

    public interface QueueCommandConditional {

        public boolean isTrue();

    }

}
