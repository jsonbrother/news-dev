package com.api.service;

import com.github.pagehelper.PageInfo;
import com.result.PagedGridResult;
import com.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Json
 * @date 2021/2/6 15:36
 */
public class BaseService {

    @Autowired
    protected RedisOperator redis;

    /**
     * 列表数据分页
     */
    protected PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setTotal(pageList.getPages());
        return gridResult;
    }
}
