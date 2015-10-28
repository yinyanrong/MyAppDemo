package com.example.user.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络请求工具 此工具类需要优化，处理高并发
 *
 * @author 333
 */
public class HttpRequestUtil {
    //埋点参数
    private static String tel ;//手机号码
    private static String sn ;//设备号
    private static String model ;//手机型号
    private static String osVer ;//手机系统版本
    private static String appVer ;//APP版本
    private static String cid ;//渠道ID
    private static ExecutorService threadPool;
    private static String NETWORKTYPE;//网络状态
    private static String headString;

    private static Context mContext;
    private static final int REQUEST_SECOND=20000;
    public static TelephonyManager telephonyManager;
//
//
//    /**
//     * POST
//     *
//     * @param context  上下文
//     * @param url      接口
//     * @param param    参数
//     * @param callback 回调
//     * @param taskId   任务ID，用于区分任务
//     */
//    public static void HttpsRequestByPost(final Context context,
//                                          final String url, final Map<String, String> param,
//                                          final HttpReqCallback callback, final int taskId) {
//
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                HttpClient httpClient = SSLSocketFactoryEx
//                        .getNewHttpClient();
//                httpClient.getParams().setParameter(
//                        CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_SECOND);
//                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, REQUEST_SECOND);
//                String result = null;
//
//                try {
//                    HttpPost httpPost = new HttpPost(url);
//                    List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
//                    if (param != null) {
////							param.put("version", VERSION);
////							param.put("userDevice", MANUFACTURE);
////							param.put("network", NETWORKTYPE);
////							param.put("channelId", CHANNEL_ID);
//                        Iterator<Entry<String, String>> iterator = param
//                                .entrySet().iterator();
//                        while (iterator.hasNext()) {
//                            Entry<String, String> entry = iterator.next();
//                            String key = entry.getKey();
//                            String value = entry.getValue();
//                            list.add(new BasicNameValuePair(key, value));
//                        }
//                        httpPost.setEntity(new UrlEncodedFormEntity(list,
//                                "UTF-8"));
//                    }
//                    HttpResponse getResponse = httpClient.execute(httpPost);
//                    int responseCode = getResponse.getStatusLine().getStatusCode();
//                    if (200 == responseCode || 400 == responseCode) {
//                        result = EntityUtils.toString(
//                                getResponse.getEntity(), "UTF-8");
//                        if (callback != null) {
//                            callback.result(taskId, result);
//                        }
//                    }else {
//                        if (500 == responseCode) {
//                            if (callback != null) {
//                                callback.error(taskId, "请求失败,服务器内部错误");
//                            }
//                        }
//
//                    }
//                } catch (ConnectTimeoutException e) {
//                    e.printStackTrace();
//                    if (callback != null) {
//                        callback.timeOut(taskId, e.toString());
//                    }
//                } catch (SocketTimeoutException e) {
//                    e.printStackTrace();
//                    if (callback != null) {
//                        callback.timeOut(taskId, e.toString());
//                    }
//                } catch (Exception e) {
//                    if (callback != null) {
//                        callback.error(taskId, e.toString());
//                    }
//                } finally {
//                    try {
//                        httpClient.getConnectionManager().shutdown();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
//    }

//
//
//    //上传通讯录专用post
//    public static void HttpsRequestPost(final Context context,
//                                          final String url, final List<NameValuePair> param,
//                                          final HttpReqCallback callback, final int taskId) {
//
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                HttpClient httpClient = SSLSocketFactoryEx
//                        .getNewHttpClient();
//                httpClient.getParams().setParameter(
//                        CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_SECOND);
//                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, REQUEST_SECOND);
//                String result = null;
//                String head = SharedPreferenceUtil.getToket(context);
//                try {
//                    HttpPost httpPost = new HttpPost(url);
//                    List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
//                    NETWORKTYPE = DeviceUtil.getNetWorkType(mContext);
//                    if (param != null) {
////							param.put("version", VERSION);
////							param.put("userDevice", MANUFACTURE);
////							param.put("network", NETWORKTYPE);
////							param.put("channelId", CHANNEL_ID);
//                        /*Iterator<Entry<String, String>> iterator = param
//                                .entrySet().iterator();
//                        while (iterator.hasNext()) {
//                            Entry<String, String> entry = iterator.next();
//                            String key = entry.getKey();
//                            String value = entry.getValue();
//                            list.add(new BasicNameValuePair(key, value));
//                        }
//                        httpPost.setEntity(new UrlEncodedFormEntity(param,
//                                "UTF-8"));*/
////                        StringEntity entity = new StringEntity(param,"utf-8");//解决中文乱码问题
////                        httpPost.setEntity(entity);
//                        httpPost.setEntity(new UrlEncodedFormEntity(param,
//					HTTP.UTF_8));
//                    }
//                    if (!head.equals("")) {
//                        httpPost.setHeader("Authorization", "Token " + head);
//                    }
//                    headString = VERSION + "/" + MANUFACTURE + "_" + MODEL
//                            + "_android/" + CHANNEL_ID + "/" + SDK_VERSION
//                            + "/" + NETWORKTYPE+ "/" +TERMINAL;
//                    httpPost.setHeader("User-Agent", headString);
//                    LogUtil.e("requestUrl:" + url);
//                    LogUtil.e("head:" + head);
//                    HttpResponse getResponse = httpClient.execute(httpPost);
//                    int responseCode = getResponse.getStatusLine().getStatusCode();
//                    if (200 == responseCode || 400 == responseCode) {
//                        result = EntityUtils.toString(
//                                getResponse.getEntity(), "UTF-8");
//                        if (callback != null) {
//                            callback.result(taskId, result);
//                        }
//                    }else {
//                        LogUtil.e("---->>request failed: error code  "
//                                + responseCode);
//                        LogUtil.e("---->>response result:  "
//                                + EntityUtils.toString(
//                                getResponse.getEntity(), "UTF-8"));
//                    }
//                } catch (ConnectTimeoutException e) {
//                    e.printStackTrace();
//                    if (callback != null) {
//                        callback.timeOut(taskId, e.toString());
//                    }
//                } catch (SocketTimeoutException e) {
//                    e.printStackTrace();
//                    if (callback != null) {
//                        callback.timeOut(taskId, e.toString());
//                    }
//                } catch (Exception e) {
//                    if (callback != null) {
//                        callback.error(taskId, e.toString());
//                    }
//                } finally {
//                    try {
//                        httpClient.getConnectionManager().shutdown();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
//    }
//
//
//
//
//
//
    static {
        int threadCount = Runtime.getRuntime().availableProcessors() * 4;

        threadPool = Executors.newFixedThreadPool(threadCount);
    }
    /**
     * GET
     *
     * @param context  上下文
     * @param url      接口
     */
    public static void HttpsRequestByGet(final Context context,
                                         final String url, final long   time) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_SECOND);
                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, REQUEST_SECOND);
                String result = null;
                try {
                    HttpGet httpget;
//						if (!url.contains("network")
//								&& !url.contains("userDevice")
//								&& !url.contains("version")
//								&& !url.contains("?")) {
//
//							httpget = new HttpGet(url + "?version=" + VERSION
//									+ "&userDevice=" + MANUFACTURE
//									+ "&network=" + NETWORKTYPE + "&channelId="
//									+ CHANNEL_ID);
//						} else {
                    httpget = new HttpGet(url);
//						}

//					Log.e("tag", head);

                    httpget.setHeader("User-Agent", headString);

                    HttpResponse getResponse = httpClient.execute(httpget);
                    int responseCode = getResponse.getStatusLine().getStatusCode();
                    result = EntityUtils.toString(
                            getResponse.getEntity(), "UTF-8");
//                    Log.i("http","wanglibao---->"+result);

                       long useTime=System.currentTimeMillis()- time;
                    Log.i("http","wanglibao花费时间："+useTime);

                } catch (Exception e) {
                    e.printStackTrace();
//                    Log.i("http", "wanglibao---->" + result);
                } finally {
                    try {
                        httpClient.getConnectionManager().shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

//    /**
//     * POST
//     *
//     * @param context
//     *            上下文
//     * @param url
//     *            接口
//     * @param param
//     *            参数
//     * @param callback
//     *            回调
//     * @param taskId
//     *            任务ID，用于区分任务
//     */
//    public static void HttpsRequestByPostForBuridPoint(final Context context,
//                                                       final String url, final Map<String, String> param,
//                                                       final HttpReqCallback callback, final int taskId) {
//
//        threadPool.execute(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                HttpClient httpClient = new DefaultHttpClient();
//                httpClient.getParams().setParameter(
//                        CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_SECOND);
//                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, REQUEST_SECOND);
//                String result = null;
//                SharedPreferences sp = SharedPreferenceUtil.getSpInstance(mContext);
//
//                tel = sp.getString("myasset", "");
//
////                String head = SharedPreferenceUtil.getToket(context);
//                try {
//
//                    HttpPost httpPost = new HttpPost(url);
//                    List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
//                    NETWORKTYPE = DeviceUtil.getNetWorkType(mContext);
//                    if (param != null) {
////							param.put("tel", tel);
////							param.put("sn", sn);
////							param.put("model", model);
////							param.put("osVer", osVer);
////                            param.put("appVer", appVer);
////                            param.put("cid", cid);
//                        Iterator<Entry<String, String>> iterator = param
//                                .entrySet().iterator();
//                        while (iterator.hasNext()) {
//                            Entry<String, String> entry = iterator.next();
//                            String key = entry.getKey();
//                            String value = entry.getValue();
//                            list.add(new BasicNameValuePair(key, value));
//                        }
//                        httpPost.setEntity(new UrlEncodedFormEntity(list,
//                                "UTF-8"));
//                    }
////                    if (!head.equals("")) {
////                        httpPost.setHeader("Authorization", "Token " + head);
////                    }
//                    headString = tel + "||" + sn +"||" + model +"||"
//                            + osVer + "||" + appVer
//                            + "||" + cid;
//                    httpPost.setHeader("User-Agent", headString);
//                    LogUtil.e("requestUrl:" + url );
////                    LogUtil.e("head:" + head);
//                    HttpResponse getResponse = httpClient.execute(httpPost);
//                    if (getResponse.getStatusLine().getStatusCode() == 200) {
//                        result = EntityUtils.toString(
//                                getResponse.getEntity(), "UTF-8");
//                        if (callback != null) {
//                            callback.result(taskId, result);
//                        }
//                    } else {
//                        LogUtil.e("---->>request failed: error code  "
//                                + getResponse.getStatusLine()
//                                .getStatusCode());
//                        LogUtil.e("---->>response result:  "
//                                + EntityUtils.toString(
//                                getResponse.getEntity(), "UTF-8"));
//
//                    }
//                } catch (ConnectTimeoutException e) {
//                    e.printStackTrace();
//                    if (callback != null) {
//                        callback.timeOut(taskId, e.toString());
//                    }
//                } catch (SocketTimeoutException e) {
//                    e.printStackTrace();
//                    if (callback != null) {
//                        callback.timeOut(taskId, e.toString());
//                    }
//                } catch (Exception e) {
//                    if (callback != null) {
//                        callback.error(taskId, e.toString());
//                    }
//                } finally {
//                    try {
//                        httpClient.getConnectionManager().shutdown();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
//    }
//
//    /**
//     * 回调接口
//     *
//     * @author 333
//     */
//    public interface HttpReqCallback {
//        public void result(int taskId, String result);
//
//        public void timeOut(int taskId, String string);
//
//        public void error(int taskId, String error);
//    }
//
//    /**
//     * 获取APP版本号
//     *
//     * @param context
//     * @return
//     */
//    public static String getVersion(Context context) {
//        try {
//            PackageManager manager = context.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
//                    0);
//            String version = info.versionName;
//            return version;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

}
