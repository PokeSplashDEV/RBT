package org.pokesplash.rbt.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.block.entity.PCBlockEntity;
import com.cobblemon.mod.common.net.messages.client.storage.party.SetPartyPokemonPacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.util.CobblemonUtils;
import org.pokesplash.rbt.util.LuckPermsUtils;
import org.pokesplash.rbt.util.Utils;

import java.util.HashSet;
import java.util.UUID;

public class GiveCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("give")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".give");
					} else {
						return true;
					}
				})
				.executes(this::usage)
				.then(CommandManager.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							for (ServerPlayerEntity player :
									ctx.getSource().getServer().getPlayerManager().getPlayerList()) {
								builder.suggest(player.getName().getString());
							}
							return builder.buildFuture();
						})
						.executes(this::run))
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		// If no active tourney, dont do anything.
		if (Rbt.tourney == null) {
			context.getSource().sendMessage(Text.literal(
					Utils.formatMessage("§cThere is no RBT currently.",
							context.getSource().isExecutedByPlayer())
			));
			return 1;
		}

		String playerArg = StringArgumentType.getString(context, "player");

		ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerArg);

		if (player == null) {
			context.getSource().sendMessage(Text.literal(
					Utils.formatMessage("§cPlayer " + playerArg + " could not be found.",
							context.getSource().isExecutedByPlayer())
			));
			return 1;
		}

		try {

			HashSet<Pokemon> playerPokemon = CobblemonUtils.addRbtPokemon(player.getUuid());

			// Adds player to tourney and sets their team.
			Rbt.tourney.addParticipant(player);
			Rbt.tourney.getParticipant(player).setPokemon(playerPokemon);


			for (Pokemon pokemon : playerPokemon) {
				context.getSource().sendMessage(Text.literal(
						Utils.formatMessage("§aGiven " + player.getName().getString() + " " +
										pokemon.getDisplayName().getString(),
								context.getSource().isExecutedByPlayer())
				));
			}

		} catch (Exception e) {
			e.printStackTrace();
			context.getSource().sendMessage(Text.literal(e.getMessage()));
		}


		return 1;
	}

	public int usage(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.literal(
				Utils.formatMessage("§2RBT - Give\n§a- rbt give <player>",
						context.getSource().isExecutedByPlayer())
		));

		return 1;
	}
}
