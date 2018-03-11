package moe.clienthax.tests;

import moe.clienthax.tests.commands.EntityTestCommand;
import moe.clienthax.tests.commands.PCTestCommand;
import moe.clienthax.tests.commands.PartyTestCommand;
import moe.clienthax.tests.commands.SpriteTestCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

/**
 * Created by clienthax on 05/03/2018.
 */
@Plugin(id = "testsss", name="Tests", version = "1.0.0", description = "Tests yo", dependencies = {@Dependency(id = "pixelmonbridge")})
public class Tests {

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        CommandSpec testSpec = CommandSpec.builder()
                .executor(new SpriteTestCommand())
                .build();
        Sponge.getCommandManager().register(this, testSpec, "spritetest");


        CommandSpec pcTestSpec = CommandSpec.builder()
                .executor(new PCTestCommand())
                .build();
        Sponge.getCommandManager().register(this, pcTestSpec, "pctest");

        CommandSpec partyTestSpec = CommandSpec.builder()
                .executor(new PartyTestCommand())
                .build();
        Sponge.getCommandManager().register(this, partyTestSpec, "partytest");

        CommandSpec entityTestSpec = CommandSpec.builder()
                .executor(new EntityTestCommand())
                .build();
        Sponge.getCommandManager().register(this, entityTestSpec, "entitytest");
    }

}