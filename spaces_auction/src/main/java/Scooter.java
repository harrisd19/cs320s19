public class Scooter extends Item {

    private Boolean electric;

    public Scooter() {
        super();
    }

    public Scooter(String description, Double price, Boolean electric) {
        super(description, price);
        this.electric = electric;
    }

    public Boolean getElectric() {
        return electric;
    }

    public void setElectric(Boolean electric) {
        this.electric = electric;
    }

    @Override
    public String toString() {
        return super.toString() + "; Scotter{" +
                "electric=" + electric +
                '}';
    }
}
