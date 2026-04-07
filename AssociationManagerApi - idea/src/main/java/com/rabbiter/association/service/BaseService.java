package com.rabbiter.association.service;

import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseService<T, Pk> extends IService<T> {
    public void add(T t);
    public void update(T t);
    public void delete(T t);
    public T getOne(Pk id);
}
