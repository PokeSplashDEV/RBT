package org.pokesplash.rbt.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.config.Participant;
import org.pokesplash.rbt.util.LuckPermsUtils;
import org.pokesplash.rbt.util.Utils;

public class ParticipantCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("players")
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

		// If no active tourney, dont do anything.
		if (Rbt.tourney == null) {
			context.getSource().sendMessage(Text.literal(
					Utils.formatMessage("§cThere is  no active RBT.",
							context.getSource().isExecutedByPlayer())
			));
			return 1;
		}

		String out = "§3Players:\n";

		for (Participant participant : Rbt.tourney.getParticipants()) {
			out += "§b- " + participant.getName() + "\n";
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(
				out.trim(), context.getSource().isExecutedByPlayer()
		)));

		return 1;
	}
}
