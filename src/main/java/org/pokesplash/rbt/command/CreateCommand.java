package org.pokesplash.rbt.command;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.config.Tourney;
import org.pokesplash.rbt.util.CobblemonUtils;
import org.pokesplash.rbt.util.LuckPermsUtils;
import org.pokesplash.rbt.util.Utils;

import java.util.HashSet;

public class CreateCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("create")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(),
								CommandHandler.basePermission + ".create");
					} else {
						return true;
					}
				})
				.executes(this::run)
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		// If no active tourney, dont do anything.
		if (Rbt.tourney != null) {
			context.getSource().sendMessage(Text.literal(
					Utils.formatMessage("§cThere is already an active RBT.",
							context.getSource().isExecutedByPlayer())
			));
			return 1;
		}

		Rbt.tourney = new Tourney();


		Utils.broadcastMessage("§bA RBT has been created! Run §3/rbt join §bto join!");

		return 1;
	}
}
