package primitive.collection;

/* DO NOT EDIT THIS FILE, IT IS GENERATED! */

import java.util.Random;

public class ShortStatList extends ShortList {

    public void each(ShortFunction func) {
        for (int i = 0; i != this.size(); ++i) {
            func.apply(this.get(i));
        }
    }

    public short reduce(short initial, ShortFunction func) {
        short memo = initial;
        for (int i = 0; i != this.size(); ++i) {
            memo = func.accumulate(memo, this.get(i));
        }
        return memo;

    }

    public short sum() {
        ShortFunction f = new ShortFunction() {
            public short accumulate(short memo, short value) {
                return (short) (memo + value);
            }
        };

        return this.reduce((short) 0, f);
    }

    public ShortStats stats() {

        final ShortStats stats = new ShortStats();

        ShortFunction f = new ShortFunction() {
            public void apply(short value) {

                if (value < stats.min) {
                    stats.min = value;
                }
                if (value > stats.max) {
                    stats.max = value;
                }
                stats.sum += value;
            }

        };

        this.each(f);

        stats.n = this.size();
        stats.avg = stats.sum / (double) stats.n;

        f = new ShortFunction() {
            public void apply(short value) {
                stats.var += Math.pow(stats.avg - (double) value, 2);
            }
        };

        this.each(f);

        stats.var = stats.var / (double) stats.n;
        stats.stdd = Math.pow(stats.var, 0.5);
        return stats;


    }

    static ShortStatList random(int count) {
        Random rand = new Random(0L);
        ShortStatList slist = new ShortStatList();
        short value;
        for (int i = 0; i != count; ++i) {
            value = (short) rand.nextInt(32000);
            slist.add(value);
        }
        return slist;
    }

    class ShortStats {
        public int n;
        public short min = Short.MAX_VALUE;
        public short max = Short.MIN_VALUE;
        public short sum;
        public double avg;
        public double stdd;
        public double var;

        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("n    = ");
            b.append(n);
            b.append("\n");
            b.append("min  = ");
            b.append(min);
            b.append("\n");
            b.append("max  = ");
            b.append(max);
            b.append("\n");
            b.append("sum  = ");
            b.append(sum);
            b.append("\n");
            b.append("avg  = ");
            b.append(avg);
            b.append("\n");
            b.append("stdd = ");
            b.append(stdd);
            b.append("\n");
            b.append("var  = ");
            b.append(var);
            b.append("\n");
            return b.toString();
        }
    }

    private static void p(Object o) {
        System.out.println(o);
    }

    public static void main(String[] args) {
        ShortStatList list = ShortStatList.random(10);

        list.each(new ShortFunction() {
            public void apply(short value) {
                p(value);
            }
        });
        p(list.stats());
        p(list.sum());

        IntStatList l = new IntStatList();
        for (int i = 1; i < 101; ++i) {
            l.add(i);
        }
        p(l.stats());
        p(l.sum());
    }

}

