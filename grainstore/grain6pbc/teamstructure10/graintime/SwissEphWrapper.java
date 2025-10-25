import swisseph.*;

/**
 * Swiss Ephemeris wrapper for Babashka
 * 
 * Compile: javac -cp lib/swisseph-2.01.00-01.jar SwissEphWrapper.java
 * Run: java -cp .:lib/swisseph-2.01.00-01.jar SwissEphWrapper 2025 10 25 19 51 0
 */
public class SwissEphWrapper {
    
    public static void main(String[] args) {
        if (args.length < 6) {
            System.out.println("Usage: java SwissEphWrapper YEAR MONTH DAY HOUR MINUTE SECOND");
            System.out.println("Example: java SwissEphWrapper 2025 10 25 19 51 0");
            System.exit(1);
        }
        
        try {
            // Parse datetime
            int year = Integer.parseInt(args[0]);
            int month = Integer.parseInt(args[1]);
            int day = Integer.parseInt(args[2]);
            int hour = Integer.parseInt(args[3]);
            int minute = Integer.parseInt(args[4]);
            int second = Integer.parseInt(args[5]);
            
            double timeDecimal = hour + (minute / 60.0) + (second / 3600.0);
            
            // Initialize Swiss Ephemeris
            SwissEph sw = new SwissEph();
            sw.swe_set_ephe_path("resources/ephe");
            
            // Set Lahiri ayanamsa for sidereal calculations
            sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI, 0.0, 0.0);
            
            // Calculate Julian Day using SweDate
            SweDate sd = new SweDate(year, month, day, timeDecimal);
            double jd = sd.getJulDay();
            
            // Prepare result arrays
            double[] xx = new double[6];
            StringBuffer serr = new StringBuffer();
            
            // Calculate sidereal moon position
            int flag = SweConst.SEFLG_SWIEPH | SweConst.SEFLG_SIDEREAL | SweConst.SEFLG_SPEED;
            int ret = sw.swe_calc(jd, SweConst.SE_MOON, flag, xx, serr);
            
            if (ret == SweConst.OK) {
                double siderealLongitude = xx[0];
                int nakshatraIndex = (int)(siderealLongitude / 13.333333);
                double degreeWithinNakshatra = siderealLongitude % 13.333333;
                
                // Get ayanamsa
                double ayanamsa = sw.swe_get_ayanamsa_ut(jd);
                
                // Calculate tropical for comparison
                int tropFlag = SweConst.SEFLG_SWIEPH | SweConst.SEFLG_SPEED;
                double[] xxTrop = new double[6];
                StringBuffer serrTrop = new StringBuffer();
                sw.swe_calc(jd, SweConst.SE_MOON, tropFlag, xxTrop, serrTrop);
                double tropicalLongitude = xxTrop[0];
                
                // Output in format Babashka can parse
                System.out.println("JULIAN_DAY=" + jd);
                System.out.println("SIDEREAL_LONGITUDE=" + siderealLongitude);
                System.out.println("TROPICAL_LONGITUDE=" + tropicalLongitude);
                System.out.println("AYANAMSA=" + ayanamsa);
                System.out.println("NAKSHATRA_INDEX=" + nakshatraIndex);
                System.out.println("DEGREE_WITHIN_NAKSHATRA=" + degreeWithinNakshatra);
                
            } else {
                System.err.println("ERROR: " + serr.toString());
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

