package com.xuecheng.manage_media.service.impl;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import com.xuecheng.manage_media.service.MediaFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.ExampleMatcherAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class MediaFileServiceImpl implements MediaFileService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaFileServiceImpl.class);

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Override
    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        MediaFile mediaFile = new MediaFile();
        if (queryMediaFileRequest==null){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        // 查询条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher( // tag字段模糊匹配
                        "tag", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher( // 原始名称，字段模糊匹配
                        "fileOriginalName",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher(// processStatus //处理状态精确匹配（默认）
                        "processStatus",ExampleMatcher.GenericPropertyMatchers.exact());
        if(!StringUtils.isEmpty(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if(!StringUtils.isEmpty(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName( queryMediaFileRequest.getFileOriginalName() );
        }
        if(!StringUtils.isEmpty(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus( queryMediaFileRequest.getProcessStatus() );
        }
        // 定义example实例
        Example<MediaFile> ex = Example.of(mediaFile, matcher);
        page = page-1;
        // 分页参数
        PageRequest pageable = new PageRequest(page, size);
        // 分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(ex, pageable);
        // 封装查询结果
        QueryResult<MediaFile> mediaFileQueryResult = new QueryResult<>();
        mediaFileQueryResult.setList(all.getContent());
        mediaFileQueryResult.setTotal(all.getTotalElements());

        return new QueryResponseResult(CommonCode.SUCCESS,mediaFileQueryResult);
    }
    
    
    
    
    
}
