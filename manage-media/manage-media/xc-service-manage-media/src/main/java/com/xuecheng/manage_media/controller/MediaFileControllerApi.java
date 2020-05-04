package com.xuecheng.manage_media.controller;

import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(
        value = "媒资管理接口",
        description = "媒资管理接口，提供媒资文件查看，处理，删除等操作",
        tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {

    @ApiOperation("媒体分页")
    public QueryResponseResult findList(
            @ApiParam(name = "page",value = "当前页数",required = true) int page,
            @ApiParam(name = "size",value = "页大小",required = true) int size,
            @ApiParam(name = "queryMediaFileRequest",value = "查询条件") QueryMediaFileRequest queryMediaFileRequest);

    @ApiOperation("获得所有mapping")
    public String getAllUrl(String a);
}
