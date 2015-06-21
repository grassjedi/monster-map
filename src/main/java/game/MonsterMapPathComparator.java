package game;

import java.util.Comparator;

public class MonsterMapPathComparator implements Comparator<MonsterMapGamePath> {
    public int compare(MonsterMapGamePath o1, MonsterMapGamePath o2) {
        //reverse order
        return (o2.getTreasureCount() - o2.getMonsterCount())
                - (o1.getTreasureCount() - o1.getMonsterCount());
    }
}
