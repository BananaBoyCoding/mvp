package com.wd.tech.baselibrary.mvp.network;

import android.util.Log;
import android.widget.Toast;

import com.wd.tech.baselibrary.BaseApplication;
import com.wd.tech.baselibrary.Certificate;
import com.wd.tech.baselibrary.Contacts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author:wyx
 * Time:2019/3/1 11:29
 * Description:
 */
public class RetrofitUtils {

    private MyApiService myApiService;
    private OkHttpClient okHttpClient;

    private RetrofitUtils() {
        Certificate certificate=new Certificate();
        try {
            okHttpClient = certificate.testHttps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化Retrofit 并结合各种操作
        Retrofit retrofit = new Retrofit.Builder()
                //结合Gson解析
                .addConverterFactory(GsonConverterFactory.create())
                //结合Rxjava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Contacts.BASE_URL)
                .client(okHttpClient)
                .build();
        //通过Retrofit创建完 这个ApiService 就可以调用方法了
        myApiService = retrofit.create(MyApiService.class);
    }

    public static RetrofitUtils getInstance() {
        return RetroHolder.retro;
    }

    private static class RetroHolder {
        private static final RetrofitUtils retro = new RetrofitUtils();
    }

    //封装Get方式
    public RetrofitUtils get(String url, Map<String, Object> headmap, Map<String, Object> querymap) {
        if (headmap == null) {
            headmap = new HashMap<>();
        }
        if (querymap == null) {
            querymap = new HashMap<>();
        }
        myApiService.get(url, headmap, querymap).subscribeOn(Schedulers.io())//io就是子线程
                //在主线程调用
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }


    //封装post方式
    public RetrofitUtils post(String url, Map<String, Object> headmap, Map<String, Object> querymap) {
        if (headmap == null) {
            headmap = new HashMap<>();
        }
        if (querymap == null) {
            querymap = new HashMap<>();
        }

        myApiService.post(url, headmap, querymap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }

    //封装put方式
    public RetrofitUtils put(String url, Map<String, Object> headmap, Map<String, Object> querymap) {
        if (headmap == null) {
            headmap = new HashMap<>();
        }
        if (querymap == null) {
            querymap = new HashMap<>();
        }

        myApiService.put(url, headmap, querymap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }

    //封装delete方式
    public RetrofitUtils delete(String url, Map<String, Object> headmap, Map<String, Object> querymap) {
        if (headmap == null) {
            headmap = new HashMap<>();
        }
        if (querymap == null) {
            querymap = new HashMap<>();
        }

        myApiService.delete(url, headmap, querymap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }

    //封装一个上传图片

    public RetrofitUtils image(String url, Map<String, Object> headmap, Map<String, Object> map, List<Object> list) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (headmap == null) {
            headmap = new HashMap<>();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (map.isEmpty()) {
//           map为空走上传头像
            if (list.size() < 2) {
                for (int i = 0; i < list.size(); i++) {
                    File file = new File((String) list.get(i));
                    builder.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/octet-stream"), file));
                }
            } else {
                Toast.makeText(BaseApplication.getContext(), "没有选择图片", Toast.LENGTH_SHORT).show();
            }
        } else {
            /**
             * 发布帖子
             */
            if (!String.valueOf(map.get("content")).equals("")) {
                builder.addFormDataPart("content", String.valueOf(map.get("content")));
            } else {
                builder.addFormDataPart("content", "你个小拉给");
            }
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    File file = new File((String) list.get(i));
                    Log.e("file", (String) list.get(i));
                    builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/octet-stream"), file));
                }
            }
        }
        myApiService.image(url, headmap, builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }



    //子类使用
    private Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(ResponseBody responseBody) {

        }
    };
    //重写一个观察者对象
    private Observer observer = new Observer<ResponseBody>() {

        @Override
        public void onCompleted() {

        }

        //网络处理失败
        @Override
        public void onError(Throwable e) {
            if (httpListener != null) {
                httpListener.onError(e.getMessage());
            }
        }

        //网络处理成功
        @Override
        public void onNext(ResponseBody responseBody) {
            if (httpListener != null) {
                try {
                    httpListener.onSuccess(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public interface HttpListener {
        void onSuccess(String jsonStr);

        void onError(String error);
    }

    private HttpListener httpListener;

    public void setHttpListener(HttpListener listener) {
        this.httpListener = listener;
    }


}
