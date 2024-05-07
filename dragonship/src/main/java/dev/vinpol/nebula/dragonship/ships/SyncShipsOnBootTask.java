package dev.vinpol.nebula.dragonship.ships;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "nebula.ext.sync-on-boot", havingValue = "true")
public class SyncShipsOnBootTask implements ApplicationRunner {

    private final ShipSynchronizeTask task;

    public SyncShipsOnBootTask(ShipSynchronizeTask task) {
        this.task = task;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        task.run();
    }
}
