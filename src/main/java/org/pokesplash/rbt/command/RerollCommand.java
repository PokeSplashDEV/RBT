package org.pokesplash.rbt.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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

public class RerollCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("roll")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(),
								CommandHandler.basePermission + ".user");
					} else {
						return true;
					}
				})
				.executes(this::usage)
				.then(CommandManager.argument("slot", IntegerArgumentType.integer(1, 6))
						.suggests((ctx, builder) -> {
							for (int x = 1; x < 7; x++) {
								builder.suggest(x);
							}
							return builder.buildFuture();
						})
						.executes(this::run))
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player"));
			return 1;
		}

		// If no active tourney, dont do anything.
		if (Rbt.tourney == null) {
			context.getSource().sendMessage(Text.literal(
					Utils.formatMessage("§cThere is no active RBT.",
							context.getSource().isExecutedByPlayer())
			));
			return 1;
		}

		int slot = IntegerArgumentType.getInteger(context, "slot");

		ServerPlayerEntity player = context.getSource().getPlayer();


		try {

			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

			Pokemon newPokemon = CobblemonUtils.getRandomPokemon();

			Rbt.tourney.getParticipant(player).swapPokemon(party.get(slot - 1),
					newPokemon);

			party.remove(new PartyPosition(slot - 1));

			party.add(newPokemon);

		} catch (Exception e) {
			context.getSource().sendMessage(Text.literal("§c" + e.getMessage()));
		}


		return 1;
	}

	public int usage(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.literal(
				Utils.formatMessage("§2RBT - Roll\n§a- rbt roll <slot>",
						context.getSource().isExecutedByPlayer())
		));

		return 1;
	}
}
