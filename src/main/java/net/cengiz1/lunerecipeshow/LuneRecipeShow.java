package net.cengiz1.lunerecipeshow;


import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class LuneRecipeShow extends JavaPlugin implements @NotNull Listener {

    @Override
    public void onEnable() {
        getCommand("esyayapimi").setExecutor(new command(this));
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }


    @Override
    public void onDisable() {
    }
}
