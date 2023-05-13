package controller;

import algorithms.Distance;
import algorithms.EuclideanDistance;
import algorithms.ManhattanDistance;
import util.SongRecSys;

import java.util.List;

public class CallSongRecSys {
    SongRecSys songRecSys;
    public void defineParameters(String recommendationMethod, String distanceType) throws Exception {
        Distance distance;
        if (distanceType == "Euclidean"){
            distance = new EuclideanDistance();
        }
        else if (distanceType == "Manhattan"){
            distance = new ManhattanDistance();
        }
        else {
            throw new IllegalArgumentException();
        }
        songRecSys = new SongRecSys(recommendationMethod, distance);
    }

    public List<String> recommend(String name, int numRecommendations){
        return songRecSys.recommend(name,numRecommendations);
    }
}
