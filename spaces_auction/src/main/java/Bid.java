import com.gigaspaces.annotation.pojo.SpaceId;

import java.io.Serializable;

public class Bid implements Serializable {

    private String id;
    private Item   item;
    private String seller;
    private Double offeredPrice;

    public Bid() {

    }

    public Bid(Item item, String seller, Double offeredPrice) {
        this.item = item;
        this.seller = seller;
        this.offeredPrice = offeredPrice;
    }

    @SpaceId (autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(Double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id='" + id + '\'' +
                ", item=" + item +
                ", seller='" + seller + '\'' +
                ", offeredPrice=" + offeredPrice +
                '}';
    }
}
