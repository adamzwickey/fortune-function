package io.pivotal.pfs.demo.redisfunction;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("fortune")
public class Fortune {

    @Id
    private String id;
    private String text;

    public Fortune() {  }

    public Fortune(String text) {
        this.text = text;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return getId() + ", " + getText();
    }
}
