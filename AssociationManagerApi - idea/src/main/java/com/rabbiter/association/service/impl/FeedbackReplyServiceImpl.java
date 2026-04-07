package com.rabbiter.association.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.FeedbackReplyDao;
import com.rabbiter.association.entity.FeedbackReply;
import com.rabbiter.association.service.FeedbackReplyService;
import org.springframework.stereotype.Service;
@Service
public class FeedbackReplyServiceImpl extends ServiceImpl<FeedbackReplyDao, FeedbackReply> implements FeedbackReplyService {
}
