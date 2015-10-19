package com.fyxridd.lib.enchants.api;

import com.fyxridd.lib.enchants.EnchantsMain;
import org.bukkit.inventory.ItemStack;

public class EnchantsApi {
    /**
     * 重新读取附魔配置
     * 会读取'插件名/enchants.yml'文件
     */
    public static void reloadEnchants(String plugin) {
        EnchantsMain.reloadEnchants(plugin);
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
