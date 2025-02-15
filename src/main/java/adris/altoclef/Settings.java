package adris.altoclef;

import adris.altoclef.util.KillAura;
import adris.altoclef.util.csharpisbetter.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Settings {

    public static final String SETTINGS_PATH = "altoclef_settings.json";

    /**
     * If true, text will appear on the top left showing the current
     * task chain.
     */
    private boolean showTaskChains = true;

    /**
     * Whenever we're moving, speed up our client by a multiple of this number.
     * Useful when traveling long distances, and only is enabled when we're moving and not mining.
     *
     * Set to 1 for this to have no effect.
     */
    private float speedHack = 1.0f;

    /**
     * The delay between moving items for crafting/furnace/any kind of inventory movement.
     */
    private float containerItemMoveDelay = 0.08f;

    /**
     * If a dropped resource item is further than this from the player, don't pick it up.
     *
     * -1 (or less than 0) to disable.
     */
    private float resourcePickupDropRange = -1;

    /**
     * Chests are cached for their contents.
     *
     * If the bot is collecting a resource and finds a chest within this range,
     * it will grab the resource from the chest.
     *
     * Set this to 0 to disable chest pickups.
     *
     * Don't set this too high, as the bot will prioritize chests even if the resource
     * is easily accesible now.
     *
     */
    private float resourceChestLocateRange = 500;

    /**
     * Some block resources are obtained through non-mining means
     * (like a crafting table or stone block, which can be crafted or smelted).
     *
     * However, if the block resource is found within this range it will be mined first.
     *
     * Set this to 0 to disable this feature
     * (keep in mind, this will not affect blocks like "dirt" and "cobblestone"
     * that can only be obtained through mining)
     *
     * Set this to -1 to ALWAYS mine a block if it's catalogued.
     * This is not recommended. For example, if the bot happens to track a
     * crafting table 10000 blocks away, and it then tries obtaining one
     * it will travel 10000 blocks to mine that table, even if it finds
     * itself in a forest where the wood is abundant.
     */
    private float resourceMineRange = 100;

    /**
     * When going to the nearest chest to store items, the bot may normally
     * dig up dungeons constantly. If this is set to true, the bot will
     * search around each chest to make sure it's not in a dungeon.
     */
    private boolean avoidSearchingDungeonChests = true;


    /**
     * Some larger special tasks will perform extra preparation work to ensure your player
     * has the most efficient tools for the job at hand.
     *
     * For instance, the Place Signs task might first acquire a diamond axe to ease the collection of wood.
     * if "sharpenAxe" is disabled, it won't grab the axe and will collect wood with the best tool it currently has.
     */
    //private boolean sharpenAxe = true;

    /**
     * Before grabbing ANYTHING, get a pickaxe.
     *
     * Will help with navigation as sometimes dropped items will be underground,
     * but this behaviour only makes sense in regular minecraft worlds.
     */
    private boolean collectPickaxeFirst = true;

    /**
     * If set to true, crops broken when collecting food will be replanted.
     */
    private boolean replantCrops = true;

    /**
     * Uses killaura to move mobs away and performs survival moves including:
     * - Running away from hostile mobs when your health is low
     * - Run away from creepers about to blow up
     * - Avoid wither skeletons and other really dangerous mobs
     * - Attempting to dodge arrows and other projectiles
     */
    private boolean mobDefense = true;

    /**
     * Defines how killaura behaves when "mobDefense" is set to true.
     *
     *
     * Strategies:
     *
     *      FASTEST: All hostiles are attacked at every possible moment, every frame.
     *      SMART: Closest hostile is attacked at max every 0.2 seconds.
     */
    private KillAura.Strategy forceFieldStrategy = KillAura.Strategy.FASTEST;

    /**
     * Only applies if mobDefense is on.
     *
     * If enabled, will attempt to dodge all incoming projectiles
     */
    private boolean dodgeProjectiles = true;

    /**
     * Skeletons and large groups of mobs are a huge pain.
     *
     * With this set to true, the bot may either
     * kill or run away from mobs that stay too close for too long.
     *
     */
    private boolean killOrAvoidAnnoyingHostiles = true;

    /**
     * If enabled, the bot will avoid going underwater if baritone
     * isn't giving the bot movement instructions.
     *
     * Baritone doesn't know how to move underwater so this should cause
     * no problems, but disable it if you want the bot to be able to sink.
     *
     */
    private boolean avoidDrowning = true;

    /**
     * If true, eat when we're hungry or in danger.
     */
    private boolean autoEat = true;

    /**
     * If true, MLG/No Fall Bucket if we're knocked off course and falling.
     */
    private boolean autoMLGBucket = true;

    /**
     * If true, will automatically reconnect to the last open server if you get disconnected.
     *
     * If disabled, the bot will stop running when you disconnect from a server.
     */
    private boolean autoReconnect = true;

    /**
     * If true, will automatically respawn instantly if you die.
     *
     * If disabled, the bot will stop running when you die.
     */
    private boolean autoRespawn = true;

    /**
     * If true, will use blacklist for rejecting users from using your player as a butler
     */
    private boolean useButlerBlacklist = true;
    /**
     * If true, will use whitelist to only accept users from said whitelist.
     */
    private boolean useButlerWhitelist = true;

    /**
     *
     *
     * Servers have different messaging plugins that change the way messages are displayed.
     * Rather than attempt to implement all of them and introduce a big security risk,
     * you may define custom whisper formats that the butler will watch out for.
     *
     * Within curly brackets are three special parts:
     *
     * {from}: Who the message was sent from
     * {to}: Who the message was sent to, butler will ignore if this is not your username.
     * {message}: The message.
     *
     *
     * WARNING: The butler will only accept non-chat messages as commands, but don't make this too lenient,
     *      else you may risk unauthorized control to the bot. Basically, make sure that only whispers can
     *      create the following messages.
     */
    private String[] whisperFormats = new String[] {
            "{from} whispers to you: {message}",
            "{from} whispers: {message}",
            "\\[{from} -> {to}\\] {message}"
    };

    /**
     * If true, the bot will perform basic survival tasks when no commands are in progress
     * (eat food, force field mobs, etc.)
     * It will only perform survival tasks allowed by other parameters in the settings file.
     */
    private boolean idleWhenNotActive = false;

    /**
     * If we need to throw away something, throw away these items first.
     */
    private int[] throwawayItems = new int[] {
            // Overworld junk
            Item.getRawId(Items.DIORITE),
            Item.getRawId(Items.ANDESITE),
            Item.getRawId(Items.GRANITE),
            Item.getRawId(Items.COBBLESTONE),
            Item.getRawId(Items.DIRT),
            Item.getRawId(Items.GRAVEL),
            // Nether junk, to be fair it's mostly tuned for the "beat game" task
            Item.getRawId(Items.NETHERRACK),
            Item.getRawId(Items.MAGMA_BLOCK),
            Item.getRawId(Items.SOUL_SOIL),
            Item.getRawId(Items.SOUL_SAND),
            Item.getRawId(Items.NETHER_BRICKS),
            Item.getRawId(Items.NETHER_BRICK)
    };

    /**
     * If we need to throw away something but we don't have any "throwaway Items",
     * throw away any unimportant item that's not currently needed in our task chain.
     *
     * Careful with this! If true, any item not in "importantItems" is liable to be thrown away.
     */
    private boolean throwAwayUnusedItems = false;

    /**
     * We will NEVER throw away these items.
     * Even if "throwAwayUnusedItems" is true and one of these items is not used in a task.
     */
    private int[] importantItems = new int[] {
            Item.getRawId(Items.ENCHANTED_GOLDEN_APPLE),
            Item.getRawId(Items.ENDER_EYE),
            // Don't throw away shulker boxes that would be pretty bad lol
            Item.getRawId(Items.SHULKER_BOX),
            Item.getRawId(Items.BLACK_SHULKER_BOX),
            Item.getRawId(Items.BLUE_SHULKER_BOX),
            Item.getRawId(Items.BROWN_SHULKER_BOX),
            Item.getRawId(Items.CYAN_SHULKER_BOX),
            Item.getRawId(Items.GRAY_SHULKER_BOX),
            Item.getRawId(Items.GREEN_SHULKER_BOX),
            Item.getRawId(Items.LIGHT_BLUE_SHULKER_BOX),
            Item.getRawId(Items.LIGHT_GRAY_SHULKER_BOX),
            Item.getRawId(Items.LIME_SHULKER_BOX),
            Item.getRawId(Items.MAGENTA_SHULKER_BOX),
            Item.getRawId(Items.ORANGE_SHULKER_BOX),
            Item.getRawId(Items.PINK_SHULKER_BOX),
            Item.getRawId(Items.PURPLE_SHULKER_BOX),
            Item.getRawId(Items.RED_SHULKER_BOX),
            Item.getRawId(Items.WHITE_SHULKER_BOX),
            Item.getRawId(Items.YELLOW_SHULKER_BOX)
    };

    /**
     * These areas will not be mined.
     * Used to prevent griefing
     * or to define a "spawn protection" zone so
     * the bot doesn't keep trying to break spawn protected
     * blocks.
     */
    private ProtectionRange[] areasToProtect = new ProtectionRange[] {

    };

    // Internal tracking of whether we're dirty or not.
    private transient boolean _dirty;

    public static Settings load() {

        File loadFrom = new File(SETTINGS_PATH);
        if (!loadFrom.exists()) {
            Settings result = new Settings();
            result.markDirty();
            result.save();
            return result;
        }

        String data;
        try {
            data = new String(Files.readAllBytes(Paths.get(SETTINGS_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new Gson();

        Settings result = gson.fromJson(data, Settings.class);
        result.markDirty();
        result.save();

        for (ProtectionRange protection : result.areasToProtect) {
            Debug.logInternal("Debug: Protection range: " + protection);
        }

        return result;
    }

    private static void save(Settings settings) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String userJson = gson.toJson(settings);

        try {
            Files.write(Paths.get(SETTINGS_PATH), userJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Dirty managing
    private void markDirty() {
        _dirty = true;
    }
    public boolean isDirty() {
        return _dirty;
    }

    public void save() {
        if (!_dirty) return;
        save(this);
        _dirty = false;
    }

    public boolean shouldShowTaskChain() { return showTaskChains; }

    public void setSpeedHack(float value) {
        speedHack = value; markDirty();
    }
    public float getSpeedHack() {
        return speedHack;
    }

    public float getResourcePickupRange() {return resourcePickupDropRange;}

    public float getResourceChestLocateRange() {return resourceChestLocateRange;}

    public float getResourceMineRange() {return resourceMineRange;}

    public float getContainerItemMoveDelay() {
        return containerItemMoveDelay;
    }

    public boolean isMobDefense() {
        return mobDefense;
    }
    public void setMobDefense(boolean mobDefense) {
        this.mobDefense = mobDefense; markDirty();
    }

    public boolean isDodgeProjectiles() {
        return dodgeProjectiles;
    }
    public void setDodgeProjectiles(boolean dodgeProjectiles) {
        this.dodgeProjectiles = dodgeProjectiles; markDirty();
    }

    public boolean isAutoEat() {
        return autoEat;
    }
    public void setAutoEat(boolean autoEat) {
        this.autoEat = autoEat;
        markDirty();
    }
    public boolean isAutoReconnect() {
        return autoReconnect;
    }
    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public boolean isAutoRespawn() {
        return autoRespawn;
    }
    public void setAutoRespawn(boolean autoRespawn) {
        this.autoRespawn = autoRespawn;
    }

    public boolean shouldReplantCrops() {return replantCrops;}

    public boolean isUseButlerBlacklist() {
        return useButlerBlacklist;
    }
    public void setUseButlerBlacklist(boolean useButlerBlacklist) {
        this.useButlerBlacklist = useButlerBlacklist;
    }

    public boolean isUseButlerWhitelist() {
        return useButlerWhitelist;
    }
    public void setUseButlerWhitelist(boolean useButlerWhitelist) {
        this.useButlerWhitelist = useButlerWhitelist;
    }

    public boolean shouldDealWithAnnoyingHostiles() {return killOrAvoidAnnoyingHostiles;}

    public KillAura.Strategy getForceFieldStrategy() {return forceFieldStrategy;}

    public boolean shouldIdleWhenNotActive() {return idleWhenNotActive;}

    public boolean shouldAutoMLGBucket() {
        return autoMLGBucket;
    }

    public boolean shouldCollectPickaxeFirst() { return collectPickaxeFirst; }

    public boolean shouldAvoidDrowning() {return avoidDrowning;}

    public boolean shouldAvoidSearchingForDungeonChests() {return avoidSearchingDungeonChests;}

    public boolean isThrowaway(Item item) {
        return idArrayContainsItem(item, throwawayItems);
    }
    public boolean isImportant(Item item) {
        return idArrayContainsItem(item, importantItems);
    }
    public boolean shouldThrowawayUnusedItems() {
        return this.throwAwayUnusedItems;
    }
    public Item[] getThrowawayItems(AltoClef mod) {
        List<Item> result = new ArrayList<>();
        for (int throwawayItem : throwawayItems) {
            Item item = Item.byRawId(throwawayItem);
            if (!mod.getConfigState().isProtected(item)) {
                result.add(item);
            }
        }
        return Util.toArray(Item.class, result);
    }
    public Item[] getThrowawayItemsRaw() {
        Item[] result = new Item[throwawayItems.length];
        for (int i = 0; i < throwawayItems.length; ++i) {
            result[i] = Item.byRawId(throwawayItems[i]);
        }
        return result;
    }

    public String[] getWhisperFormats() {return whisperFormats;}

    public boolean isPositionExplicitelyProtected(BlockPos pos) {
        for (ProtectionRange protection : areasToProtect) {
            if (protection.includes(pos)) return true;
        }
        return false;
    }

    private static boolean idArrayContainsItem(Item item, int[] ids) {
        int id = Item.getRawId(item);
        for (int check : ids) {
            if (check == id) return true;
        }
        return false;
    }

    private static class ProtectionRange {
        public BlockPos start;
        public BlockPos end;

        public boolean includes(BlockPos pos) {
            return (start.getX() <= pos.getX() && pos.getX() <= end.getX() &&
                    start.getZ() <= pos.getZ() && pos.getZ() <= end.getZ() &&
                    start.getY() <= pos.getY() && pos.getY() <= end.getY());
        }

        public String toString() {
            return "[" + start.toShortString() + " -> " + end.toShortString() + "]";
        }
    }
}
