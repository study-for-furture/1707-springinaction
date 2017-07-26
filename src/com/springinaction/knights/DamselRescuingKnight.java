package com.springinaction.knights;

/**
 * Created by mitmo on 2017-07-27.
 */
public class DamselRescuingKnight implements Knight {
    private RescueDamselQuest quest;

    public DamselRescuingKnight(){
        this.quest = new RescueDamselQuest();
    }

    @Override
    public void embarkOnQuest() {

    }
}
