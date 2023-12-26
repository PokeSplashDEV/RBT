package org.pokesplash.rbt.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.util.LuckPermsUtils;
import org.pokesplash.rbt.util.Utils;

public class LeaveCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("leave")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(),
								CommandHandler.basePermission + ".user");
					} else {
						return true;
					}
				})
				.executes(this::run)
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

		boolean hasJoined = Rbt.tourney.removeParticipant(context.getSource().getPlayer());

		if (hasJoined) {
			context.getSource().sendMessage(Text.literal("§bYou have left the RBT."));
		} else {
			context.getSource().sendMessage(Text.literal("§bYou have not joined the RBT."));
		}

		return 1;
	}
}
