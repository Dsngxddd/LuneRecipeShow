package net.cengiz1.lunerecipeshow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class command implements CommandExecutor {

    private final LuneRecipeShow plugin;


    public command(LuneRecipeShow plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getString("messages.non-player"));
            return true;
        }

        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(config.getString("messages.no-item-in-hand"));
            return true;
        }

        Material material = itemInHand.getType();
        Recipe recipe = findRecipe(material);
        if (recipe != null && recipe instanceof ShapedRecipe) {

            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            Inventory inv = createRecipeInventory(shapedRecipe,"Eşya yapimi");
            player.openInventory(inv);
        } else {
            player.sendMessage(config.getString("messages.no-recipe"));
        }

        return true;
    }


    private Recipe findRecipe(Material material) {
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe recipe = recipes.next();
            if (recipe.getResult().getType() == material) {
                return recipe;
            }
        }
        return null;
    }
    private Inventory createRecipeInventory(ShapedRecipe recipe, String title) {
        Inventory inv = Bukkit.createInventory(null, 54, title);
        int[] craftingSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30};

        String[] shape = recipe.getShape();
        ItemStack[] items = new ItemStack[9];

        for (int row = 0; row < 3; row++) {
            if (row >= shape.length) continue;
            String currentRow = shape[row];
            for (int col = 0; col < 3; col++) {
                if (col >= currentRow.length()) continue;
                char symbol = currentRow.charAt(col);
                items[row * 3 + col] = recipe.getIngredientMap().get(symbol);
            }
        }

        for (int i = 0; i < 9; i++) {
            inv.setItem(craftingSlots[i], items[i]);
        }

        inv.setItem(24, recipe.getResult());

        ItemStack filler = new ItemStack(Material.BROWN_STAINED_GLASS, 64);
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
        }

        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getView().getTitle().contains("Eşya yapimi")) {
            event.setCancelled(true);
        }
    }


}