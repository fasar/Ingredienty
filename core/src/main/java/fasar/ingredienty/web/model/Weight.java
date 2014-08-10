package fasar.ingredienty.web.model;

import java.util.Date;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class Weight {
    private final User user;
    private final Date date;
    private final Double weight;
    private final Double fat;
    private final Double water;
    private final Double muscles;
    private final Double bones;
    private final Double visceralFat;

    public Weight(User user, Date date, Double weight, Double fat, Double water, Double muscles, Double bones, Double visceralFat) {
        this.user = user;
        this.date = date;
        this.weight = weight;
        this.fat = fat;
        this.water = water;
        this.muscles = muscles;
        this.bones = bones;
        this.visceralFat = visceralFat;
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getFat() {
        return fat;
    }

    public Double getWater() {
        return water;
    }

    public Double getMuscles() {
        return muscles;
    }

    public Double getBones() {
        return bones;
    }

    public Double getVisceralFat() {
        return visceralFat;
    }
}
