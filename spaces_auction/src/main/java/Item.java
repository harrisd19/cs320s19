import com.gigaspaces.annotation.pojo.SpaceId;

import java.io.Serializable;

public class Item implements Serializable {

    private String id;
    private String description;
    private Double price;

    public Item() {

    }

    public Item(String description, Double price) {
        this.description = description;
        this.price = price;
    }

    @SpaceId (autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }


}
