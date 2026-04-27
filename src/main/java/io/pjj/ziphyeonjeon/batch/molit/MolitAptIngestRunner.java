package io.pjj.ziphyeonjeon.batch.molit;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MolitAptIngestRunner implements CommandLineRunner {

    private final MolitAptCsvIngestService csvService;
    private final HouseDataMigrationService migrationService;
    private final Environment env;

    @Override
    public void run(String... args) throws Exception {
        String ingestEnabled = env.getProperty("molit.ingest", "false");
        String migrationEnabled = env.getProperty("house.migration", "false");

        if ("true".equalsIgnoreCase(ingestEnabled)) {
            System.out.println("[MOLIT] ingest start...");
            csvService.ingestAllApt();
            System.out.println("[MOLIT] ingest done.");
        }

        if ("true".equalsIgnoreCase(migrationEnabled)) {
            System.out.println("[HOUSE] data migration start...");
            migrationService.migrateAllDataToHouse();
            System.out.println("[HOUSE] data migration done.");
        }
    }
}
