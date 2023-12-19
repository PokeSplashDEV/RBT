package org.pokesplash.rbt.config;

import com.google.gson.Gson;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;

	public Lang() {
		title = Rbt.MOD_ID;
		fillerMaterial = "minecraft:white_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(Rbt.BASE_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
				});

		if (!futureRead.join()) {
			Rbt.LOGGER.info("No lang.json file found for " + Rbt.MOD_ID + ". Attempting to " +
					"generate " +
					"one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(Rbt.BASE_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				Rbt.LOGGER.fatal("Could not write lang.json for " + Rbt.MOD_ID + ".");
			}
			return;
		}
		Rbt.LOGGER.info(Rbt.MOD_ID + " lang file read successfully.");
	}
}
