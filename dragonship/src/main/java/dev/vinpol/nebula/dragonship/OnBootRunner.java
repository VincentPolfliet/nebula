package dev.vinpol.nebula.dragonship;

import dev.vinpol.nebula.dragonship.ships.ShipSynchronizeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "nebula.sync-on-boot", havingValue = "true")
public class OnBootRunner implements ApplicationRunner {

    private final ShipSynchronizeTask task;

    public OnBootRunner(ShipSynchronizeTask task) {
        this.task = task;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        task.run();
    }
}
