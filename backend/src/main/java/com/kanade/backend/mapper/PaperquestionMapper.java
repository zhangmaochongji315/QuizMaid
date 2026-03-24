package com.kanade.backend.mapper;

import com.mybatisflex.core.BaseMapper;
import com.kanade.backend.model.entity.Paperquestion;
import com.mybatisflex.core.query.QueryWrapper;

import java.util.List;

/**
 * 试卷试题关联表 映射层。
 *
 * @author kanade
 */
public interface PaperquestionMapper extends BaseMapper<Paperquestion> {

    List<Paperquestion> selectList(QueryWrapper orderBy);
}
