package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Submit;
import com.example.springboot.service.ISubmitService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;
import com.example.springboot.service.IUserService;

import com.example.springboot.service.IJobsService;
import com.example.springboot.entity.Jobs;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  frontend controller
 * </p>
 *
 * @author 
 * @since 
 */
@RestController
@RequestMapping("/jobs")
public class JobsController {

    @Resource
    private IJobsService jobsService;

    @Resource
    private ISubmitService submitService;
    @Resource
    IUserService userService;

    private final String now = DateUtil.now();

    // add or edit
    @PostMapping
    public Result save(@RequestBody Jobs jobs) {
        if (jobs.getId() == null) {
            jobs.setTime(DateUtil.now());
        }
        jobsService.saveOrUpdate(jobs);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        jobsService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        jobsService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(jobsService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        Jobs jobs = jobsService.getById(id);
        jobs.setSubmitNums(submitService.count(new LambdaQueryWrapper<Submit>().eq(Submit::getName,jobs.getName())));

        return Result.success(jobs);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Jobs> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        Page<Jobs> page = jobsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().stream().forEach(jobs -> {
            jobs.setSubmitNums(submitService.count(new LambdaQueryWrapper<Submit>().eq(Submit::getName,jobs.getName())));
        });
        return Result.success(page);
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

