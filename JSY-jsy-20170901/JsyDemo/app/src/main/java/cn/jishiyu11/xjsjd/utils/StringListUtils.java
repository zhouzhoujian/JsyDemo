package cn.jishiyu11.xjsjd.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 2016/8/23.
 */
public class StringListUtils {
    /**

     *   将json 数组转换为Map 对象

     * @param jsonString

     * @return

     */

    public static Map<String, Object> getMap(String jsonString)

    {

        JSONObject jsonObject;

        try

        {

            jsonObject = new JSONObject(jsonString);   @SuppressWarnings("unchecked")

        Iterator<String> keyIter = jsonObject.keys();

            String key;

            Object value;

            Map<String, Object> valueMap = new HashMap<String, Object>();

            while (keyIter.hasNext())

            {

                key = (String) keyIter.next();

                value = jsonObject.get(key);

                valueMap.put(key, value);


            }

            return valueMap;

        }

        catch (JSONException e)

        {

            e.printStackTrace();

        }

        return null;

    }

	
	 /** 
     * 将对象分装为json字符串 (json + 递归) 
     * @param obj 参数应为{@link Map} 或者 {@link List}
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static Object jsonEnclose(Object obj) {  
        try {  
            if (obj instanceof Map) {   //如果是Map则转换为JsonObject  
                Map<String, Object> map = (Map<String, Object>)obj;  
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                JSONStringer jsonStringer = new JSONStringer().object();
                while (iterator.hasNext()) {  
                    Map.Entry<String, Object> entry = iterator.next();
                    jsonStringer.key(entry.getKey()).value(jsonEnclose(entry.getValue()));  
                }  
                JSONObject jsonObject = new JSONObject(new JSONTokener(jsonStringer.endObject().toString()));
                return jsonObject;  
            } else if (obj instanceof List) {  //如果是List则转换为JsonArray  
                List<Object> list = (List<Object>)obj;  
                JSONStringer jsonStringer = new JSONStringer().array();  
                for (int i = 0; i < list.size(); i++) {  
                    jsonStringer.value(jsonEnclose(list.get(i)));  
                }  
                JSONArray jsonArray = new JSONArray(new JSONTokener(jsonStringer.endArray().toString()));  
                return jsonArray;  
            } else {  
                return obj;  
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
//            LogUtil.e("jsonUtil--Enclose", e.getMessage());
            return e.getMessage();  
        }  
    }
	


    /**

     * 把json 转换为ArrayList 形式

     * @return

     */

    public static List<Map<String, Object>> getList(String jsonString)

    {

        List<Map<String, Object>> list = null;

        try

        {

            JSONArray jsonArray = new JSONArray(jsonString);

            JSONObject jsonObject;

            list = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < jsonArray.length(); i++)

            {

                jsonObject = jsonArray.getJSONObject(i);

                list.add(getMap(jsonObject.toString()));

            }

        }

        catch (Exception e)

        {

            e.printStackTrace();

        }

        return list;

    }

/**
 *崎岖返回数据中的jsonarray
 *
 * */
    public static List getlist2(String responsejson){
        List<Map<String, Object>> list= new ArrayList<>();
        try {
            JSONObject object=new JSONObject(responsejson);
            JSONArray array= (JSONArray) object.get("data");
            Map<String,String> hashMap=new HashMap<String,String>();
            list= getList(array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 得到键值对的值数组
     * @param strings 键数组
     * @param map       值数组
     * */
    public static Object[] getDBkeystring(String[] strings,Map<String,Object> map){
        Object[] o=new Object[strings.length];

        for (int i=0; i<strings.length ;i++){
            if (strings[i].equals("Id")){
                o[i]=Integer.parseInt(map.get("Id")+"");
            }else
            o[i]=map.get(strings[i]);
        }
        return o;
    }

/**
 * 将毫秒转成时间yyyy-MM-dd HH:mm
 * */
    public static String GetStringFromLong(long millis)
    {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt = new Date(millis);
        return sdf.format(dt);
    }



































}
