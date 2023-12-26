package org.pokesplash.rbt.config;

import com.google.gson.Gson;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Config {
	private String prefix;
	private int pokemonAmount;
	private int rerollAmount;

	public Config() {
		prefix = "§3[§bRBT§3]";
		pokemonAmount = 4;
		rerollAmount = 1;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(Rbt.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					prefix = cfg.getPrefix();
					pokemonAmount = cfg.getPokemonAmount();
					rerollAmount = cfg.getRerollAmount();
				});

		if (!futureRead.join()) {
			Rbt.LOGGER.info("No config.json file found for " + Rbt.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(Rbt.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				Rbt.LOGGER.fatal("Could not write config for " + Rbt.MOD_ID + ".");
			}
			return;
		}
		Rbt.LOGGER.info(Rbt.MOD_ID + " config file read successfully");
	}

	public int getPokemonAmount() {
		return pokemonAmount;
	}

	public int getRerollAmount() {
		return rerollAmount;
	}

	public String getPrefix() {
		return prefix;
	}
}
