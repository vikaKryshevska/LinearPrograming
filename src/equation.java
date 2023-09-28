public class equation {
    private double A;
    private double B;
    private double C;

    private boolean sign;
    public equation(double a, double b, double c,boolean sign) {
        A = a;
        B = b;
        C = c;
        this.sign=sign;
    }
    public equation(double[] cons){

        A = cons[0];
        B = cons[1];
        C = cons[2];
        this.sign=false;
    }
    public double getA(){return A;}
    public double getB(){return B;}
    public double getC() {
        return C;
    }

    public boolean isInArea(double x1, double x2){
        double res=(A*x1+B*x2);
        return sign ? (res>=C) : (res<=C);
    }
    public double getValue(double x1,double x2){
        return x1*A+x2*B;
    }
    @Override
    public String toString() {
        return String.format("%.2gx1%+.2gx2 %s %.2g",A,B,sign?">=":"<=",C);
    }
}
