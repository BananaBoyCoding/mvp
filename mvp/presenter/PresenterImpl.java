package com.wd.tech.baselibrary.mvp.presenter;

import com.wd.tech.baselibrary.mvp.contract.Contract;
import com.wd.tech.baselibrary.mvp.model.ModelImpl;
import com.wd.tech.baselibrary.mvp.network.RetrofitUtils;

import java.util.List;
import java.util.Map;

/**
 * Author:wyx
 * Time:2019/3/1 11:26
 * Description:
 */
public class PresenterImpl implements Contract.Presenter {
    private ModelImpl model;
    private Contract.View view;

    public PresenterImpl(Contract.View view) {
        this.view = view;
        model = new ModelImpl();
    }

    @Override
    public void get(String url, Map<String, Object> headmap, Map<String, Object> map, Class aClass) {
        model.get(url, headmap, map, aClass, new Contract.MyCallBack() {
            @Override
            public void success(Object success) {
                view.success(success);
            }

            @Override
            public void error(String error) {
                view.error(error);
            }
        });
    }

    @Override
    public void post(String url, Map<String, Object> headmap, Map<String, Object> map, Class aClass) {
        model.post(url, headmap, map, aClass, new Contract.MyCallBack() {
            @Override
            public void success(Object success) {
                view.success(success);
            }

            @Override
            public void error(String error) {
                view.error(error);
            }
        });
    }

    @Override
    public void put(String url, Map<String, Object> headmap, Map<String, Object> map, Class aClass) {
        model.put(url, headmap, map, aClass, new Contract.MyCallBack() {
            @Override
            public void success(Object success) {
                view.success(success);
            }

            @Override
            public void error(String error) {
                view.error(error);
            }
        });
    }

    @Override
    public void delete(String url, Map<String, Object> headmap, Map<String, Object> map, Class aClass) {
        model.delete(url, headmap, map, aClass, new Contract.MyCallBack() {
            @Override
            public void success(Object success) {
                view.success(success);
            }

            @Override
            public void error(String error) {
                view.error(error);
            }
        });
    }

    @Override
    public void img(String url, Map<String, Object> headmap, Map<String, Object> map, List<Object> list, Class aClass) {
        model.img(url, headmap, map, list, aClass, new Contract.MyCallBack() {
            @Override
            public void success(Object success) {
                view.success(success);
            }

            @Override
            public void error(String error) {
                view.error(error);
            }
        });
    }
}
