package com.c88.admin.common.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.c88.common.web.log.IAnnoLogService;
import com.c88.common.web.log.LogDTO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.Map;

import static com.c88.amqp.BroadcastConfig.TOPIC_ANNO_LOG_QUEUE;

@Slf4j
@Configuration
public class QueueConfig {

    @Autowired
    private IAnnoLogService annoLogService;

    @RabbitListener(queues = {TOPIC_ANNO_LOG_QUEUE})
    public void receive(Message message, Channel channel, @Payload LogDTO logDTO, @Headers Map<String, Object> headers) throws IOException {
        // public void receiveMessageFromTopicAnnoLog(LogDTO logDTO) {
        log.info("start_to_log:{}", logDTO.getContent());
        try {
            annoLogService.saveAnnoLog(logDTO);
        } catch (Exception e) {
            log.error("save operation log fail : {}", ExceptionUtil.stacktraceToString(e));
            // channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
