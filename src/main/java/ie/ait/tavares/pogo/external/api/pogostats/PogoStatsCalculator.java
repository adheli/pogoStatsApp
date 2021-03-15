package ie.ait.tavares.pogo.external.api.pogostats;

import ie.ait.tavares.pogo.application.League;
import ie.ait.tavares.pogo.application.dto.Ranking;
import ie.ait.tavares.pogo.application.dto.StatsDto;
import ie.ait.tavares.pogo.model.entity.PokemonStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PogoStatsCalculator {

    double[] cpm = {0.09399999678, 0.1351374321, 0.1663978696, 0.1926509132, 0.2157324702, 0.2365726514, 0.2557200491,
            0.2735303721, 0.2902498841, 0.3060573814, 0.3210875988, 0.335445032, 0.3492126763, 0.3624577366,
            0.3752355874, 0.3875924077, 0.3995672762, 0.4111935532, 0.4225000143, 0.4329264205,
            0.4431075454, 0.4530599482, 0.4627983868, 0.4723360853, 0.481684953, 0.4908558072, 0.499858439,
            0.508701749, 0.5173939466, 0.5259425161, 0.5343543291, 0.5426357538, 0.5507926941, 0.5588305845,
            0.5667545199, 0.5745691281, 0.5822789073, 0.5898879079, 0.5974000096, 0.6048236487, 0.6121572852,
            0.619404108, 0.6265671253, 0.6336491787, 0.6406529546, 0.6475809714, 0.6544356346, 0.6612192658,
            0.6679340005, 0.6745818856, 0.6811649203, 0.6876849013, 0.6941436529, 0.700542901, 0.7068842053,
            0.7131690749, 0.7193990946, 0.7255755869, 0.7317000031, 0.7347410386, 0.7377694845, 0.7407855797,
            0.7437894344, 0.7467811972, 0.749761045, 0.7527290997, 0.7556855083, 0.7586303702, 0.7615638375,
            0.7644860496, 0.7673971653, 0.7702972937, 0.7731865048, 0.7760649471, 0.7789327502, 0.7817900508,
            0.7846369743, 0.7874736085, 0.7903000116, 0.792803968, 0.7953000069, 0.7978038984, 0.8003000021,
            0.8028038719, 0.8052999973, 0.8078038508, 0.8102999926, 0.8128038352, 0.8152999878, 0.8178038066,
            0.820299983, 0.8228037786, 0.8252999783, 0.8278037509, 0.8302999735, 0.8328037534, 0.8353000283,
            0.8378037559, 0.8403000236, 0.842803729, 0.8453000188, 0.8478037024, 0.850300014, 0.852803676,
            0.8553000093, 0.8578036499, 0.8603000045, 0.862803624, 0.8652999997};

    double[] cpm2;
    double[] cpm4;

    public PogoStatsCalculator() {
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS

        cpm2 = new double[cpm.length];
        for (int i = 0; i < cpm.length; i++) {
            cpm2[i] = Double.parseDouble(df.format(Math.pow(cpm[i], 2)));
        }

        cpm4 = new double[cpm2.length];
        for (int i = 0; i < cpm2.length; i++) {
            cpm4[i] = Double.parseDouble(df.format(Math.pow(cpm2[i], 2)));
        }
    }

    private double binarySearch(double[] ar, double num) {
        double m = 0;
        int n = ar.length - 1;
        while (m <= n) {
            int k = (int) (n + m) >> 1;
            if (ar[k] < num) {
                m = (double) k + 1;
            } else if (ar[k] > num) {
                n = k - 1;
            } else {
                return k;
            }
        }
        return m - 1;
    }

    private double cp(double attack, double defense, double st, double multiple2) {
        return Math.floor((attack * Math.sqrt(defense * st) * multiple2) / 10);
    }


    private double lvlCap(double at, double df, double st, double targetCP, double maxLvl) {
        double cap = binarySearch(cpm4, ((targetCP + 1) * (targetCP + 1) * 100) / (at * at * df * st));
        return Math.min(cap, maxLvl);
    }

    private IvCombo calculateIvs(PokemonStats poke, IvCombo rawIv, int maxCpLeague) {
        int at = (int) (Math.floor(poke.getAttack()) + rawIv.getAttack());
        int df = (int) (Math.floor(poke.getDefense()) + rawIv.getDefense());
        int st = (int) (Math.floor(poke.getHealthPoints()) + rawIv.getStat());
        int lvl = (int) lvlCap(at, df, st, maxCpLeague, rawIv.getLevel());

        return new IvCombo(at, df, st, lvl);
    }

    private List<ProductStats> generateProductsStats(int maxCpLeague, PokemonStats poke, List<IvCombo> ivComboList) {
        List<ProductStats> products = new ArrayList<>();

        for (int i = 0; i < ivComboList.size(); i++) {
            IvCombo ivCombo = ivComboList.get(i);
            IvCombo calculatedIvs = calculateIvs(poke, ivCombo, maxCpLeague);

            double statProduct = Math.floor(cpm2[calculatedIvs.getLevel()] * calculatedIvs.getAttack() * calculatedIvs.getDefense()) *
                    Math.floor(cpm[calculatedIvs.getLevel()] * calculatedIvs.getStat());


            products.add(i, new ProductStats(statProduct, calculatedIvs.getLevel(),
                    ivCombo.getAttack(), ivCombo.getDefense(), ivCombo.getStat(),
                    calculatedIvs.getAttack(), calculatedIvs.getDefense(), calculatedIvs.getStat()));
        }

        return products.stream().sorted().collect(Collectors.toList());
    }

    private List<IvCombo> getIvCombos(int ivMin, int levelCap) {
        List<IvCombo> ivComboList = new ArrayList<>();

        int index = 0;
        for (int at = ivMin; at <= 15; at++) {
            for (int df = ivMin; df <= 15; df++) {
                for (int st = ivMin; st <= 15; st++) {
                    ivComboList.add(index, new IvCombo(at, df, st, levelCap));
                    index++;
                }
            }
        }
        return ivComboList;
    }

    private List<Ranking.RankDetails> calculate(int maxCpLeague, PokemonStats poke, IvCombo originalStats, int levelCap) {
        int ivMin = 0;

        List<IvCombo> ivComboList = getIvCombos(ivMin, levelCap);

        List<ProductStats> products = generateProductsStats(maxCpLeague, poke, ivComboList);

        int rank = 0;
        for (int i = 0; i < products.size(); i++) {
            ProductStats p = products.get(i);
            if (p.getIvAttack() == originalStats.getAttack()
                    && p.getIvDefense() == originalStats.getDefense()
                    && p.getIvStat() == originalStats.getStat()) {
                rank = i + 1;
                break;
            }
        }

        IvCombo calculatedIvs = calculateIvs(poke, originalStats, maxCpLeague);
        double attXDef = Math.floor(cpm2[calculatedIvs.getLevel()] * calculatedIvs.getAttack() * calculatedIvs.getDefense());
        double statProduct = attXDef * Math.floor(cpm[calculatedIvs.getLevel()] * calculatedIvs.getStat());

        if (rank == 0) {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getStatProduct() > statProduct) {
                    rank = i;
                    break;
                }
            }
        }

        double combatPower = cp(calculatedIvs.getAttack(), calculatedIvs.getDefense(), calculatedIvs.getStat(), cpm2[calculatedIvs.getLevel()]);
        long perfection = Math.round((statProduct / products.get(0).getStatProduct()) * 100000L) / 1000L;

        Ranking.RankDetails evaluated = new Ranking.RankDetails(rank, originalStats.getAttack(),
                originalStats.getDefense(), originalStats.getStat(), originalStats.getLevel() / 2 + 1, (int) combatPower, perfection);


        ProductStats perfSt = products.get(0);
        double perfectCp = cp((Math.floor(poke.getAttack()) + perfSt.getIvAttack()),
                (Math.floor(poke.getDefense()) + perfSt.getIvDefense()),
                (Math.floor(poke.getHealthPoints()) + perfSt.getIvStat()), cpm2[perfSt.getLevel()]);

        Ranking.RankDetails perfect = new Ranking.RankDetails(1, perfSt.getIvAttack(),
                perfSt.getIvDefense(), perfSt.getIvStat(), perfSt.getLevel() / 2 + 1, (int) perfectCp, 100);
        return Arrays.asList(evaluated, perfect);
    }

    public Ranking checkPokemonStats(StatsDto stats, PokemonStats pokemon) {
        int levelCap = StringUtils.isEmpty(stats.getLevelCap()) ? 78 : Integer.parseInt(stats.getLevelCap());
        IvCombo originalStats = new IvCombo(Integer.parseInt(stats.getAttackIv()),
                Integer.parseInt(stats.getDefenseIv()), Integer.parseInt(stats.getHpIv()), levelCap);
        List<Ranking.RankDetails> greatLeague = calculate(League.GREAT.getMaxCp(), pokemon, originalStats, levelCap);
        List<Ranking.RankDetails> ultraLeague = calculate(League.ULTRA.getMaxCp(), pokemon, originalStats, levelCap);
        List<Ranking.RankDetails> masterLeague = calculate(League.MASTER.getMaxCp(), pokemon, originalStats, levelCap);

        return new Ranking(masterLeague.get(0), ultraLeague.get(0), greatLeague.get(0),
                masterLeague.get(1), ultraLeague.get(1), greatLeague.get(1));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    protected static class IvCombo {
        private int attack;
        private int defense;
        private int stat;
        private int level;

        @Override
        public String toString() {
            return "IvCombo{" +
                    "attack=" + attack +
                    ", defense=" + defense +
                    ", stat=" + stat +
                    ", level=" + level +
                    '}';
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    protected static class ProductStats implements Comparable<ProductStats> {
        private double statProduct;
        private int level;
        private int ivAttack;
        private int ivDefense;
        private int ivStat;
        private int totalAttack;
        private int totalDefense;
        private int totalStat;

        @Override
        public int compareTo(ProductStats o) {
            return Double.compare(o.statProduct, this.statProduct);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductStats that = (ProductStats) o;
            return Double.compare(that.statProduct, statProduct) == 0 &&
                    level == that.level && ivAttack == that.ivAttack && ivDefense == that.ivDefense &&
                    ivStat == that.ivStat && totalAttack == that.totalAttack && totalDefense == that.totalDefense && totalStat == that.totalStat;
        }

        @Override
        public int hashCode() {
            return Objects.hash(statProduct, level, ivAttack, ivDefense, ivStat, totalAttack, totalDefense, totalStat);
        }

        @Override
        public String toString() {
            return "ProductStats{" +
                    "statProduct=" + statProduct +
                    ", level=" + level +
                    ", ivAttack=" + ivAttack +
                    ", ivDefense=" + ivDefense +
                    ", ivStat=" + ivStat +
                    ", totalAttack=" + totalAttack +
                    ", totalDefense=" + totalDefense +
                    ", totalStat=" + totalStat +
                    '}';
        }
    }
}
