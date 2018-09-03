package com.itheima.bos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 通用的Dao
 * @author lenovo
 *
 */
@NoRepositoryBean // 主动拒绝Spring data jpa生成代理类对象。
public interface BaseDao<T>  extends JpaRepository<T, Long>,JpaSpecificationExecutor<T>{

}
