package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 媒体上传
 */
public interface MediaUploadService {

    //文件上传前的准备工作,校验文件是否存在
    public ResponseResult register(String fileMd5,
                                   String fileName,
                                   Long fileSize,
                                   String mimetype,
                                   String fileExt);

    public CheckChunkResult checkchunk(String fileMd5,
                                       Integer chunk,
                                       Integer chunkSize);

    public ResponseResult uploadchunk(MultipartFile file,
                                      String fileMd5,
                                      Integer chunk);

    public ResponseResult mergechunks(String fileMd5,
                                      String fileName,
                                      Long fileSize,
                                      String mimetype,
                                      String fileExt);


}
