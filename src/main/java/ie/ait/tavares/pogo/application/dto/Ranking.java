package ie.ait.tavares.pogo.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Ranking {

    private RankDetails evaluatedMaster;
    private RankDetails evaluatedUltra;
    private RankDetails evaluatedGreat;

    private RankDetails perfectMaster;
    private RankDetails perfectUltra;
    private RankDetails perfectGreat;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RankDetails {
        private long rank;
        private int attack;
        private int defense;
        private int hp;
        private int level;
        private int combaPower;
        private long perfection;

        @Override
        public String toString() {
            return "Rank Details: " +
                    "Rank=" + rank +
                    "\t| IVs=" + attack +
                    "/" + defense +
                    "/" + hp +
                    "\t| Lvl=" + level +
                    "\t| CP=" + combaPower +
                    "\t| % perfection=" + perfection;
        }
    }
}
