package com.homvee.youhui.service.dictionary.impl;

import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.dao.dictionary.DictionaryDao;
import com.homvee.youhui.dao.dictionary.model.Dictionary;
import com.homvee.youhui.service.BaseServiceImpl;
import com.homvee.youhui.service.dictionary.DictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary,Long> implements DictionaryService {

    @Resource
    private DictionaryDao dictionaryDao;



    @Override
    public List<Dictionary> findAll() {
        return dictionaryDao.findByYn(YNEnum.YES.getVal());
    }

}
