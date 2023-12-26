package org.pokesplash.rbt.config;

import com.cobblemon.mod.common.pokemon.Pokemon;
import org.pokesplash.rbt.Rbt;

import java.util.HashSet;
import java.util.UUID;

public class Participant {
	private UUID uuid;
	private String name;
	private int availableRolls;
	private HashSet<Pokemon> pokemon;

	public Participant(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
		availableRolls = Rbt.config.getRerollAmount();
		pokemon = new HashSet<>();
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public int getAvailableRolls() {
		return availableRolls;
	}

	public HashSet<Pokemon> getPokemon() {
		return pokemon;
	}

	public void setPokemon(HashSet<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}

	public void swapPokemon(Pokemon oldPokemon, Pokemon newPokemon) throws Exception {
		if (availableRolls > 0) {
			boolean successRemove = pokemon.remove(oldPokemon);

			if (!successRemove) {
				throw new Exception(oldPokemon.getDisplayName().getString() + " is not part of your team.");
			}

			boolean successAdd = pokemon.add(newPokemon);

			if (!successAdd) {
				throw new Exception("Unable to add new Pokemon: " + newPokemon.getDisplayName().getString() + ".");
			}

			availableRolls --;
		} else {
			throw new Exception("You have no available rolls left.");
		}
	}
}
