import java.util.List;

public interface Trackable {

    void updateWeight(double newWeight);
    double getCurrentWeight();
    List<Double> getWeightHistory();
}