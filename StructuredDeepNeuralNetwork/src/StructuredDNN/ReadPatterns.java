package StructuredDNN;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ReadPatterns {

    private ArrayList<Pattern> patternsList;
    double value = 0.;

    public ReadPatterns() {
    }

    public ArrayList<Pattern> readPatterns(String fileName) throws IOException {
        patternsList = new ArrayList<Pattern>();
        Pattern pattern;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        String[] buff;
        int i;

        try {
            // skip headers
            br.readLine();

            // read pattern lines
            while ((line = br.readLine()) != null) {
                pattern = new Pattern();

                buff = line.split(",");
                pattern = this.readPattern(buff);

                patternsList.add(pattern);
            }
        } finally {
            br.close();
        }
        return patternsList;
    }

    public Pattern readPattern(String[] buff) {

        Pattern pattern = new Pattern();
        for (int i = 0; i < buff.length; i++) {
            if (!buff[i].isEmpty()) {
                // label:selling price (52500-1000000)
                if (i == 0) {
                    value = (Double.valueOf(buff[i]) - 52500)
                            / (1500000 - 52500);
                    pattern.y.add(value);
                }
                // number of beds(0-9)
                // value = input - min_value / max_val - min_val
                if (i == 1) {
                    value = (Double.valueOf(buff[i]) - 0) / (9 - 0);

                    // if(value >=1){value = 0.9999;}
                    pattern.x.add(value);
                }

                // number of baths(0-9)
                if (i == 2) {
                    value = (Double.valueOf(buff[i]) - 0) / (9 - 0);

                    pattern.x.add(value);
                }

                // floor area(300-2500)
                if (i == 3) {
                    value = (Double.valueOf(buff[i]) - 300)
                            / (5000 - 300);

                    pattern.x.add(value);
                }

                // Lot area(0-200000)
                if (i == 4) {
                    value = (Double.valueOf(buff[i]) - 0) / (20000 - 0);
                    pattern.x.add(value);
                }

                // age of the house(0-265)
                if (i == 5) {
                    value = (2015 - Double.valueOf(buff[i])) / 265;

                    pattern.x.add(value);
                }

                // yearly tax
                if (i == 6) {
                    value = (Double.valueOf(buff[i]) - 500)
                            / (12000 - 500);
                    pattern.x.add(value);
                }

                // sold date
                if (i == 7) {
                    value = (Double.valueOf(buff[i]));
                    // pattern.x.add(value);
                    pattern.z.add(value);

                    // value = (value -10)/(800-10);
                    // pattern.x.add(value);
                }

                // /*
                // HOA
                // if(i==8){value =
                // (Double.valueOf(buff[i])-0)/(1000-0);
                // pattern.x.add(value);}

                // house type
                // if(i==9){value = (Double.valueOf(buff[i])-0)/(5-0);
                // pattern.x.add(0.);}

                // number of stories
                if (i == 10) {
                    value = (Double.valueOf(buff[i]) - 0) / (5 - 0);
                    pattern.x.add(value);
                }

                // firePlace_flag
                if (i == 11) {
                    pattern.x.add(Double.valueOf(buff[i]));
                }

                // waterFront_flag
                if (i == 12) {
                    pattern.x.add(Double.valueOf(buff[i]));
                }

                // heating_flag
                if (i == 13) {
                    pattern.x.add(Double.valueOf(buff[i]));
                }

                // cooling_flag
                if (i == 14) {
                    pattern.x.add(Double.valueOf(buff[i]));
                }

                // patio_flag
                if (i == 15) {
                    pattern.x.add(Double.valueOf(buff[i]));
                }

                // parking
                // if(i==16){
                // pattern.x.add(Double.valueOf(buff[i]));
                // }

                // park_flag
                if (i == 17) {
                    pattern.x.add(Double.valueOf(buff[i]));
                }

                // similar house average selling price
                if (i == 18) {
                    value = (Double.valueOf(buff[i]) - 250)
                            / (1300 - 250);
                    pattern.x.add(value);
                }

                // nearby schools average rating(Scale 0-10)
                if (i == 19) {
                    value = (Double.valueOf(buff[i]) - 0) / (10 - 0);
                    pattern.x.add(value);
                }
                // */

            }
        }
        return pattern;
    }

}
