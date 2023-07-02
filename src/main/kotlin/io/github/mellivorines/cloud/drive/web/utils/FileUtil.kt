package io.github.mellivorines.cloud.drive.web.utils

import io.github.mellivorines.cloud.drive.web.utils.DateUtil.todayDayString
import org.apache.commons.lang3.StringUtils
import java.io.*
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.activation.MimetypesFileTypeMap


/**
 * @Description:文件工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object FileUtil {
    private const val KB_STR = "K"
    private const val MB_STR = "M"
    private const val GB_STR = "G"
    private const val UNIT = 1024
    private const val FILE_SIZE_DESC_FORMAT = "%.2f"
    private const val POINT_STR = "."
    private const val ZERO_LONG = 0L
    private const val ZERO_INT = 0
    private const val ONE_INT = 1
    private const val MINUS_ONE_INT = -1
    private const val EMPTY_STR = ""
    private const val SLASH = "/"
    private const val TEMP_FOLDER_NAME = "temp"
    private const val DEFAULT_ROOT_FILE_NAME = "cloud.drive.web"
    private const val COMMON_SEPARATOR = "__,__"
    private val chunkNumContainer: MutableMap<String, AtomicInteger> = ConcurrentHashMap()

    /**
     * 添加分片并返回是否全部写入完毕
     *
     * @param md5
     * @param chunks
     * @return
     */
    fun addChunkAndCheckAllDone(md5: String, chunks: Int): Boolean {
        chunkNumContainer.putIfAbsent(md5, AtomicInteger())
        val currentChunks = chunkNumContainer[md5]!!.incrementAndGet()
        if (currentChunks == chunks) {
            deleteChunks(md5)
            return true
        }
        return false
    }

    /**
     * 清除md5对应的文件记录
     *
     * @param md5
     * @return
     */
    fun deleteChunks(md5: String) {
        chunkNumContainer.remove(md5)
    }

    /**
     * 从临时文件名称获取md5
     *
     * @param tempFilename
     * @return
     */
    fun getMd5FromTempFilename(tempFilename: String): String {
        if (StringUtils.isBlank(tempFilename)) {
            return StringUtils.EMPTY
        }
        val tempFilenameArr = tempFilename.split(COMMON_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        return tempFilenameArr[tempFilenameArr.size - ONE_INT]
    }

    /**
     * 生成临时文件名称
     *
     * @param originFilename
     * @param md5
     * @return
     */
    fun generateTempFilename(originFilename: String?, md5: String?): String {
        return StringBuffer(originFilename).append(COMMON_SEPARATOR).append(md5).toString()
    }

    /**
     * 生成临时文件路径
     *
     * @param tempFilePrefix
     * @param originFilename
     * @param md5
     * @return
     */
    fun generateTempFilePath(tempFilePrefix: String?, originFilename: String?, md5: String?): String {
        return StringBuffer(tempFilePrefix).append(File.separator).append(generateTempFilename(originFilename, md5))
            .toString()
    }

    /**
     * 生成文件本地的保存路径
     *
     * @param filePrefix
     * @param originFilename
     * @return
     */
    fun generateFilePath(filePrefix: String?, originFilename: String): String {
        return StringBuffer(filePrefix)
            .append(File.separator)
            .append(todayDayString)
            .append(File.separator)
            .append(UUIDUtil.uUID)
            .append(getFileSuffix(originFilename))
            .toString()
    }

    /**
     * 分块写入文件
     *
     * @param target
     * @param targetSize
     * @param src
     * @param srcSize
     * @param chunks
     * @param chunk
     * @throws IOException
     */
    @Synchronized
    @Throws(IOException::class)
    fun writeWithChunk(target: String?, targetSize: Long, src: InputStream, srcSize: Long, chunks: Int, chunk: Int) {
        val randomAccessFile = RandomAccessFile(target, "rw")
        randomAccessFile.setLength(targetSize)
        if (chunk == chunks - ONE_INT) {
            randomAccessFile.seek(targetSize - srcSize)
        } else {
            randomAccessFile.seek(chunk * srcSize)
        }
        val buf = ByteArray(1024)
        var len: Int
        while (MINUS_ONE_INT != src.read(buf).also { len = it }) {
            randomAccessFile.write(buf, ZERO_INT, len)
        }
        randomAccessFile.close()
    }

    /**
     * 获取输入流写入输出流
     *
     * @param fileInputStream
     * @param outputStream
     * @param size
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeFileToStream(fileInputStream: FileInputStream, outputStream: OutputStream, size: Long?) {
        val fileChannel = fileInputStream.channel
        val writableByteChannel = Channels.newChannel(outputStream)
        fileChannel.transferTo(ZERO_LONG, size!!, writableByteChannel)
        outputStream.flush()
        fileInputStream.close()
        outputStream.close()
        fileChannel.close()
        writableByteChannel.close()
    }

    /**
     * 获取输入流写入输出流
     *
     * @param inputStream
     * @param fileOutputStream
     * @param size
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeStreamToFile(inputStream: InputStream, fileOutputStream: FileOutputStream, size: Long?) {
        val fileChannel = fileOutputStream.channel
        val readableByteChannel = Channels.newChannel(inputStream)
        fileChannel.transferFrom(readableByteChannel, ZERO_LONG, size!!)
        fileOutputStream.flush()
        inputStream.close()
        fileOutputStream.close()
        fileChannel.close()
        readableByteChannel.close()
    }

    /**
     * 传统流对流传输
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeStreamToStreamNormal(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != MINUS_ONE_INT) {
            outputStream.write(buffer, ZERO_INT, len)
        }
        outputStream.flush()
        inputStream.close()
        outputStream.close()
    }

    /**
     * 获取文件大小字符串
     *
     * @param size
     * @return
     */
    fun getFileSizeDesc(size: Long): String {
        var fileSize = size.toDouble()
        var fileSizeSuffix = KB_STR
        fileSize = fileSize / UNIT
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT
            fileSizeSuffix = MB_STR
        }
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT
            fileSizeSuffix = GB_STR
        }
        return String.format(FILE_SIZE_DESC_FORMAT, fileSize) + fileSizeSuffix
    }

    /**
     * 通过文件名获取文件后缀
     *
     * @param filename
     * @return
     */
    fun getFileSuffix(filename: String): String {
        return if (StringUtils.isBlank(filename) || filename.indexOf(POINT_STR) == MINUS_ONE_INT) {
            EMPTY_STR
        } else filename.substring(filename.lastIndexOf(POINT_STR)).lowercase(Locale.getDefault())
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    fun getFileExtName(filename: String): String {
        val fileSuffix = getFileSuffix(filename)
        return if (StringUtils.isBlank(fileSuffix)) {
            fileSuffix
        } else fileSuffix.substring(ONE_INT)
    }

    /**
     * 通过文件路径获取文件名称
     *
     * @param filePath
     * @return
     */
    fun getFilename(filePath: String): String {
        var filename = EMPTY_STR
        if (StringUtils.isBlank(filePath)) {
            return filename
        }
        if (filePath.indexOf(File.separator) != MINUS_ONE_INT) {
            filename = filePath.substring(filePath.lastIndexOf(File.separator) + ONE_INT)
        }
        if (filePath.indexOf(SLASH) != MINUS_ONE_INT) {
            filename = filePath.substring(filePath.lastIndexOf(SLASH) + ONE_INT)
        }
        return filename
    }

    /**
     * 获取文件的content-type
     *
     * @param filePath
     * @return
     */
    fun getContentType(filePath: String): String? {
        //利用nio提供的类判断文件ContentType
        val file = File(filePath)
        var contentType: String? = null
        try {
            contentType = Files.probeContentType(file.toPath())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //若失败则调用另一个方法进行判断
        if (StringUtils.isBlank(contentType)) {
            contentType = MimetypesFileTypeMap().getContentType(file)
        }
        return contentType
    }

    /**
     * 创建文件夹
     *
     * @param folderPath
     */
    fun createFolder(folderPath: String) {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    /**
     * 生成临时文件目录路径
     *
     * @param basePath
     * @return
     */
    fun generateTempFolderPath(basePath: String): String {
        return basePath + File.separator + TEMP_FOLDER_NAME
    }

    /**
     * 生成临时文件目录路径
     *
     * @return
     */
    fun generateDefaultRootFolderPath(): String {
        return System.getProperty("user.home") + File.separator + DEFAULT_ROOT_FILE_NAME
    }

    /**
     * 删除物理文件
     *
     * @param file
     */
    fun delete(file: File) {
        file.delete()
    }

    /**
     * 移动物理文件
     *
     * @param tempFilePath
     * @param realPath
     */
    @Throws(IOException::class)
    fun moveFile(tempFilePath: String, realPath: String) {
        val source = File(tempFilePath)
        val target = File(realPath)
        if (!target.parentFile.exists()) {
            target.parentFile.mkdirs()
        }
        Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
