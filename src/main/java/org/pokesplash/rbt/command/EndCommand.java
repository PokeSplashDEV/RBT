package org.pokesplash.rbt.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.util.LuckPermsUtils;
import org.pokesplash.rbt.util.Utils;

public class EndCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("end")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(),
								CommandHandler.basePermission + ".end");
					} else {
						return true;
					}
				})
				.executes(this::run)
				.then(CommandManager.argument("winner", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							for (ServerPlayerEntity player :
									ctx.getSource().getServer().getPlayerManager().getPlayerList()) {
								builder.suggest(player.getDisplayName().getString());
							}
							return builder.buildFuture();
						})
						.executes(this::runWinner)
				)
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		// If no active tourney, dont do anything.
		if (Rbt.tourney == null) {
			context.getSource().sendMessage(Text.literal(
					Utils.formatMessage("§cThere is  no active RBT.",
							context.getSource().isExecutedByPlayer())
			));
			return 1;
		}

		Rbt.tourney = null;


		Utils.broadcastMessage("§bThe RBT has ended!");

		return 1;
	}

	public int runWinner(CommandContext<ServerCommandSource> context) {

		// If no active tourney, dont do anything.
		if (Rbt.tourney == null) {
			context.getSource().sendMessage(Text.literal(
					Utils.formatMessage("§cThere is  no active RBT.",
							context.getSource().isExecutedByPlayer())
			));
			return 1;
		}

		String winner = StringArgumentType.getString(context, "winner");

		ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(winner);

		if (player == null) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage(
					"§cPlayer " + winner + " could not be found",
					context.getSource().isExecutedByPlayer()
			)));
			return 1;
		}

		Rbt.tourney = null;

		Utils.broadcastMessage("§3" + player.getName().getString() + "§b has won the RBT!");

		return 1;
	}
}
