package com.c88.admin.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.MemberTagMapper;
import com.c88.admin.pojo.entity.MemberTag;
import com.c88.admin.service.IMemberTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * 會員標籤對應
 */
@Service
public class MemberTagServiceImpl extends ServiceImpl<MemberTagMapper, MemberTag> implements IMemberTagService {

    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchIgnore(Collection<MemberTag> entityList) {
        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        sqlStatement += "Ignore";
        String finalSqlStatement = sqlStatement;
        return executeBatch(entityList, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> sqlSession.insert(finalSqlStatement, entity));
    }

}
