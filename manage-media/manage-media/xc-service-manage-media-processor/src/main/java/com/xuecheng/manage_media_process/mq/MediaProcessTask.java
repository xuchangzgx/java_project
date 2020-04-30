package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class MediaProcessTask {
    private  static  final Logger LOGGER = LoggerFactory.getLogger(MediaProcessTask.class);

    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpegPath;
    @Value("${xc-service-manage-media.video-location}")
    private String serverPath;
    @Autowired
    private MediaFileRepository mediaFileRepository;

    /**
     * 消息监听队列
     * @param msg 客户端发送的消息
     * @param message message对象
     * @param channel channel通道对象
     */
    @RabbitListener( // queues,监听的queue名称,containerFactory 消费者容器，多个消费者并行
            queues = "${xc-service-manage-media.mq.queue-media-video-processor}",
            containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg, Message message, Channel channel){
        JSONObject msgObj = JSON.parseObject(msg);
        LOGGER.info("receive media process task mag:{}",msgObj);
        // 获得上传的媒资文件id
        String mediaId = msgObj.getString("mediaId");
        // 从mongodb中获得媒资文件信息
        Optional<MediaFile> mediaFileOpt = mediaFileRepository.findById(mediaId);
        // 如果没有对应的媒资文件直接结束
        if(!mediaFileOpt.isPresent()){
            return ;
        }
        MediaFile mediaFile = mediaFileOpt.get();
        // 获得文件类型
        String fileType = mediaFile.getFileType();
        if( fileType==null||!"avi".equals(fileType) ){
            mediaFile.setProcessStatus("303304"); // 处理状态为无需处理
            mediaFileRepository.save(mediaFile);
            return;
        }else{
            mediaFile.setProcessStatus("303001"); // 处理状态为未处理
            mediaFileRepository.save(mediaFile);
        }
        // 生成mp4文件
        // 原始文件存储文件
        String filePath = serverPath + "/" + mediaFile.getFilePath();
        String videoPath = filePath+mediaFile.getFileName();
        String mp4Name = mediaFile.getFileId()+".mp4";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath, videoPath, mp4Name, filePath);
        // 生成MP4文件
        String result = mp4VideoUtil.generateMp4();
        if(result==null||!"success".equals(result) ){
            // 操作失败写入处理日志
            mediaFile.setProcessStatus("303003"); // 处理失败状态
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return ;
        }
        // 生成命m3u8
        // mp4文件路径
        String mp4Path = filePath + mp4Name;
        String m3u8Name = mediaFile.getFileId()+".m3u8";
        String m3u8FolderPath = filePath+"hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath, mp4Path, m3u8Name, m3u8FolderPath);
        // 生成m3u8文件
        String m3u8Result = hlsVideoUtil.generateM3u8();
        if(m3u8Result == null || !"success".equals(m3u8Result)){
            //操作失败写入处理日志
            mediaFile.setProcessStatus("303003");//处理状态为处理失败
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return ;
        }
        //获取m3u8列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        //更新处理状态为成功
        mediaFile.setProcessStatus("303002");//处理状态为处理成功
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        //m3u8文件url
        mediaFile.setFileUrl(m3u8FolderPath+m3u8Name);
        mediaFileRepository.save(mediaFile);
    }

}
