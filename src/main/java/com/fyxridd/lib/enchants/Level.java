package com.fyxridd.lib.enchants;

import com.fyxridd.lib.core.api.hashList.ChanceHashList;
import com.fyxridd.lib.core.api.hashList.ChanceHashListImpl;

import java.util.Random;

public class Level {
    private static final Random r = new Random();

    //1,2,3
    int mode;

    //mode 1
    int level;

    //mode 2
    int min,max;

    //mode 3
    ChanceHashList<Integer> chances;

    public Level(int level) {
        mode = 1;
        this.level = level;
    }

    public Level(int min, int max) {
        mode = 2;
        this.min = min;
        this.max = max;
    }

    public Level(ChanceHashList<Integer> chances) {
        mode = 3;
        this.chances = chances;
    }

    /**
     * 获取随机等级
     * @return 随机等级,>=0
     */
    public int getRandomLevel() {
        switch (mode) {
            case 1:
                return level;
            case 2:
                return r.nextInt(max-min+1)+min;
            case 3:
                return chances.getRandom();
        }
        //异常
        return 0;
    }

    /**
     * 从字符串中读取等级信息
     * @param s 定义等级信息字符串
     * @return 异常返回null
     */
    public static Level load(String s) {
        try {
            Level level;
            if (s.contains("-")) {
                int min = Integer.parseInt(s.split("\\-")[0]);
                int max = Integer.parseInt(s.split("\\-")[1]);
                if (min < 0 || min > max) return null;
                level = new Level(min, max);
            }else if (s.contains(":")) {
                ChanceHashList<Integer> chances = new ChanceHashListImpl<Integer>();
                for (String temp:s.split(";")) {
                    int num = Integer.parseInt(temp.split(":")[0]);
                    int chance = Integer.parseInt(temp.split(":")[1]);
                    if (num < 0 || chance < 0) return null;
                    chances.addChance(num, chance);
                }
                level = new Level(chances);
            }else {
                int num = Integer.parseInt(s);
                if (num < 0) return null;
                level = new Level(num);
            }
            return level;
        } catch (Exception e) {
            return null;
        }
    }
}
