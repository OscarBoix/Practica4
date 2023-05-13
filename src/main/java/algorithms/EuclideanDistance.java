package algorithms;

import java.util.List;

public class EuclideanDistance implements Distance{
    public double calculateDistance(List<Double> p, List<Double> q) {
        double distance = .0;
        for(int currentColumn = 0; currentColumn<p.size(); currentColumn++)
            distance += Math.pow(p.get(currentColumn)-q.get(currentColumn),2);
        return Math.sqrt(distance);
    }
}
