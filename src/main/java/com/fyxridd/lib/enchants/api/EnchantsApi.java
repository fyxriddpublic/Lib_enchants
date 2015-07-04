package com.fyxridd.lib.enchants.api;

import com.fyxridd.lib.enchants.EnchantsMain;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class EnchantsApi {
    /**
     * @param file yml文件,可为null(null时无效果)
     * @see #reloadEnchants(String, org.bukkit.configuration.file.YamlConfiguration)
     */
    public static void reloadEnchants(String plugin, File file) {
        EnchantsMain.reloadEnchants(plugin, file);
    }

    /**
     * 重新读取附魔配置
     * @param plugin 注册的插件名,可为null(null时无效果)
     * @param config 配置文件,可为null(null时无效果)
     */
    public static void reloadEnchants(String plugin, YamlConfiguration config) {
        EnchantsMain.reloadEnchants(plugin, config);
    }

    /**
     * 给物品增加指定类型的附魔
     * @param plugin 注册的插件名,可为null(null时返回false)
     * @param type 类型,可为null(null时返回false)
     * @param is 物品,可为null(null时返回false)
     * @return 添加是否成功(只要有一个成功)
     */
    public static boolean addEnchant(String plugin, String type, ItemStack is) {
        return EnchantsMain.addEnchant(plugin, type, is);
    }
}
