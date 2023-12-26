package org.pokesplash.rbt.config;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.util.CobblemonUtils;
import org.pokesplash.rbt.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class Tourney {
	private String date;
	private HashSet<Participant> participants;

	public Tourney() {
		date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		participants = new HashSet<>();
	}

	public boolean addParticipant(ServerPlayerEntity player) {
		if (getParticipant(player) == null) {
			participants.add(new Participant(player.getUuid(), player.getDisplayName().getString()));
			return true;
		} else {
			return false;
		}
	}

	public boolean removeParticipant(ServerPlayerEntity player) {

		Participant participant = getParticipant(player);

		if (participant == null) {
			return false;
		}

		participants.remove(participant);
		return true;
	}

	public Participant getParticipant(ServerPlayerEntity player) {
		for (Participant participant : participants) {
			if (participant.getUuid().equals(player.getUuid())) {
				return participant;
			}
		}

		return null;
	}

	public ArrayList<Exception> start() {

		ArrayList<Exception> exceptions = new ArrayList<>();

		try {
			for (Participant participant : participants) {
				HashSet<Pokemon> playerPokemon = CobblemonUtils.addRbtPokemon(participant.getUuid());

				participant.setPokemon(playerPokemon);
			}
		} catch (Exception e) {
			exceptions.add(e);
		}

		return exceptions;
	}
}
