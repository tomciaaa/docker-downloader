package com.github.tomciaaa.docker_hub_api;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TheWholeShebangTest {
    @Test
    public void fetch_busybox() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TheWholeShebang.FetchImage("library/busybox", "latest", os);
        assertThat(os.size()).isGreaterThan(723172);
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));

        List<String> names = new ArrayList<>();
        TarArchiveEntry entry = tarIn.getNextTarEntry();
        while(entry != null) {
            names.add(entry.getName());
            assertThat(entry.getSize()).isGreaterThan(0);
            entry = tarIn.getNextTarEntry();
        }
        assertThat(names).contains("manifest.json", "repositories");
        assertThat(names).anyMatch(x -> x.endsWith("/layer.tar"));
        assertThat(names).anyMatch(x -> x.matches("\\w{64}\\.json"));
    }
}