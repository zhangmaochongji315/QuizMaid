package com.kanade.backend.service;

import com.kanade.backend.model.dto.PaperQuestionAddDTO;
import com.kanade.backend.model.dto.PaperQuestionUpdateDTO;
import com.kanade.backend.model.entity.Paperquestion;
import com.mybatisflex.core.service.IService;

public interface PaperQuestionService extends IService<Paperquestion> {

    Long addQuestionToPaper(PaperQuestionAddDTO dto);

    boolean updatePaperQuestion(PaperQuestionUpdateDTO dto);

    boolean removeQuestionFromPaper(Long relationId);
}