import java.util.ArrayList;

public class Calculation {

    protected static double getMax(equation targetEquation, ArrayList<Point> intersectionPoint){
        double res = targetEquation.getValue(intersectionPoint.get(0).getX(),intersectionPoint.get(0).getY());
        for (int i = 1; i < intersectionPoint.size(); i++) {
            double tmp = targetEquation.getValue(intersectionPoint.get(i).getX(),intersectionPoint.get(i).getY());
            if(tmp>res){
                res = tmp;
            }
        }
        return res;
    }
    protected static double getMin(equation targetEquation, ArrayList<Point> intersectionPoint){
        double res = targetEquation.getValue(intersectionPoint.get(0).getX(),intersectionPoint.get(0).getY());
        for (int i = 1; i < intersectionPoint.size(); i++) {
            double tmp = targetEquation.getValue(intersectionPoint.get(i).getX(),intersectionPoint.get(i).getY());
            if(tmp<res){
                res = tmp;
            }
        }
        return res;
    }



    public static Point intersection(equation equation1,equation equation2){
        double det = equation1.getA()*equation2.getB()-equation1.getB()*equation2.getA();
        double detX = equation1.getC()*equation2.getB()-equation1.getB()*equation2.getC();
        double detY = equation1.getA()*equation2.getC()-equation1.getC()*equation2.getA();
        return new Point(detX/det,detY/det);
    }

    protected static String printValue(ArrayList<Point> intersectionPoint, equation targetEquation) {
        String points = "\nТочки ОДЗ:\n";
        for (Point point:intersectionPoint){
            points = points.concat("("+point.getX() + ", "+point.getY()+")\n");
        }
        return points;

    }

    protected static String printPoint(ArrayList<Point> intersectionPoint) {
        String points = "\nТочки перетину \n";
        for(Point point:intersectionPoint){
            String s = "("+ point.getX()+", "+point.getY()+ ")\n";
            points = points.concat(s);
        }
        return points;
    }

    protected static void output(equation targetEquation, ArrayList<equation> funcList ){
        System.out.println("Задача:");
        System.out.println("F(x,y)="+targetEquation.getA()+"x "+(targetEquation.getB()<0 ? targetEquation.getB():"+"+targetEquation.getB()) + "y -> min,max");
        System.out.println("Нерівності меж:");
        for (equation element:funcList) {
            System.out.println(element);
        }
    }
}
