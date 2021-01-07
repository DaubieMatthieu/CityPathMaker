package main.java.gtfs_vo;

public abstract class GenericGtfsVO {

    //needs to be called after instantiation so the objects can register themselves
    //can't be called from constructor because register need the attributes values,
    //and the attributes values are affected after the constructor execution
    public abstract void register();

    public void print() {
        System.out.println(this.toString());
    }

    public abstract String toString();

}
