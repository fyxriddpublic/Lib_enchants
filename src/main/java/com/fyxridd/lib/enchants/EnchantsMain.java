package com.fyxridd.lib.enchants;

import com.fyxridd.lib.core.api.ConfigApi;
import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.event.ReloadConfigEvent;
import com.fyxridd.lib.enchants.api.EnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class EnchantsMain implements Listener{
    private static String savePath;

    //插件名 类型名 附魔信息
    private static HashMap<String, HashMap<String, Enchant>> enchantsHash = new HashMap<>();

    public EnchantsMain() {
        savePath = EnchantsPlugin.dataPath+File.separator+ "enchants.yml";
        //初始化配置
        initConfig();
		//读取配置文件
		loadConfig();
		//注册事件
		Bukkit.getPluginManager().registerEvents(this, EnchantsPlugin.instance);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void onReloadConfig(ReloadConfigEvent e) {
		if (e.getPlugin().equals(EnchantsPlugin.pn)) {
            loadConfig();
        }
	}

    /**
     * @see com.fyxridd.lib.enchants.api.EnchantsApi#addEnchant(String, String, org.bukkit.inventory.ItemStack)
     */
    public static boolean addEnchant(String plugin, String type, ItemStack is) {
        if (plugin == null || type == null || is == null) return false;

        //不存在此注册的插件
        if (!enchantsHash.containsKey(plugin)) return false;

        //此插件未注册此附魔类型
        if (!enchantsHash.get(plugin).containsKey(type)) return false;

        //检测添加
        Enchant enchant = enchantsHash.get(plugin).get(type);
        return enchant.addEnchant(is);
    }


    /**
     * @see com.fyxridd.lib.enchants.api.EnchantsApi#reloadEnchants(String, File)
     */
    public static void reloadEnchants(String plugin, File file) {
        if (plugin == null || file == null) return;
        reloadEnchants(plugin, CoreApi.loadConfigByUTF8(file));
    }

    /**
     * @see com.fyxridd.lib.enchants.api.EnchantsApi#reloadEnchants(String, org.bukkit.configuration.file.YamlConfiguration)
     */
    public static void reloadEnchants(String plugin, YamlConfiguration config) {
        if (plugin == null || config == null) return;

        enchantsHash.put(plugin, new HashMap<String, Enchant>());
        try {
            Map<String, Object> map = config.getValues(false);
            for (String key:map.keySet()) loadEnchant(plugin, config, key);
        } catch (Exception e) {
            ConfigApi.log(EnchantsPlugin.pn, "load plugin " + plugin + "'s enchants error");
        }
    }

    /**
     * 读取指定的附魔配置
     * @param plugin 注册的插件名,不为null
     * @param config 配置文件,不为null
     * @param type 指定的类型,不为null
     */
    @SuppressWarnings("deprecation")
    private static void loadEnchant(String plugin, YamlConfiguration config, String type) {
        //读取
        try {
            //mode
            int mode = config.getInt(type+".mode");
            if (mode < 1) {
                mode = 1;
                ConfigApi.log(EnchantsPlugin.pn, "load plugin "+plugin+"'s type "+type+"'s enchant's mode error");
            }else if (mode > 3) {
                mode = 3;
                ConfigApi.log(EnchantsPlugin.pn, "load plugin "+plugin+"'s type "+type+"'s enchant's mode error");
            }
            //all
            boolean all = config.getBoolean(type+".all");
            //fit
            boolean fit = config.getBoolean(type+".fit");
            //safe
            boolean safe = config.getBoolean(type+".safe");
            //enchants
            HashMap<Enchantment, Level> enchants = new HashMap<Enchantment, Level>();
            for (String s:config.getStringList(type+".enchants")) {
                //enchantment
                String[] ss = s.split(" ");
                Enchantment enchantment = CoreApi.getEnchantment(ss[0]);
                if (enchantment == null) {
                    ConfigApi.log(EnchantsPlugin.pn, "load plugin "+plugin+"'s type "+type+"'s enchant's enchantment error");
                    continue;
                }
                //level
                Level level = Level.load(ss[1]);
                if (level == null) {
                    ConfigApi.log(EnchantsPlugin.pn, "load plugin "+plugin+"'s type "+type+"'s enchant's level error");
                    continue;
                }
                //添加
                enchants.put(enchantment, level);
            }

            //添加
            Enchant enchant = new Enchant(mode, all, fit, safe, enchants);
            enchantsHash.get(plugin).put(type, enchant);
        } catch (Exception e) {
            ConfigApi.log(EnchantsPlugin.pn, "load plugin " + plugin + "'s type " + type + "'s enchant error");
        }
    }

    private void initConfig() {
        ConfigApi.register(EnchantsPlugin.file, EnchantsPlugin.dataPath, EnchantsPlugin.pn, null);
        ConfigApi.loadConfig(EnchantsPlugin.pn);
    }

	private static void loadConfig() {
        //重新读取附魔
        YamlConfiguration saveConfig = CoreApi.loadConfigByUTF8(new File(savePath));
        if (saveConfig == null) {
            ConfigApi.log(EnchantsPlugin.pn, "enchants.yml is error");
            return;
        }
        reloadEnchants(EnchantsPlugin.pn, saveConfig);
	}
}
