package com.homvee.youhui.service.dictionary;

import com.homvee.youhui.dao.dictionary.model.Dictionary;
import com.homvee.youhui.service.BaseService;

import java.util.List;

public interface DictionaryService extends BaseService<Dictionary, Long> {
    /**
     * find all
     * @return
     */
    List<Dictionary> findAll();

}
