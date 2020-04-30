package com.xuecheng.manage_media.test;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文件分块与合并测试
 */
public class ChunkTest {

    /**
     * 文件分块
     */
    @Test
    public void splitChunk() throws IOException {
        File sourceFile = new File("E:\\video\\lucene.mp4");
        String chunkStr = "E:\\video\\chunkFile\\";
        File chunkfolder = new File("E:\\video\\chunkFile\\");
        // 块文件大小
        Long chunkSize = 1L*1024*1024;
        // 分割后的块文件个数
        Long chunkCount = 0L;

        if(!sourceFile.exists()){
            throw new RuntimeException(sourceFile.getAbsolutePath()+" 文件不存在");
        }
        if(!chunkfolder.exists()){
            chunkfolder.mkdirs();
        }
        // 文件个数，向上取整
        chunkCount = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        chunkCount = chunkCount>0?chunkCount:1;
        // 缓存区
        byte[] buf = new byte[1024];
        // 读取源文件
        RandomAccessFile source_read = new RandomAccessFile(sourceFile, "r");
        for (int i=0;i<chunkCount;i++){
            // 创建一个新文件
            File chunkFile = new File(chunkStr + i);
            boolean newFile = chunkFile.createNewFile();
            // 文件创建成功
            if(newFile) {
                RandomAccessFile rw = new RandomAccessFile(chunkFile, "rw");
                int len = -1;
                while ((len=source_read.read(buf))!=-1){// 每次读取，指针都会下移
                    rw.write(buf,0,len);
                    // 如果写入的块文件大小大于块文件定义的大小，则结束这次循环
                    if(chunkFile.length()>chunkSize){
                        break;
                    }
                }
                // 关闭块文件写入流
                rw.close();
            }
            
        }
        // 关闭读取文件
        source_read.close();
    }

    /**
     * 拼接块文件
     */
    @Test
    public void mergeChunk() throws IOException {
        File chunkfolder = new File("E:\\video\\chunkFile\\");
        File mergeFile = new File("E:\\video\\mergeFile.mp4");
        if (mergeFile.exists()){
            mergeFile.delete();
        }
        mergeFile.createNewFile();
        RandomAccessFile megerRw = new RandomAccessFile(mergeFile, "rw");
        megerRw.seek(0);
        byte[] buf = new byte[1024];

        if (chunkfolder.exists()){
            // 获得分块列表
            File[] files = chunkfolder.listFiles();
            List<File> fileList = Arrays.asList(files);
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if( Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName()) ){
                        return 1;
                    }
                    return -1;
                }
            });

            for (File file: fileList) {
                RandomAccessFile r = new RandomAccessFile(file, "r");
                int len = -1;
                while ( (len=r.read(buf))!=-1 ){
                    megerRw.write(buf,0,len);
                }
                r.close();
            }

            megerRw.close();
        }else {
            throw new RuntimeException(chunkfolder.getAbsolutePath()+" 文件不存在");
        }

    }


}
