package org.pokesplash.rbt.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.config.Tourney;
import org.pokesplash.rbt.util.LuckPermsUtils;
import org.pokesplash.rbt.util.Utils;

public class StartCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("start")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(),
								CommandHandler.basePermission + ".start");
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

		Rbt.tourney.start();


		Utils.broadcastMessage("§bThe RBT has begun!");

		return 1;
	}
}
