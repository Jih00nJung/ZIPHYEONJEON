package io.pjj.ziphyeonjeon.batch.molit;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MolitAptIngestRunner implements CommandLineRunner {

    private final MolitAptCsvIngestService service;
    private final Environment env;

    @Override
    public void run(String... args) throws Exception {
        String enabled = env.getProperty("molit.ingest", "false");
        if (!"true".equalsIgnoreCase(enabled)) return;

        System.out.println("[MOLIT] ingest start...");
        service.ingestAllApt();
        System.out.println("[MOLIT] ingest done.");
    }
}
