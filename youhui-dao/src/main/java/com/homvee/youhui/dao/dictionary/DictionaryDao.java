package com.homvee.youhui.dao.dictionary;

import com.homvee.youhui.dao.dictionary.model.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:07
 */
public interface DictionaryDao extends JpaRepository<Dictionary, Long>,DictionaryDaoExt  {

    /**
     * find all
     * @return
     */
    List<Dictionary> findByYn(Integer yn);

}