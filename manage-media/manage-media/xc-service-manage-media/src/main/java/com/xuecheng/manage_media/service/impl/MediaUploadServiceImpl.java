package com.xuecheng.manage_media.service.impl;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.controller.MediaUploadController;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.*;
import java.util.*;

@Service
@Transactional
public class MediaUploadServiceImpl implements MediaUploadService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaUploadController.class);

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Value("${xc-service-manage-media.upload-location}")
    private String uploadPath;

    /**
     * 规则：
     *      一级目录：md5第一个字符
     *      二级目录：md5第二个字符
     *      三级目录：md5
     *  文件名：md5+扩展名
     * @param fileMd5
     * @param fileExt
     * @return 文件路径
     */
    private String getFilePath(String fileMd5,String fileExt){
        String path = uploadPath+fileMd5.substring(0,1)
                +"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/"+fileMd5+"."+fileExt;
        return path;
    }

    /**
     * 获得文件目录的相对路径
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getFileFolderRelativePath(String fileMd5,String fileExt){
        String folderPath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5+"/";
        return folderPath;
    }

    /**
     * 获得文件所在目录
     * @param fileMd5
     * @return
     */
    private String getFileFolderPath(String fileMd5){
        String fileFolderPath = uploadPath
                + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        return fileFolderPath;
    }

    private boolean createFolder(String fileMd5){
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if(!fileFolder.exists()){
            return fileFolder.mkdirs();
        }

        return true;
    }

    /**
     * 文件上传
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    @Override
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 检查文件是否已经上传
       // 1. 得到文件路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        // 2,查看数据库中文件是否存在
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        // 文件存在直接返回
        if( file.exists()&&optional.isPresent() ){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        // 创建文件所在文件夹
        boolean folder = createFolder(fileMd5);
        // 创建失败直接返回
        if(!folder){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获得快文件存储路径
     * @param fileMd5
     * @return
     */
    private String getChunkFileFolderPath(String fileMd5){
        return getFileFolderPath(fileMd5)+"/chunks/";
    }

    @Override
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        String chunkFileFolder = getChunkFileFolderPath(fileMd5);
        // 块文件的名称以1，2，3...序号命名
        File file = new File(chunkFileFolder+chunk);
        if(file.exists()){
            // 文件已存在
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,true);
        }else{
            // 文件不存在
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,false);
        }
    }

    /**
     * 创建块文件路径
     * @param fileMd5
     * @return
     */
    private boolean createChunkFileFolder(String fileMd5){
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        // 文件夹不存在时创建
        if(!chunkFileFolder.exists()){
            // 创建文件夹
            return chunkFileFolder.mkdirs();
        }

        return true;
    }

    /**
     * 分块上传方法
     * @param file
     * @param fileMd5
     * @param chunk
     * @return
     */
    @Override
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        if(file==null){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_ISNULL);
        }
        // 创建块文件目录
        boolean chunkFileFolder = createChunkFileFolder(fileMd5);
        // 块文件
        File chunkFile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        // 上传块文件
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try{
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream,outputStream);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("upload chunk file fail:{}", e.getMessage());
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        }finally{
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 获得块文件存在路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if(!chunkFileFolder.exists()){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        // 文件合并之后的路径
        File megerFile = new File(getFilePath(fileMd5, fileExt));
        // 创建合并文件
        boolean megerNewFile = false;
        try {
            megerNewFile = megerFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("megerchunks..create mergeFile fail:{} ",e.getMessage());
        }
        if(!megerNewFile){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        // 获得已经排序的块文件列表
        List<File> chunkFils = getChunkFils(chunkFileFolder);
        // 合并块文件
        megerFile = megerFile(megerFile, chunkFils);
        if(megerFile==null){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        // 校验合并后的文件
        boolean b = checkFileMd5(megerFile, fileMd5);
        if(!b){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5+"."+fileExt);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5,fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);

        //状态为上传成功
        mediaFile.setFileStatus("301002");
        MediaFile save = mediaFileRepository.save(mediaFile);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 校验合并文件的正确性
     * @param megerFile
     * @param fileMd5
     * @return
     */
    public boolean checkFileMd5(File megerFile,String fileMd5){
        if(megerFile==null|| StringUtils.isEmpty(fileMd5)){
            return false;
        }
        FileInputStream megerInputStream = null;
        String megerFileMd5 = "";
        try{
            megerInputStream = new FileInputStream(megerFile);
            // 获得合并文件的md5
            megerFileMd5 = DigestUtils.md5DigestAsHex(megerInputStream);
            if(megerFileMd5.equalsIgnoreCase(fileMd5)){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("checkFileMd5 error,file is:{} md5 is:{}",megerFile.getAbsoluteFile(),megerFileMd5);
        }finally {
            try {
                megerInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 合并块文件
     * @param megerFile
     * @param chunkFils
     */
    private File megerFile(File megerFile, List<File> chunkFils) {
        try {
            RandomAccessFile rw = new RandomAccessFile(megerFile, "rw");
            byte[] buf = new byte[1024];
            // 遍历块文件
            for (File chunkFIle:chunkFils){
                int len = -1;
                RandomAccessFile r = new RandomAccessFile(chunkFIle, "r");
                while( (len=r.read(buf))!=-1 ){
                    rw.write(buf,0,len);
                }
                r.close();
            }
            rw.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("meger file error:{}",e.getMessage());
            return null;
        }

        return megerFile;
    }

    /**
     * 排序块文件
     * @param chunkFileFolder
     * @return
     */
    private List<File> getChunkFils(File chunkFileFolder){
        File[] chunkFiles = chunkFileFolder.listFiles();
        List<File> chunkFileList = Arrays.asList(chunkFiles);
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if( Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName()) ){
                    return 1;
                }
                return -1;
            }
        });

        return chunkFileList;
    }

}
