package com.itheima.bos.action;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itheima.bos.service.BaseService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	
	private T model;
	
	//****给model创建对象****
	public BaseAction(){
		try {
			//System.out.println("BaseAction==============");
			
			//使用反射创建model对象
			//1.获取子类类型： StandardAction.class
			//this:当前运行时的实例
			//System.out.println(this.getClass()+"====");
			Class clz = this.getClass();
			
			//2.获取类的泛型父类 : BaseAction<Standard>
			//Type: 是Java里面所有类型的父接口
			Type type = clz.getGenericSuperclass();
			
			//3.把Type转换为具体的类型: BaseAction<Standard>
			ParameterizedType pt = (ParameterizedType)type;
			
			//4.从具体类型中获取泛型  : Standard.class
			System.out.println(pt.getActualTypeArguments()[0]);
			
			Class modelClass = (Class)pt.getActualTypeArguments()[0];
			
			//5.创建类型的对象
			model = (T) modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public T getModel() {
		return model;
	}

	@Autowired
	private BaseService<T> baseService;
	
	/**
	 * 查询所有
	 * @throws Exception 
	 */
	@Action("list")
	public void list() throws Exception{
		//1.调用业务，返回数据
		List<T> list = baseService.findAll();
		
		writeJson(list);
	}
	
	/**
	 * 转换对象为json字符串，并且写回页面
	 * @param list
	 * @throws IOException
	 */
	protected void writeJson(Object target) throws IOException {
		//2.把数据转换为json字符串:使用fastJSON
		//toJSONString(): 把任何对象转换为json字符串
		//SerializerFeature.DisableCircularReferenceDetect：关闭fastJSON循环引用重复检测机制
		String jsonString = JSON.toJSONString(target,SerializerFeature.DisableCircularReferenceDetect);
		
		//3.把json字符串写回页面
		HttpServletResponse response = ServletActionContext.getResponse();
		//设置返回类型和编码
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write(jsonString);
	}
	
	//使用属性驱动接收分页参数
	private Integer page;
	private Integer rows;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	//设计一个Map集合，用于封装需要回传给页面的对象
	protected Map<String,Object> result = new HashMap<String,Object>();
	
	/**
	 * 分页查询+条件
	 * @throws Exception 
	 */
	@Action("listByPage")
	public void listByPage() throws Exception{
		//封装Pageable对象
		Pageable pageable = new PageRequest(page-1,rows);
		
		Specification<T> spec = buildSpecification();
		
		//调用业务方法
		Page<T> pageBean = baseService.findAll(spec,pageable);
		
		//存入数据
		result.put("total", pageBean.getTotalElements());
		result.put("rows", pageBean.getContent());
		
		//写出json字符串
		writeJson(result);
	}

	protected abstract Specification<T> buildSpecification();
	
	/**
	 * 保存数据
	 * @throws Exception 
	 */
	@Action("save")
	public void save() throws Exception{
		try {
			baseService.save(model);
			
			//成功：{success:true}
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			//失败：{success:false,msg:错误信息}
			result.put("success", false);
			result.put("msg", e.getMessage());
		}finally{
			writeJson(result);
		}
	}
	
	private Long uuid;
	public Long getUuid() {
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	/**
	 * 根据id查询
	 * @throws Exception 
	 */
	@Action("findById")
	public void findById() throws Exception{
		T standard = baseService.findById(uuid);
		writeJson(standard);
	}
	
	private String ids;
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * 删除
	 * @throws Exception 
	 */
	@Action("delete")
	public void delete() throws Exception{
		try {
			baseService.delete(ids);
			
			//成功：{success:true}
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			//失败：{success:false,msg:错误信息}
			result.put("success", false);
			result.put("msg", e.getMessage());
		}finally{
			writeJson(result);
		}
	}
}
