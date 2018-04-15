package hello;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

public class CommandExecutor {

    public String execToString(String command) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommandLine commandline = CommandLine.parse(command);
        DefaultExecutor exec = new DefaultExecutor();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        exec.setStreamHandler(streamHandler);
        exec.setExitValue(127);
        exec.execute(commandline);
        return(outputStream.toString());
    }

    //public InputStream execToSomething() throws Exception {
    //    Process p = new ProcessBuilder().command(Arrays.asList("cat" "/dev/zero")).start();
    //    .getInputStream();
    //
    //}
}
