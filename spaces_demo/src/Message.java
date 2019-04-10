import com.gigaspaces.annotation.pojo.SpaceId;

public class Message {
   private Integer id;
   private String  text;

   @SpaceId
   public Integer getId() {
       return id;
   }

   public void setId(Integer id) {
       this.id = id;
   }

   public String getText() {
       return text;
   }

   public void setText(String text) {
       this.text = text;
   }

   @Override
   public String toString() {
       return "Message{" + "id=" + id + ", text='" + text + '\'' + '}';
   }
}
