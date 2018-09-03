package com.itheima.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;



public interface BaseService<T> {
	public List<T> findAll();

	public Page<T> findAll(Pageable pageable);
	
	public List<T> findAll(Specification<T> spec);

	public Page<T> findAll(Specification<T> spec, Pageable pageable);

	public void save(T model);
	
	public void save(List<T> list);

	public T findById(Long uuid);

	public void delete(String ids);

	
}
