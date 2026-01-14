package cn.lazyking.power.controller;


import cn.hutool.core.date.DateUtil;
import cn.lazyking.power.config.OSSConfig;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * 文件上传控制层
 */
@RestController
@RequestMapping("admin/file")
@Api(tags = "文件上传管理")
@RequiredArgsConstructor
public class FileUploadController {

    private final OSS ossClient;
    private final OSSConfig ossConfig;

    @ApiOperation("上传单个文件")
    @PostMapping("upload/element")
    public String uploadFile(MultipartFile file) {
        // 创建以天为单位的文件夹名称
        String folderName = DateUtil.format(DateUtil.date(), "yyyy-MM-dd");
        // 以时间戳作为存储后的文件名
        String fileName = String.valueOf(System.currentTimeMillis());
        // 获取源文件的名称
        String originalFileName = file.getOriginalFilename();
        String suffix = null;
        if (originalFileName != null) {
            suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        // oss 存储对象名称
        String objectName = folderName + "/" + fileName + suffix;

        URL url = null;
        try {
            // 创建 PutObjectRequest 对象
            PutObjectRequest request = new PutObjectRequest(ossConfig.getBucketName(), objectName, file.getInputStream());
            // 上传
            ossClient.putObject(request);
            // 文件上传后的访问地址
            url = ossClient.generatePresignedUrl(
                    ossConfig.getBucketName(),
                    objectName,
                    DateUtil.offsetDay(
                            new Date(),
                            365 * 10
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 注意：不要在这里关闭 ossClient，因为它是 Spring 管理的单例 Bean
        return url.toString();
    }
}
