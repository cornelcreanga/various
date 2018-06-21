package commandline;

import picocli.CommandLine;

@CommandLine.Command(name = "topology",
        subcommands = {CommandInfo.class,CommandDelete.class})
public class CommandMain implements Runnable {

    @CommandLine.Option(names = {"-s", "--server"}, description = "Server")
    private String server;

    @Override
    public void run() {
        System.out.println("main run");
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new CommandMain());
        if (args.length == 0) {
            commandLine.usage(System.out);
        } else
            commandLine.parseWithHandler(new CommandLine.RunAll(), args);
    }
}
