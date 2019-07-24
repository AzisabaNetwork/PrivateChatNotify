package net.azisaba.lgw.privatechatnotify;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.lc.event.LunaChatChannelChatEvent;

import net.azisaba.playersettings.PlayerSettings;
import net.azisaba.playersettings.util.SettingsData;
import net.md_5.bungee.api.ChatColor;

public class PrivateChatNotify extends JavaPlugin implements Listener {

    private final String playSoundKey = "PrivateChatNotify.PlaySound";
    private final String displayTitleKey = "PrivateChatNotify.DisplayTitle";

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getLogger().info(getName() + " enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(getName() + " disabled.");
    }

    @EventHandler
    public void onPrivateChat(LunaChatChannelChatEvent e) {
        // 個人チャットではない場合return
        if ( !e.getChannel().isPersonalChat() ) {
            return;
        }

        // 対象を取得
        String channelName = e.getChannelName(); // ex. siloneco>kuloneco
        Player target = Bukkit.getPlayerExact(channelName.substring(channelName.indexOf(">") + 1));

        // ターゲットがnullならreturn
        if ( target == null ) {
            return;
        }

        // 音とタイトルの設定
        boolean playSound = true;
        boolean displayTitle = true;
        // Pluginのロードを確認
        if ( isEnabledSettingsPlugin() ) {
            // 設定をロード
            SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(target);

            // 取得
            if ( data.isSet(playSoundKey) ) {
                playSound = data.getBoolean(playSoundKey);
            }
            if ( data.isSet(displayTitleKey) ) {
                displayTitle = data.getBoolean(displayTitleKey);
            }
        }

        if ( playSound ) {
            // ターゲットに音を鳴らす
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, Float.MAX_VALUE, 2f);
        }
        if ( displayTitle ) {
            // タイトルを表示する
            target.sendTitle("", ChatColor.YELLOW + e.getPlayer().getName() + "からの個人チャット", 0, 10, 10);
        }
    }

    // 設定Pluginがロードされているか確認
    private boolean isEnabledSettingsPlugin() {
        return Bukkit.getPluginManager().getPlugin("PlayerSettings") != null;
    }
}
