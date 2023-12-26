package org.pokesplash.rbt.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
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

public class TeamCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("team")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(),
								CommandHandler.basePermission + ".team");
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

		String output = "§3" + player.getName().getString() + " Team\n";

		for (Pokemon pokemon : Rbt.tourney.getParticipant(player).getPokemon()) {

			output += "§b- " + pokemon.getDisplayName().getString() + "\n";
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(
				output.trim(), context.getSource().isExecutedByPlayer()
		)));


		return 1;
	}

	public int usage(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.literal(
				Utils.formatMessage("§2RBT - Team\n§a- rbt team <player>",
						context.getSource().isExecutedByPlayer())
		));

		return 1;
	}
}
