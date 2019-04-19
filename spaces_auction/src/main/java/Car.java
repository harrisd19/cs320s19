public class Car extends Item {

    private Integer year;

    public Car() {
        super();
    }

    public Car(String description, Double price, Integer year) {
        super(description, price);
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return super.toString() + "; Car{" +
                "year=" + year +
                '}';
    }
}
