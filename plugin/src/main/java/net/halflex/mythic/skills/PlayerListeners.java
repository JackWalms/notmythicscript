package net.halflex.mythic.skills;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import eu.asangarin.mythickeys.api.MythicKeyPressEvent;
import eu.asangarin.mythickeys.api.MythicKeyReleaseEvent;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.adapters.BukkitTriggerMetadata;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.core.skills.SkillTriggers;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.player.profile.Profile;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerListeners extends ReloadableModule<NotMythicScript> {
    public PlayerListeners(NotMythicScript plugin) {
        super(plugin);
    }

    @Override
    public void load(NotMythicScript notMythicScript) {
        Events.subscribe(PlayerToggleSneakEvent.class)
                .handler(this::onPlayerCrouch).bindWith(this);
        Events.subscribe(EntityDamageByEntityEvent.class)
                .handler(this::onCombat).bindWith(this);
        Events.subscribe(PlayerDeathEvent.class)
                .handler(this::onPlayerDeath).bindWith(this);
        Events.subscribe(PlayerJoinEvent.class)
                .handler(this::onPlayerJoin).bindWith(this);
        Events.subscribe(PlayerRespawnEvent.class)
                .handler(this::onPlayerRespawn).bindWith(this);
        Events.subscribe(EntityDeathEvent.class)
                .handler(this::onKillEntity).bindWith(this);
        Events.subscribe(EntityShootBowEvent.class)
                .handler(this::onPlayerBowShoot).bindWith(this);
        Events.subscribe(EntityDamageEvent.class)
                .handler(this::onDamaged).bindWith(this);
        Events.subscribe(PlayerInteractEvent.class)
                .handler(this::onInteract).bindWith(this);
        Events.subscribe(PlayerInteractAtEntityEvent.class)
                .handler(this::onInteractEntity).bindWith(this);
        Events.subscribe(BlockBreakEvent.class)
                .handler(this::onBlockBreak).bindWith(this);
        Events.subscribe(BlockPlaceEvent.class)
                .handler(this::onBlockPlace).bindWith(this);
        Events.subscribe(PlayerArmorChangeEvent.class)
                .handler(this::onArmorEquip).bindWith(this);
        Events.subscribe(PlayerDropItemEvent.class)
                .handler(this::onDropItem).bindWith(this);
        Events.subscribe(PlayerJumpEvent.class)
                .handler(this::onJump).bindWith(this);
        Events.subscribe(PlayerItemConsumeEvent.class)
                .handler(this::onConsumeItem).bindWith(this);
        Events.subscribe(SheepDyeWoolEvent.class)
                .handler(this::onDyeSheep).bindWith(this);


        if (MythicBukkit.inst().getCompatibility().getMythicKeys().isPresent()){
            Events.subscribe(MythicKeyPressEvent.class).handler(event -> {
                final Player player = event.getPlayer();

                getProfile(player).runSkills(SkillTriggers.PRESS, BukkitAdapter.adapt(player.getLocation()), BukkitAdapter.adapt(player), false, (meta) -> meta.getVariables().putString("key-id", event.getId().toString()));
            }).bindWith(this);

            Events.subscribe(MythicKeyReleaseEvent.class).handler(event -> {
                final Player player = event.getPlayer();
                getProfile(player).runSkills(SkillTriggers.RELEASE, BukkitAdapter.adapt(player.getLocation()), BukkitAdapter.adapt(player), false, (meta) -> meta.getVariables().putString("key-id", event.getId().toString()));
            }).bindWith(this);
        }


    }

    @Override
    public void unload() {

    }

    private void onPlayerCrouch(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        Profile caster = plugin.profileManager().getProfile(player);

        if (event.isSneaking()){
            caster.runSkills(SkillTriggers.CROUCH, caster.getEntity());
        } else {
            caster.runSkills(SkillTriggers.UNCROUCH, caster.getEntity());
        }
    }

    private void onPlayerDeath(PlayerDeathEvent event){
        Player killed = event.getEntity();
        Entity killer = killed.getKiller();

        if(killer == null)   {
            EntityDamageEvent entityDamageEvent = event.getEntity().getLastDamageCause();
            if(entityDamageEvent != null && !entityDamageEvent.isCancelled() && (entityDamageEvent instanceof EntityDamageByEntityEvent byEntityEvent)) {
                killer = byEntityEvent.getDamager();
            }
        }
        if(killer instanceof Projectile proj)    {
            ProjectileSource ps = proj.getShooter();

            if(ps instanceof LivingEntity)  {
                killer = (Entity) ps;
            }
        }

        if(killer instanceof Player playerKiller)   {
            final Profile killerProfile = getProfile(playerKiller);
            killerProfile.runSkills(SkillTriggers.KILLPLAYER, BukkitAdapter.adapt(killed));
        }

        final Profile killedProfile = getProfile(killed);
        killedProfile.runSkills(SkillTriggers.DEATH, BukkitAdapter.adapt(killer));

    }

    private void onKillEntity(EntityDeathEvent event){
        LivingEntity killed = event.getEntity();
        Entity killer = killed.getKiller();

        if(killer == null)   {
            var entityDamageEvent = event.getEntity().getLastDamageCause();
            if(entityDamageEvent != null && !entityDamageEvent.isCancelled() && (entityDamageEvent instanceof EntityDamageByEntityEvent byEntityEvent)) {
                killer = byEntityEvent.getDamager();
            }
        }
        if(killer instanceof Projectile proj)    {
            ProjectileSource ps = proj.getShooter();

            if(ps instanceof Entity)  {
                killer = (Entity) ps;
            }
        }

        if(killer instanceof Player playerKiller)   {
            final Profile killerProfile = getProfile(playerKiller);
            killerProfile.runSkills(SkillTriggers.KILL, BukkitAdapter.adapt(killed));
        }

    }

    private void onDamaged(EntityDamageEvent e) {
        if(e.getEntity() instanceof final Player player) {
            final Profile gs = getProfile(player);

            if(!(e instanceof EntityDamageByEntityEvent)) {
                final boolean c = gs.runSkills(
                        SkillTriggers.DAMAGED,
                        BukkitAdapter.adapt(player.getLocation()),
                        BukkitAdapter.adapt(player), true,
                        (meta) -> BukkitTriggerMetadata.apply(meta,e));
                if(c)   {
                    e.setCancelled(true);
                }
            }
        }
    }

    private void onCombat(EntityDamageByEntityEvent event) {
        Profile gs = null;
        boolean projectile = false;

        if(event.getDamager() instanceof Player p)    {
            gs = getProfile(p);
        } else if(event.getDamager() instanceof Projectile proj) {
            projectile = true;

            if(proj.getShooter() instanceof Player p)    {
                gs = getProfile(p);
            }
        }

        if(gs != null)   {
            final boolean c;
            if(projectile)  {
                c = gs.runSkills(SkillTriggers.BOW_HIT, BukkitAdapter.adapt(event.getEntity()));
            } else  {
                c = gs.runSkills(SkillTriggers.ATTACK, BukkitAdapter.adapt(event.getEntity()));
            }
            if(c)   {
                event.setCancelled(true);
            }
        }

        if(event.getEntity() instanceof Player p) {
            gs = getProfile(p);

            LivingEntity damager = null;

            if(event.getDamager() instanceof LivingEntity le)    {
                damager = le;
            } else if(event.getDamager() instanceof Projectile proj) {
                if(proj.getShooter() instanceof LivingEntity le)    {
                    damager = le;
                }
            }

            if(damager != null) {
                final boolean c = gs.runSkills(SkillTriggers.DAMAGED, BukkitAdapter.adapt(damager.getLocation()), BukkitAdapter.adapt(damager), true, (meta) -> BukkitTriggerMetadata.apply(meta,event));
                if(c)   {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void onInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if(player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        final Profile gs = getProfile(e.getPlayer());

        switch (e.getAction()) {
            case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> {
                final boolean c = gs.runSkills(SkillTriggers.SWING, gs.abstractPlayer());
                if (c) {
                    e.setCancelled(true);
                }
            }
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                if (player.getInventory().getItemInMainHand() != null
                        && player.getInventory().getItemInMainHand().getType() != Material.AIR
                        && e.getHand() == EquipmentSlot.HAND) {
                    final boolean cc = gs.runSkills(SkillTriggers.USE, gs.abstractPlayer());
                    if (cc) {
                        e.setCancelled(true);
                    }
                    break;
                }
                try {
                    final boolean ccc = gs.runSkills(SkillTriggers.RIGHTCLICK, gs.abstractPlayer());
                    if (ccc) {
                        e.setCancelled(true);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void onInteractEntity(PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();

        if(player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        final Profile gs = getProfile(player);

        final boolean c = gs.runSkills(SkillTriggers.INTERACT, BukkitAdapter.adapt(event.getRightClicked()));
        if(c)   {
            event.setCancelled(true);
        }
    }

    private void onPlayerJoin(PlayerJoinEvent event){
        Profile caster = plugin.profileManager().getProfile(event.getPlayer());
        caster.runSkills(SkillTriggers.JOIN, caster.abstractPlayer());
    }

    private void onPlayerRespawn(PlayerRespawnEvent event){
        Profile caster = plugin.profileManager().getProfile(event.getPlayer());
        caster.runSkills(SkillTriggers.RESPAWN, caster.abstractPlayer());
    }

    private void onPlayerBowShoot(EntityShootBowEvent event)   {
        if(!(event.getEntity() instanceof Player player)) return;

        final Profile caster = getProfile(player);

        final boolean c = caster.runSkills(SkillTriggers.SHOOT, BukkitAdapter.adapt(player.getLocation()), caster.abstractPlayer(), true, (meta) -> {
            meta.getVariables().putFloat("bow-tension", event.getForce());
            meta.setPower(event.getForce());
        });

        if(c)   {
            event.setCancelled(true);
        }
    }

    private void onBlockPlace(BlockPlaceEvent event) {
        final Profile caster = getProfile(event.getPlayer());
        final AbstractLocation origin = BukkitAdapter.adapt(event.getBlock().getLocation());

        final boolean b = caster.runSkills(SkillTriggers.BLOCK_PLACE, origin, caster.abstractPlayer());
        if (b){
            event.setCancelled(true);
        }
    }

    private void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Profile caster = getProfile(player);
        final AbstractLocation origin = BukkitAdapter.adapt(event.getBlock().getLocation());

        final boolean b = caster.runSkills(SkillTriggers.BLOCK_BREAK, origin, caster.abstractPlayer());
        if(b)   {
            event.setCancelled(true);
        }
    }

    private void onArmorEquip(PlayerArmorChangeEvent event){
        Profile caster = getProfile(event.getPlayer());
        if (event.getNewItem() != null){
            caster.runSkills(SkillTriggers.ARMOR_EQUIP, caster.abstractPlayer());
        }
        if (event.getOldItem() != null){
            caster.runSkills(SkillTriggers.ARMOR_UNEQUIP, caster.abstractPlayer());
        }
    }

    private void onDropItem(PlayerDropItemEvent event){
        Profile caster = getProfile(event.getPlayer());
        AbstractLocation origin = BukkitAdapter.adapt(event.getItemDrop().getLocation());
        boolean b = caster.runSkills(SkillTriggers.PRESS_Q, origin, caster.abstractPlayer());
        if (b){
            event.setCancelled(true);
        }
    }

    private void onJump(PlayerJumpEvent event){
        Profile caster = getProfile(event.getPlayer());
        AbstractLocation origin = BukkitAdapter.adapt(event.getFrom());
        boolean b = caster.runSkills(SkillTriggers.JUMP, origin, caster.abstractPlayer());
        if (b){
            event.setCancelled(true);
        }
    }

    private void onConsumeItem(PlayerItemConsumeEvent event)   {
        Profile caster = getProfile(event.getPlayer());
        boolean b = caster.runSkills(SkillTriggers.CONSUME, caster.abstractPlayer());
        if (b){
            event.setCancelled(true);
        }
    }

    private void onDyeSheep(SheepDyeWoolEvent event){
        Profile caster = getProfile(event.getPlayer());
        boolean b = caster.runSkills(SkillTriggers.CONSUME, BukkitAdapter.adapt(event.getEntity().getLocation()), BukkitAdapter.adapt(event.getEntity()), true, meta -> meta.getVariables().putString("wool-color", event.getColor().toString()));
        if (b){
            event.setCancelled(true);
        }
    }

    private Profile getProfile(Player player){
        return plugin.profileManager().getProfile(player);
    }



}
