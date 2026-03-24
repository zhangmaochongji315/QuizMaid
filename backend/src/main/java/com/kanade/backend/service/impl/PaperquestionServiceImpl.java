package com.kanade.backend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.kanade.backend.exception.BusinessException;
import com.kanade.backend.exception.ErrorCode;
import com.kanade.backend.mapper.PaperquestionMapper;
import com.kanade.backend.model.dto.PaperQuestionAddDTO;
import com.kanade.backend.model.dto.PaperQuestionUpdateDTO;
import com.kanade.backend.model.entity.ExamPaper;
import com.kanade.backend.model.entity.Paperquestion;
import com.kanade.backend.service.ExamPaperService;
import com.kanade.backend.service.PaperQuestionService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperQuestionServiceImpl extends ServiceImpl<PaperquestionMapper, Paperquestion> implements PaperQuestionService {

    private final ExamPaperService examPaperService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addQuestionToPaper(PaperQuestionAddDTO dto) {
        // 检查试卷存在且当前用户有权限
        ExamPaper paper = examPaperService.getById(dto.getPaperId());
        if (paper == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "试卷不存在");
        }
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!paper.getCreatorId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "无权操作此试卷");
        }
        // 检查是否已关联
        Paperquestion existing = this.getOne(
                QueryWrapper.create().eq("paperId", dto.getPaperId()).eq("questionId", dto.getQuestionId())
        );
        if (existing != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "试题已在该试卷中");
        }
        // 创建关联
        Paperquestion relation = new Paperquestion();
        relation.setPaperId(dto.getPaperId());
        relation.setQuestionId(dto.getQuestionId());
        relation.setQuestionScore(dto.getQuestionScore());
        relation.setSort(dto.getSort() != null ? dto.getSort() : 0);
        this.save(relation);
        // 重新计算总分并更新试卷
        recalcTotalScore(dto.getPaperId());
        return relation.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePaperQuestion(PaperQuestionUpdateDTO dto) {
        Paperquestion relation = this.getById(dto.getId());
        if (relation == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "关联不存在");
        }
        // 校验权限：试卷创建人才能修改
        ExamPaper paper = examPaperService.getById(relation.getPaperId());
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!paper.getCreatorId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "无权操作");
        }
        if (dto.getQuestionScore() != null) {
            relation.setQuestionScore(dto.getQuestionScore());
        }
        if (dto.getSort() != null) {
            relation.setSort(dto.getSort());
        }
        boolean updated = this.updateById(relation);
        if (updated) {
            recalcTotalScore(relation.getPaperId());
        }
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeQuestionFromPaper(Long relationId) {
        Paperquestion relation = this.getById(relationId);
        if (relation == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "关联不存在");
        }
        // 校验权限
        ExamPaper paper = examPaperService.getById(relation.getPaperId());
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!paper.getCreatorId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "无权操作");
        }
        boolean removed = this.removeById(relationId);
        if (removed) {
            recalcTotalScore(relation.getPaperId());
        }
        return removed;
    }

    private void recalcTotalScore(Long paperId) {
        List<Paperquestion> relations = this.list(QueryWrapper.create().eq("paperId", paperId));
        int total = relations.stream().mapToInt(Paperquestion::getQuestionScore).sum();
        ExamPaper paper = new ExamPaper();
        paper.setId(paperId);
        paper.setTotalScore(total);
        examPaperService.updateById(paper);
    }
}