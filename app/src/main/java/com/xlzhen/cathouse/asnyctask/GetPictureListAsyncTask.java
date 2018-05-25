package com.xlzhen.cathouse.asnyctask;

import android.os.AsyncTask;

import com.xlzhen.cathouse.listener.Y_NetWorkSimpleResponse;
import com.xlzhen.cathouse.utils.HttpUtils;
import com.xpping.windows10.utils.FastJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xlzhen on 2/20 0020.
 * 搜索图片列表 网络访问
 */

public class GetPictureListAsyncTask extends AsyncTask<Void,Void,String> {
    private String url;
    private Y_NetWorkSimpleResponse response;
    private Class<?> cla;
    public GetPictureListAsyncTask(String url,Y_NetWorkSimpleResponse response,Class<?> cla) {
        this.url = url;
        this.response=response;
        this.cla=cla;
        execute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String html= HttpUtils.getHtml(url);

        /*try {
            html=html.substring(html.indexOf("app.page[\"pins\"] = "),html.indexOf("app.page[\"board\"] = "));
            html=html.replace("app.page[\"pins\"] = ","");
            html=html.substring(0,html.length()-2);
        }catch (Exception ex){}*/

        return html;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        jsonToEntity(result);

    }

    private <T> void jsonToEntity(String result) {
        try {
            List<T> bean = (List<T>) FastJSONParser.getBeanList(new JSONObject(result).getString("pins"), cla);
            response.successResponse(bean);
        }catch (Exception ex){
            try {
                response.failResponse(new JSONObject(result));
            } catch (JSONException e) {
                e.printStackTrace();
                response.endResponse();
            }
        }

    }
}
