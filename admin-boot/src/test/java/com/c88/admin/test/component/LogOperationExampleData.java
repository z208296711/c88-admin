package com.c88.admin.test.component;

import cn.hutool.json.JSONUtil;
import com.c88.admin.test.form.UserDeleteForm;
import com.c88.admin.test.form.UserInsertForm;
import com.c88.admin.test.form.UserUpdateForm;
import com.c88.admin.test.vo.UserVo;
import com.c88.common.core.result.Result;
import com.c88.common.web.annotation.AnnoLog;
import com.c88.common.web.annotation.AnnoLogs;
import com.c88.common.web.log.LogOpResponse;
import com.c88.common.web.log.OperationEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;
import java.text.ParseException;

@Slf4j
@Component
public class LogOperationExampleData {

    @AnnoLog(uuId = "#userId",
            content = "{id:#userId,index:#index,size:#size}",
            desc = "查詢頁面${userId}",
            menu = "查詢",
            menuPage = "查询列表")
    public ResponseEntity oneLog(String userId, String index, int size) {
        return ResponseEntity.ok("onLog:" + userId + index + size);
    }


    @AnnoLogs(value = {
            @AnnoLog(uuId = "#userId", content = "{id:#userId,index:#index,size:#size}", desc = "日誌記錄1${userId}", menu = "日誌管理", menuPage = "日誌紀錄1"),
            @AnnoLog(uuId = "#userId", content = "{id:#userId,index:#index,size:#size}", desc = "日誌記錄2${userId}", menu = "日誌管理", menuPage = "日誌紀錄2")
    })
    public ResponseEntity manyLogs(String userId, String index, int size) {
        return ResponseEntity.ok("manyLogs Done");
    }


    @AnnoLog(uuId = "#userId",
            operationEnum = OperationEnum.INSERT,
            content = "{name:#form.name, age:#form.age, address:#form.address, birthDate: #form.birthDate}",
            desc = "新增了一位使用者: 名稱:${name}, 年紀:${age}, 生日:${birthDate}",
            menu = "使用者",
            menuPage = "新增使用者")
    public ResponseEntity<LogOpResponse> insert(@RequestBody UserInsertForm form) {
        log.info("{}", form);

        LogOpResponse<UserInsertForm, ?> response = new LogOpResponse<>();
        response.setBefore(form);
        return ResponseEntity.ok(response);
    }

    @AnnoLog(uuId = "#userId",
            operationEnum = OperationEnum.UPDATE,
            content = "{id:#form.id, name:#form.name, age:#form.age}",
            desc = "修改了一位使用者:${name}, 修改內容:${content}",
            menu = "使用者",
            menuPage = "修改使用者",
            i18nKey = "這是i18nKey")
    public Result<LogOpResponse> modifyWithOutPut(@RequestBody UserUpdateForm form) throws ParseException {
        log.info("----------------- modifyWithOutPut start -------------------");

        UserVo oldUser = new UserVo();
        oldUser.setId(BigInteger.valueOf(5l));
        oldUser.setName("James");
        oldUser.setAge(33);
        oldUser.setBirthDate(DateUtils.parseDate("2010-03-05 15:15:15", "yyyy-MM-dd hh:mm:ss"));
        oldUser.setAddress("台北市健康路156號");
        log.info("User Updated input{}, original:{}", JSONUtil.toJsonStr(form), JSONUtil.toJsonStr(oldUser));

        LogOpResponse<UserVo, UserUpdateForm> response = new LogOpResponse<>();
        response.setBefore(oldUser);
        response.setAfter(form);
        log.info("----------------- modifyWithOutPut end -------------------");
        return Result.success(response);
    }

    @AnnoLog(uuId = "#userId",
            operationEnum = OperationEnum.UPDATE,
            content = "{id:#form.id, name:#form.name, age:#form.age}",
            desc = "修改了一位使用者:${name}, 修改內容:${content}",
            menu = "使用者",
            menuPage = "修改使用者")
    public ResponseEntity<LogOpResponse> modifyWithNoOutPut(@RequestBody UserUpdateForm form) throws ParseException {
        log.info("----------------- modifyWithNoOutPut start -------------------");

        UserVo oldUser = new UserVo();
        oldUser.setId(BigInteger.valueOf(5l));
        oldUser.setName("James");
        oldUser.setAge(33);
        oldUser.setBirthDate(DateUtils.parseDate("2010-03-05 15:15:15", "yyyy-MM-dd hh:mm:ss"));
        oldUser.setAddress("台北市健康路156號");
        log.info("User Updated input{}, original:{} ", JSONUtil.toJsonStr(form), JSONUtil.toJsonStr(oldUser));

        LogOpResponse<UserUpdateForm, UserVo> response = new LogOpResponse<>();
        response.setBefore(form);
        log.info("----------------- modifyWithNoOutPut end -------------------");
        return ResponseEntity.ok(response);
    }

    @AnnoLog(uuId = "#userId",
            operationEnum = OperationEnum.DELETE,
            content = "{id:#form.id}",
            desc = "刪除了一位使用者: id:${id}, 名稱:${name}",
            menu = "使用者",
            menuPage = "刪除使用者")
    public ResponseEntity<LogOpResponse> deleteWithOutput(@PathVariable UserDeleteForm form) {

        log.info("----------------- delseteWithOutput start -------------------");
        UserVo v = new UserVo();
        v.setId(form.getId());
        v.setName("amber");
        log.info("User Deleted id:{}, name:{}", v.getId(), v.getName());

        LogOpResponse<?, UserVo> response = new LogOpResponse<>();
        response.setAfter(v);
        log.info("----------------- deleteWithOutput end -------------------");
        return ResponseEntity.ok(response);
    }

    @AnnoLog(uuId = "#userId",
            operationEnum = OperationEnum.DELETE,
            content = "{userId:#form.id}",
            desc = "刪除了一位使用者: id:${userId}, 名稱:${name}",
            menu = "使用者",
            menuPage = "刪除使用者")
    public ResponseEntity<?> deleteWithNoOutput(@PathVariable UserDeleteForm form) {
        log.info("----------------- deleteWithNoOutput start -------------------");

        UserVo v = new UserVo();
        v.setId(form.getId());
        v.setName("amber");

        log.info("User Deleted id:{}, name:{}", v.getId(), v.getName());
        log.info("----------------- deleteWithNoOutput end -------------------");
        return ResponseEntity.ok("ok");
    }
}
