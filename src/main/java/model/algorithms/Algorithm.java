package model.algorithms;

import model.Table;

import java.util.List;

public interface Algorithm<T extends Table,V> {
    void train(T table);
    V estimate(List<Double> data);
}
