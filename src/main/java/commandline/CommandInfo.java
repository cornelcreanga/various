package commandline;

import picocli.CommandLine;

@CommandLine.Command(name = "info")
public class CommandInfo implements Runnable {

    @CommandLine.ParentCommand
    private CommandMain parent;
    @CommandLine.Option(names = {"-n", "--name"}, required = true, description = "Topology name")
    private String[] topologyName;

    @Override
    public void run() {
        System.out.println("info run");
    }

}
