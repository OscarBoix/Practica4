package algorithms;

import java.util.List;

public class ManhattanDistance implements Distance{
    public double calculateDistance(List<Double> p, List<Double> q) {
        double distance = .0;
        for(int currentColumn = 0; currentColumn<p.size(); currentColumn++)
            distance += Math.abs(p.get(currentColumn)-q.get(currentColumn));
        return distance;
    }
}
