package us.sparknetwork.core.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.api.event.UserStaffModeEvent;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.util.PlayerCache;

public class StaffModeManager implements Listener {

	private static StaffModeManager instance;
	public JavaPlugin plugin;
	private ItemStack RandomTP;
	private ItemStack FreezePlayer;
	private ItemStack PhaseCompass;
	private ItemStack ExaminePlayer;
	private ItemStack MinerTP;
	private ItemStack BetterView;
	private ArrayList<UUID> staffmodeplayers = new ArrayList<>();
	private HashMap<UUID, PlayerCache> cachedplayers = new HashMap<>();

	private boolean HCF;

	public StaffModeManager(JavaPlugin plugin) {
		this.plugin = plugin;
		instance = this;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		HCF = plugin.getServer().getPluginManager().getPlugin("HCF") != null;

		RandomTP = CorePlugin.getPlugin().getItemDb().getItem("347", 1);
		ItemMeta rtpim = RandomTP.getItemMeta();
		rtpim.setDisplayName(CoreConstants.GOLD + "Random TP");
		RandomTP.setItemMeta(rtpim);

		FreezePlayer = new ItemStack(Material.PACKED_ICE);
		ItemMeta fpim = FreezePlayer.getItemMeta();
		fpim.setDisplayName(CoreConstants.GOLD + "Freeze Player");
		FreezePlayer.setItemMeta(fpim);

		this.PhaseCompass = new ItemStack(Material.COMPASS);
		ItemMeta pcim = PhaseCompass.getItemMeta();
		pcim.setDisplayName(CoreConstants.GOLD + "Phase Compass");
		PhaseCompass.setItemMeta(pcim);

		this.ExaminePlayer = new ItemStack(Material.BOOK);
		ItemMeta epim = ExaminePlayer.getItemMeta();
		epim.setDisplayName(CoreConstants.GOLD + "Check Player Inventory");
		ExaminePlayer.setItemMeta(epim);

		this.BetterView = new ItemStack(Material.CARPET);
		ItemMeta bvm;
		(bvm = this.BetterView.getItemMeta()).setDisplayName(null);
		this.BetterView.setItemMeta(bvm);

		this.MinerTP = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta mtpim = MinerTP.getItemMeta();
		mtpim.setDisplayName(CoreConstants.GOLD + "Miner TP");
		MinerTP.setItemMeta(mtpim);

	}

	public static StaffModeManager getInstance() {
		return instance;
	}

	public void setStaffMode(Player player, boolean bool) {
		if (bool) {
			this.cachedplayers.put(player.getUniqueId(), new PlayerCache(player));
			player.getInventory().clear();
			player.getInventory().setItem(0, RandomTP);
			player.getInventory().setItem(1, FreezePlayer);
			player.getInventory().setItem(8, ExaminePlayer);
			player.getInventory().setItem(7, PhaseCompass);
			if (HCF) {
				try {
					Class<?> csclass = Class.forName("us.sparknetwork.hcf.ConfigurationService");
					Field field = csclass.getField("KIT_MAP");
					Boolean kitmap = field.getBoolean(null);
					if (!kitmap) {
						player.getInventory().setItem(2, MinerTP);
						player.getInventory().setItem(3, BetterView);
					} else {
						player.getInventory().setItem(2, BetterView);
					}
				} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e1) {
					player.getInventory().setItem(2, BetterView);
				}
			} else {
				player.getInventory().setItem(2, BetterView);
			}
			player.setGameMode(GameMode.CREATIVE);
			staffmodeplayers.add(player.getUniqueId());
			UserStaffModeEvent e = new UserStaffModeEvent(player, true);
			Bukkit.getServer().getPluginManager().callEvent(e);
			return;
		}

		player.getInventory().clear();
		staffmodeplayers.remove(player.getUniqueId());

		if (cachedplayers.containsKey(player.getUniqueId())) {
			PlayerCache pcache = cachedplayers.get(player.getUniqueId());
			pcache.apply(player, false);
			cachedplayers.remove(player.getUniqueId());
		}

		if (!player.hasPermission("core.command.gamemode")) {
			player.setGameMode(GameMode.SURVIVAL);
		}

		UserStaffModeEvent e = new UserStaffModeEvent(player, bool);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return;
	}

	public boolean isInStaffMode(Player player) {
		return staffmodeplayers.contains(player.getUniqueId());
	}

	@EventHandler
	public void pickupEvent(PlayerPickupItemEvent e) {
		if (staffmodeplayers.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void dropEvent(PlayerDropItemEvent e) {
		if (staffmodeplayers.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void blockBreakEvent(BlockBreakEvent e) {
		if (staffmodeplayers.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent e) {
		if (staffmodeplayers.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void gameModeEvent(PlayerGameModeChangeEvent e) {
		if (staffmodeplayers.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player player = (Player) e.getWhoClicked();
			if (this.staffmodeplayers.contains(player.getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (e.getPlayer().getItemInHand() == null) {
			return;
		}
		if (e.getItem() == null) {
			return;
		}
		if (!staffmodeplayers.contains(e.getPlayer().getUniqueId())) {
			return;
		}
		if (e.getItem().equals(RandomTP)) {
			if (Bukkit.getOnlinePlayers().size() == 1) {
				e.getPlayer().sendMessage(CoreConstants.GOLD + "No players online");
				return;
			}
			Random rnd = new Random();
			List<Player> players = new ArrayList<>();
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.equals(e.getPlayer())) {
					continue;
				}
				players.add(player);
			}

			e.getPlayer().teleport(players.get(rnd.nextInt(players.size())), TeleportCause.PLUGIN);
			players.clear();
		}
		if (e.getItem().equals(MinerTP)) {
			if (Bukkit.getOnlinePlayers().size() == 1) {
				e.getPlayer().sendMessage(CoreConstants.GOLD + "No players online");
				return;
			}
			List<Player> miners = new ArrayList<>();
			Bukkit.getOnlinePlayers().forEach(p -> {
				if (p.equals(e.getPlayer()))
					return;
				if (p.getLocation().getY() < 50) {
					miners.add(p);
				}
			});
			if (miners.size() == 0) {
				e.getPlayer().sendMessage(CoreConstants.GOLD + "No miners online");
				return;
			}
			Random rnd = new Random();
			e.getPlayer().teleport(miners.get(rnd.nextInt(miners.size())), TeleportCause.PLUGIN);
			miners.clear();
		}
	}

	@EventHandler
	public void entityinteract(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof Player) {
			Player target = (Player) e.getRightClicked();
			Player player = e.getPlayer();
			ItemStack item = player.getItemInHand();
			if (staffmodeplayers.isEmpty()) {
				return;
			}
			if (!staffmodeplayers.contains(e.getPlayer().getUniqueId())) {
				return;
			}
			if (item.equals(FreezePlayer)) {
				IUser user = UserManager.getInstance().getUser(target.getUniqueId());
				if (!user.isFreezed()) {
					user.setFreezed(true);
					player.sendMessage(CoreConstants.YELLOW
							+ "You freezed the player %player%".replace("%player%", target.getName()));
					return;
				}
				user.setFreezed(false);
				player.sendMessage(CoreConstants.YELLOW
						+ "You unfreezed the player %player%".replace("%player%", target.getName()));
				return;
			}
			if (item.equals(ExaminePlayer)) {
				Inventory inv_ = target.getInventory();
				Inventory inv = Bukkit.createInventory(null, 45, "Inventory".replace("%player%", target.getName()));

				inv.setContents(inv_.getContents());
				player.openInventory(inv);
			}
		}
	}
}
