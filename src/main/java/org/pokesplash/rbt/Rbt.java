package org.pokesplash.rbt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.rbt.command.CommandHandler;
import org.pokesplash.rbt.config.Config;
import org.pokesplash.rbt.config.Tourney;

public class Rbt implements ModInitializer {
	public static final String MOD_ID = "Rbt";
	public static final String BASE_PATH = "/config/" + MOD_ID + "/";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Config config = new Config();
	public static Tourney tourney = null;
	public static MinecraftServer server;


	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		ServerWorldEvents.LOAD.register((t, e) -> server = t);
		load();
	}

	public static void load() {
		config.init();
	}
}
