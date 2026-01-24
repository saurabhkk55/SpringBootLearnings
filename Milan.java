import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Single-file Kundli Milan demo
 * User inputs only DOB, TOB, Place
 * System calculates everything else
 */
public class Milan {

    /* ===================== ENUMS ===================== */

    enum MoonSign {
        ARIES, TAURUS, GEMINI, CANCER, LEO, VIRGO,
        LIBRA, SCORPIO, SAGITTARIUS, CAPRICORN, AQUARIUS, PISCES
    }

    enum Nadi {
        AADI, MADHYA, ANTYA
    }

    enum Gana {
        DEVA, MANUSHYA, RAKSHASA
    }

    /* ===================== USER INPUT ===================== */

    static class BirthDetails {
        String name;
        LocalDate dob;
        LocalTime tob;
        String place;

        BirthDetails(String name, LocalDate dob, LocalTime tob, String place) {
            this.name = name;
            this.dob = dob;
            this.tob = tob;
            this.place = place;
        }
    }

    /* ===================== INTERNAL KUNDLI ===================== */

    static class Kundli {
        MoonSign rashi;
        int nakshatra;
        Nadi nadi;
        Gana gana;

        Kundli(MoonSign rashi, int nakshatra, Nadi nadi, Gana gana) {
            this.rashi = rashi;
            this.nakshatra = nakshatra;
            this.nadi = nadi;
            this.gana = gana;
        }
    }

    /* ===================== CALCULATION ENGINE ===================== */

    static class KundliEngine {

        // Simplified Moon longitude (0–360)
        double calculateMoonLongitude(BirthDetails bd) {
            return (bd.dob.getDayOfYear() * 13.2 + bd.tob.getHour()) % 360;
        }

        int getNakshatra(double moonLongitude) {
            return (int) (moonLongitude / 13.3333) + 1; // 1–27
        }

        MoonSign getRashi(double moonLongitude) {
            return MoonSign.values()[(int) (moonLongitude / 30)];
        }

        Nadi getNadi(int nakshatra) {
            if (nakshatra % 3 == 1) return Nadi.AADI;
            if (nakshatra % 3 == 2) return Nadi.MADHYA;
            return Nadi.ANTYA;
        }

        Gana getGana(int nakshatra) {
            if (nakshatra <= 9) return Gana.DEVA;
            if (nakshatra <= 18) return Gana.MANUSHYA;
            return Gana.RAKSHASA;
        }

        Kundli generateKundli(BirthDetails bd) {
            double moonLongitude = calculateMoonLongitude(bd);
            int nak = getNakshatra(moonLongitude);
            MoonSign rashi = getRashi(moonLongitude);

            return new Kundli(
                    rashi,
                    nak,
                    getNadi(nak),
                    getGana(nak)
            );
        }
    }

    /* ===================== ASHTA KOOTA MILAN ===================== */

    static class KundliMilanService {

        int match(Kundli boy, Kundli girl) {
            int total = 0;

            total += varna();
            total += vashya(boy, girl);
            total += tara(boy, girl);
            total += yoni();
            total += grahaMaitri();
            total += gana(boy, girl);
            total += bhakoot(boy, girl);
            total += nadi(boy, girl);

            return total;
        }

        int varna() {
            return 1; // simplified
        }

        int vashya(Kundli b, Kundli g) {
            if (b.rashi == MoonSign.LEO && g.rashi == MoonSign.ARIES)
                return 2;
            return 0;
        }

        int tara(Kundli b, Kundli g) {
            int diff = g.nakshatra - b.nakshatra;
            if (diff < 0) diff += 27;
            int rem = diff % 9;
            return (rem == 0 || rem == 3 || rem == 5) ? 0 : 3;
        }

        int yoni() {
            return 4; // assume compatible
        }

        int grahaMaitri() {
            return 5; // assume friendly planets
        }

        int gana(Kundli b, Kundli g) {
            if (b.gana == Gana.DEVA && g.gana == Gana.RAKSHASA)
                return 0;
            return 6;
        }

        int bhakoot(Kundli b, Kundli g) {
            int diff = Math.abs(b.rashi.ordinal() - g.rashi.ordinal());
            return (diff == 5 || diff == 7 || diff == 8) ? 0 : 7;
        }

        int nadi(Kundli b, Kundli g) {
            return (b.nadi == g.nadi) ? 0 : 8;
        }
    }

    /* ===================== MAIN ===================== */

    public static void main(String[] args) {

        BirthDetails boy = new BirthDetails(
                "Vikas Kumar Kardam",
                LocalDate.of(1995, 11, 9),
                LocalTime.of(23, 45),
                "Delhi"
        );

        BirthDetails girl = new BirthDetails(
                "Sweta Singh",
                LocalDate.of(1995, 2, 1),
                LocalTime.of(4, 0),
                "Delhi"
        );

        KundliEngine engine = new KundliEngine();
        Kundli boyKundli = engine.generateKundli(boy);
        Kundli girlKundli = engine.generateKundli(girl);

        KundliMilanService milan = new KundliMilanService();
        int guna = milan.match(boyKundli, girlKundli);

        System.out.println("Boy Rashi: " + boyKundli.rashi + ", Nakshatra: " + boyKundli.nakshatra);
        System.out.println("Girl Rashi: " + girlKundli.rashi + ", Nakshatra: " + girlKundli.nakshatra);
        System.out.println("Total Guna Matched: " + guna + " / 36");
    }
}
