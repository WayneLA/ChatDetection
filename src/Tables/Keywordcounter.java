package Tables;

/**
 * Created by 徐畅 on 2017/5/10.
 */
public class Keywordcounter {
    private Integer id;             //自增id
    private Integer keword_id;      //映射keyword表中id
    private String timestamp;       //时间戳
    private Integer times;          //次数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKeword_id() {
        return keword_id;
    }

    public void setKeword_id(Integer keword_id) {
        this.keword_id = keword_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
