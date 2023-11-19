package com.example.springboot.service.impl;

import com.example.springboot.entity.Jobs;
import com.example.springboot.mapper.JobsMapper;
import com.example.springboot.service.IJobsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  service implementation class
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class JobsServiceImpl extends ServiceImpl<JobsMapper, Jobs> implements IJobsService {

}
