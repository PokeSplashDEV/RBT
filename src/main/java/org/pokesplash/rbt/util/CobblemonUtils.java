package org.pokesplash.rbt.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.abilities.AbilityTemplate;
import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import org.pokesplash.rbt.Rbt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public abstract class CobblemonUtils {
	public static boolean isHA(Pokemon pokemon) {
		if (pokemon.getForm().getAbilities().getMapping().get(Priority.LOW) == null ||
				pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).size() != 1) {
			return false;
		}
		String ability =
				pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).get(0).getTemplate().getName();

		return pokemon.getAbility().getName().equalsIgnoreCase(ability);
	}

	public static ArrayList<AbilityTemplate> getNormalAbilities(Pokemon pokemon) {

		ArrayList<AbilityTemplate> abilities = new ArrayList<>();

		for (PotentialAbility ability : pokemon.getForm().getAbilities().getMapping().get(Priority.LOWEST)) {
			abilities.add(ability.getTemplate());
		}

		return abilities;
	}

	public static AbilityTemplate getHA(Pokemon pokemon) {
		if (pokemon.getForm().getAbilities().getMapping().get(Priority.LOW) == null ||
				pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).size() != 1) {
			return null;
		}

		return pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).get(0).getTemplate();
	}

	public static Pokemon getRandomPokemon() {
		Pokemon rbtPokemon = null;

		boolean isValid = false;

		while (!isValid) {
			rbtPokemon = new Pokemon();

			if (!rbtPokemon.isLegendary() &&
					!rbtPokemon.isUltraBeast() &&
					!rbtPokemon.isMythical()) {
				isValid = true;
			}
		}

		rbtPokemon.setLevel(100);

		rbtPokemon.getPersistentData().putBoolean("unbreedable", true);
		rbtPokemon.setTradeable(false);

		return rbtPokemon.initialize();
	}

	public static HashSet<Pokemon> addRbtPokemon(UUID player) throws NoPokemonStoreException {
		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
		PCStore pcStore = Cobblemon.INSTANCE.getStorage().getPC(player);

		HashSet<Pokemon> playerPokemon = new HashSet<>();

		for (int x = 0; x < 6; x++) {
			Pokemon pokemon = party.get(x);

			if (pokemon == null) {
				continue;
			}

			party.remove(new PartyPosition(x));

//				party.getOverflowPC().add(pokemon);
			pcStore.add(pokemon);
		}

		for (int y = 0; y < Rbt.config.getPokemonAmount(); y++) {
			Pokemon rbtPokemon = CobblemonUtils.getRandomPokemon();

			party.add(rbtPokemon);

			playerPokemon.add(rbtPokemon);
		}

		return playerPokemon;
	}
}
