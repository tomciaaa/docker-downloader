package hello;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandExecutorTest {
    private CommandExecutor executor;

    @Before
    public void setup() {
        executor = new CommandExecutor();
    }

    @Test
    public void quoted_arguments_accepted() throws Exception {
        String output = executor.execToString("/usr/bin/sh -c 'echo testing this and other'");
        assertThat(output).isEqualTo("testing this and other");

    }
}
