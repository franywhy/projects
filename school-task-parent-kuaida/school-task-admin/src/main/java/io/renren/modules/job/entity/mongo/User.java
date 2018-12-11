package io.renren.modules.job.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hq
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private Long id;

    private String tuid;

    private String pic;

    private int priv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTuid() {
        return tuid;
    }

    public void setTuid(String tuid) {
        this.tuid = tuid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getPriv() {
        return priv;
    }

    public void setPriv(int priv) {
        this.priv = priv;
    }
}
