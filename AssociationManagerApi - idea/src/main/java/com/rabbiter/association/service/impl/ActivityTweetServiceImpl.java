package com.rabbiter.association.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.ActivityTweetDao;
import com.rabbiter.association.entity.ActivityTweet;
import com.rabbiter.association.service.ActivityTweetService;
import org.springframework.stereotype.Service;
@Service
public class ActivityTweetServiceImpl extends ServiceImpl<ActivityTweetDao, ActivityTweet> implements ActivityTweetService {
}
