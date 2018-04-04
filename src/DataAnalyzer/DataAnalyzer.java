package DataAnalyzer;

import DataAnalyzer.lingjoin.NlpirMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fuookami on 2017/2/15.
 */
public class DataAnalyzer {
    /**
     * 单例包装
     */
    private static DataAnalyzer instance = null;

    private DataAnalyzer(){
        NlpirMethod.NLPIR_Init("Data", 1, "");
        NlpirMethod.NLPIR_ImportUserDict("dict\\user_dict.txt", true);
        NlpirMethod.NLPIR_ImportKeyBlackList("dict\\black_dict.txt");
    }

    public static DataAnalyzer getInstance() {
        if (instance == null) {
            instance = new DataAnalyzer();
        }
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        // 释放分词模块
        NlpirMethod.NLPIR_SaveTheUsrDic();
        NlpirMethod.NLPIR_Exit();

        super.finalize();
    }

    /**
     * 分析接口
     * @param chat_records
     *          聊天记录
     * @return String[]
     */
    public String[] analysis(JSONObject chat_records) throws Exception {
        switch (Integer.parseInt(chat_records.getString("code"))) {
            case 0:
                return textAnalysis(JSONObject.fromObject(chat_records.get("data")));
            case 1:
                //return speechLocalAnalysis(JSONObject.fromObject(chat_records.get("data")));
                return speechCloudAnalysis(JSONObject.fromObject(chat_records.get("data")));
            case 2:
                return imageAnalysis(JSONObject.fromObject(chat_records.get("data")));
            default:
                throw new Exception("Unknown code");
        }
    }

    /**
     * 添加关键词接口
     * @param word
     *          待添加的关键词
     * @return true:添加成功；false:添加失败。
     */
    public Boolean addWord(String word) {
        NlpirMethod.NLPIR_AddUserWord(word);
        return NlpirMethod.NLPIR_SaveTheUsrDic() == 1;
    }

    /**
     * 文本分析
     * @param data
     *          聊天记录-文本信息
     * @return
     */
    private String[] textAnalysis(JSONObject data) {
        return NlpirMethod.NLPIR_ParagraphProcess(data.getString("text"), 0).split(" ");
    }

    /**
     * 本地语音分析
     * @param data
     *          聊天记录-语音信息
     * @return
     */
    public String[] speechLocalAnalysis(JSONObject data) {
        // TODO: 2017/5/25
        return new String[]{};
    }

    /**
     * 云端语音分析
     * @param data
     *          聊天记录-语音信息
     * @return
     */
    public String[] speechCloudAnalysis(JSONObject data) {
        // TODO: 2017/5/25
        return new String[]{};
    }

    /**
     * 图片分析
     * @param data
     *          聊天记录-图片信息
     * @return
     */
    private String[] imageAnalysis(JSONObject data) {
        // TODO: 2017/3/13
        return new String[]{};
    }

    /**
     * 使用搜索搜索引擎趴取关键词
     * @param imageData
     *          图像数据
     * @return
     */
    private ArrayList<String> getKeyWordByImage(byte[] imageData) {
        // TODO: 2017/2/23
        return new ArrayList<String>();
    }

    private byte[] byteJsonArray2Array (JSONArray jsonArray) {
        byte[] bytes = new byte[jsonArray.size()];
        for (int i = 0, j = jsonArray.size(); i < j; ++i) {
            bytes[i] = (byte)jsonArray.getInt(i);
        }
        return bytes;
    }
}
