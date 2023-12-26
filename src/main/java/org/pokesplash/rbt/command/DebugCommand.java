package org.pokesplash.rbt.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.rbt.Rbt;
import org.pokesplash.rbt.util.LuckPermsUtils;

public class DebugCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("debug")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission
								+ ".debug");
					} else {
						return true;
					}
				})
				.executes(this::run)
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
			return 1;
		}

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer());

		for (int x = 0; x < 6; x++) {
			Pokemon pokemon = party.get(x);

			if (pokemon == null) {
				continue;
			}

			context.getSource().sendMessage(Text.literal("§3" + pokemon.getDisplayName().getString() +
					" is " + (pokemon.getPersistentData().getBoolean("unbreedable") ? "§cUnbreedable" :
					"§2Breedable")));
		}

		return 1;
	}
}
