package Tables;

/**
 * Created by 徐畅 on 2017/5/24.
 */
public class Wordcounter {
    private Integer id;         //自增识别码
    private String word;        //词
    private long timestamp;     //对应该条聊天记录的时间戳
    private Integer times;      //该条聊天记录该词的频率

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
