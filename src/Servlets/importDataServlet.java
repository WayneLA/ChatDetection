package Servlets;

import Managers.keywordManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by 徐畅 on 2017/5/23.
 */
@WebServlet(name = "importDataServlet")
public class importDataServlet extends HttpServlet {
    private String filePath = "C:\\Users\\徐畅\\Desktop\\Keyword.txt";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> keywordList = readTxtFile(filePath);
        keywordManager keywordmanager = new keywordManager();
        int result = keywordmanager.TESTaddKeywordByUserId(1, keywordList);
     }

    public static List<String> readTxtFile(String filePath){
        List<String> keywordList = new ArrayList<>();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while((lineTxt = bufferedReader.readLine()) != null){
                    keywordList.add(lineTxt);
                    //System.out.println(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return keywordList;
    }
}
