package com.xuecheng.manage_media.controller;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator.
 */
@Api(value = "媒资上传接口",description = "媒资上传接口，提供文件上传、处理等接口")
public interface MediaUploadControllerApi {

    //文件上传前的准备工作,校验文件是否存在
    @ApiOperation("文件上传注册")
    public ResponseResult register(@ApiParam(name = "fileMd5",value = "文件唯一标识", required=true) String fileMd5,
                                   @ApiParam(name = "fileName",value = "文件名", required=true) String fileName,
                                   @ApiParam(name = "fileSize",value = "文件大小", required=true) Long fileSize,
                                   @ApiParam(name = "mimetype",value = "文件类型", required=true) String mimetype,
                                   @ApiParam(name = "fileExt",value = "文件扩展名", required=true)  String fileExt);

    @ApiOperation("校验分块文件是否存在")
    public CheckChunkResult checkchunk(@ApiParam(name = "fileMd5",value = "文件唯一标识", required=true) String fileMd5,
                                       @ApiParam(name = "chunk",value = "块", required=true) Integer chunk,
                                       @ApiParam(name = "chunkSize",value = "块大小", required=true) Integer chunkSize);

    @ApiOperation("上传分块")
    public ResponseResult uploadchunk(@ApiParam(name = "file",value = "上传的块文件", required=true) MultipartFile file,
                                      @ApiParam(name = "fileMd5",value = "文件唯一标识", required=true) String fileMd5,
                                      @ApiParam(name = "chunk",value = "chunk", required=true) Integer chunk);

    @ApiOperation("合并分块")
    public ResponseResult mergechunks(@ApiParam(name = "fileMd5",value = "文件唯一标识", required=true) String fileMd5,
                                      @ApiParam(name = "fileName",value = "文件名", required=true) String fileName,
                                      @ApiParam(name = "fileSize",value = "文件大小", required=true) Long fileSize,
                                      @ApiParam(name = "mimetype",value = "文件类型", required=true) String mimetype,
                                      @ApiParam(name = "fileExt",value = "文件扩展名", required=true)  String fileExt);

}
