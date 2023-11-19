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
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;
import com.example.springboot.service.IUserService;

import com.example.springboot.service.ISubmitService;
import com.example.springboot.entity.Submit;

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
@RequestMapping("/submit")
public class SubmitController {

    @Resource
    private ISubmitService submitService;
    @Resource
    IUserService userService;

    private final String now = DateUtil.now();

    @PostMapping("/score")
    public Result score(@RequestBody Submit submit) {
        Double scores = 0.0;
        if (submit.getEducation().equals("Bachelor")){
            scores += 1;
        } else if (submit.getEducation().equals("Master")){
            scores += 3;
        } else if(submit.getEducation().equals("PhD")){
            scores += 5;
        } else {
            scores += 0;
        }

        if (submit.getExperiences().equals("1-2 years")){
            scores += 1;
        } else if (submit.getExperiences().equals("3-4 years")){
            scores += 2;
        } else if(submit.getExperiences().equals("5-6 years")){
            scores += 3;
        } else if(submit.getExperiences().equals("7-8 years")){
            scores += 4;
        } else {
            scores += 5;
        }

        if(submit.getIsWork().equals("No")){
            scores += 0;
        }else if (submit.getIsWork().equals("1-2 semesters")){
            scores += 1;
        }else{
            scores += 2;
        }

        submit.setScores(scores);
        submitService.updateById(submit);
        return Result.success();
    }

    // add or edit
    @PostMapping
    public Result save(@RequestBody Submit submit) {
        if (submit.getId() == null) {
            Submit one = submitService.getOne(new LambdaQueryWrapper<Submit>().eq(Submit::getUser, TokenUtils.getCurrentUser().getUsername()).eq(Submit::getName, submit.getName()));
            if (Objects.nonNull(one)){
                return Result.error("201","You have already applied for this position, please do not reapply");
            }
            submit.setTime(DateUtil.now());
        }
        submitService.saveOrUpdate(submit);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        submitService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        submitService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(submitService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(submitService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String education,
                           @RequestParam(defaultValue = "") String experiences,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Submit> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("scores");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(education)) {
            queryWrapper.like("education", education);
        }
        if (!"".equals(experiences)) {
            queryWrapper.like("experiences", experiences);
        }
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("user", currentUser.getUsername());
        }
        return Result.success(submitService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * export
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // retrieve all data from the database
        List<Submit> list = submitService.list();
        // perform in-memory operations and write to the browser
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // write objects from the list to Excel at once, using default styles, and enforce output of headers
        writer.write(list, true);

        // set the format of the browser response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Summary", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }
    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

