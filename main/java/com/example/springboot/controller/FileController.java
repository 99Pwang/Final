package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Files;
import com.example.springboot.mapper.FileMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * file-related operations
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String FILE_UPLOAD_PATH = System.getProperty("user.dir") + File.separator + "/files/";

    @Resource
    private FileMapper fileMapper;

    /**
     * upload
     *
     * @param file files passed by the front-end
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // unique id
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(FILE_UPLOAD_PATH + fileUUID);
        // directory exist ? if no, create a new file directory
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // get file's md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // check if a file with the same md5 exists
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            // file exist?
            boolean exist = FileUtil.exist(FILE_UPLOAD_PATH + url.substring(url.lastIndexOf("/") + 1));
            if (!exist) { // not exist
                // delete from db
                fileMapper.deleteById(dbFiles.getId());
                // upload new file
                file.transferTo(uploadFile);
                url = "http://localhost:9090/file/" + fileUUID;
            }
        } else {
            // upload
            file.transferTo(uploadFile);
            url = "http://localhost:9090/file/" + fileUUID;
        }


        // save in database
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024); // kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return url;
    }

    /**
     * download   http://localhost:9090/file/{fileUUID}
     *
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // get file by unique id
        File uploadFile = new File(FILE_UPLOAD_PATH + fileUUID);
        // set output format
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // writes the content of the file to the output stream
        try {
            os.write(FileUtil.readBytes(uploadFile));
        } catch (Exception e) {
            System.err.println("File download failed, the file does not exist");
        }
        os.flush();
        os.close();
    }


    /**
     * retrieve file by md5
     *
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // file's md5 exist?
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    /**
     * paginated search
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // exclude deleted files
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @PostMapping("/uploadImg")
    public Object uploadImg(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // define unique id
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(FILE_UPLOAD_PATH + fileUUID);
        // directory exist ? if no, create a new file directory
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // get md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // same record exists ?
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            file.transferTo(uploadFile);
            // if no duplicate files, the file just uploaded will not be deleted
            url = "http://localhost:9090/file/" + fileUUID;
        }

        // save
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024); // kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        Map<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        map.put("data", JSONUtil.parseArray("[{\"url\": \"" + url + "\"}]"));
        return map;
    }

    // retrieve and serve image from external URL
    @GetMapping("/out/{url}")
    public void out(@PathVariable String url, HttpServletResponse response) throws IOException {
        url = new String(Base64.getDecoder().decode(url), StandardCharsets.UTF_8);
        ResponseEntity<byte[]> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET, null, byte[].class);
        //extracts the body of the HTTP response
        byte[] body = responseEntity.getBody();
        //writes the image data to the output stream
        OutputStream os = response.getOutputStream();
        os.write(body);
        os.flush();
        //close output stream
        os.close();
    }
}
