package com.itheima.bos.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.itheima.bos.dao.BaseDao;
import com.itheima.bos.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T>{

	/**
	 * @Resource: 优先bean的名称查询，名称找不到也 会找对应的类型bean
	 *                  注意：虽然可以根据类型查找，但是不识别泛型类型。
	 * @Autowired： 优先根据bean的类型查找，查找不到报错。如果需要根据名称，可以结合@Qualifer
	 * 					注意：根据类型查找的时候，还会识别泛型类型
	 * 
	 */
	@Autowired
	private BaseDao<T> baseDao;

	@Override
	public List<T> findAll() {
		return baseDao.findAll();
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return baseDao.findAll(pageable);
	}

	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		return baseDao.findAll(spec, pageable);
	}

	@Override
	public void save(T model) {
		baseDao.save(model);
	}

	@Override
	public T findById(Long uuid) {
		return baseDao.findOne(uuid);
	}

	@Override
	public void delete(String ids) {
		if(!StringUtils.isEmpty(ids)){
			//切割
			String[] idArray = ids.split(",");
			//遍历
			for (String id : idArray) {
				baseDao.delete(Long.parseLong(id));
			}
		}
	}

	@Override
	public void save(List<T> list) {
		baseDao.save(list);
	}

	@Override
	public List<T> findAll(Specification<T> spec) {
		return baseDao.findAll(spec);
	}
}
