package adris.altoclef.commands;

import adris.altoclef.AltoClef;
import adris.altoclef.AltoClefCommands;
import adris.altoclef.commandsystem.Arg;
import adris.altoclef.commandsystem.ArgParser;
import adris.altoclef.commandsystem.Command;
import adris.altoclef.commandsystem.CommandException;

public class TestCommand extends Command {

    public TestCommand() throws CommandException {
        super("test", "Generic command for testing", new Arg(String.class, "extra", "", 0));
    }

    @Override
    protected void Call(AltoClef mod, ArgParser parser) throws CommandException {
        AltoClefCommands.TEMP_TEST_FUNCTION(mod, parser.Get(String.class));
        finish();
    }
}