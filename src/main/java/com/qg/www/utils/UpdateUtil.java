package com.qg.www.utils;

import org.apache.commons.fileupload.disk.DiskFileItem;

import java.io.InputStream;
import java.io.RandomAccessFile;


public class UpdateUtil {
    /**
     * 通过其得到文件的输入流
     */
    private DiskFileItem item;
    /**
     * 存放文件的地方
     */
    private String targetFile;
    /**
     * 要开启的线程数量
     */
    private int threadNum;
    /**
     * 存储线程的数组
     */
    private DownThread[] threads;
    /**
     * 要上传的文件大小
     */
    private int fileSize;

    public UpdateUtil(DiskFileItem item, String targetFile, int threadNum, int fileSize){
        this.item = item;
        this.targetFile = targetFile;
        this.threadNum = threadNum;
        threads = new DownThread[threadNum];
        this.fileSize = fileSize;
    }

    public void download() throws Exception
    {
        int currentPartSize = fileSize / threadNum + 1;
        RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
        // 设置本地文件的大小
        file.setLength(fileSize);
        file.close();
        for (int i = 0; i < threadNum; i++)
        {
            // 计算每条线程的下载的开始位置
            int startPos = i * currentPartSize;
            // 每个线程使用一个RandomAccessFile进行下载
            RandomAccessFile currentPart = new RandomAccessFile(targetFile,
                    "rw");
            // 定位该线程的下载位置
            currentPart.seek(startPos);
            // 创建下载线程
            threads[i] = new DownThread(startPos, currentPartSize,
                    currentPart);
            // 启动下载线程
            threads[i].start();
        }
    }

    private class DownThread extends Thread {
        /**
         * 当前线程的下载开始位置
         */
        private int startPos;
        /**
         * 当前线程负责下载的文件大小
         */
        private int currentPartSize;
        /**
         * 当前线程需要下载的文件块
         */
        private RandomAccessFile currentPart;
        /**
         * 当前线程已下载的字节数
         */
        private int length;

        private DownThread(int startPos, int currentPartSize,
                          RandomAccessFile currentPart) {
            this.startPos = startPos;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
        }

        @Override
        public void run() {
            try {
                InputStream inStream = item.getInputStream();
                // 跳过startPos个字节，表明该线程只下载自己负责哪部分文件。
                inStream.skip(startPos);
                byte[] buffer = new byte[1024];
                int hasRead;
                // 读取网络数据，并写入本地文件
                while (length < currentPartSize && (hasRead = inStream.read(buffer)) != -1) {
                    synchronized (this){
                        currentPart.write(buffer, 0, hasRead);
                        // 累计该线程下载的总大小
                        length += hasRead;
                    }
                }
                inStream.close();
                currentPart.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
