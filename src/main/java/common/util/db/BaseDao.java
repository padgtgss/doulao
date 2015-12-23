package common.util.db;

import common.util.pojo.BaseDomain;
import common.util.pojo.DomainPage;

import java.util.List;
import java.util.Map;

/**
 * @Description: BaseDao
 * @anthor: shi_lin
 * @CreateTime: 2015-12-04
 */

public interface BaseDao {

    /**
     * 持久化对象
     * @param t
     * @param <T>
     * @return
     */
    <T extends BaseDomain> void save(T t);

    /**
     *合并对象
     * @param t
     * @param <T>
     * @return
     */
    <T extends  BaseDomain> void merge(T t);


    /**
     * 根据ID 获取唯一对象
     * @param clazz
     * @param id
     * @param <T>
     * @return
     */
     <T extends  BaseDomain> T getEntityById(Class<T> clazz,Object id);

    /**
     * 查询所有实体对象(此方法会返回所有记录，慎用)
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends  BaseDomain> List<T> getAllEntities(Class<T> clazz);

    /**
     * 分页查询对象
     * @param clazz
     * @param fileName
     * @param fileValue
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends  BaseDomain> DomainPage<T> getEntitiesByPage(Class<T> clazz,String fileName,Object fileValue,long pageIndex,long pageSize);

    /**
     * 多条件查询分页  每个属性采用 = 链接
     * @param clazz
     * @param fieldNameValueMap
     * @param <T>
     * @return
     */
     <T extends BaseDomain> DomainPage<T> getEntitiesPagesByFieldList(Class<T> clazz, Map<String, Object> fieldNameValueMap,long pageIndex, long pageSize);

    /**
     * 多条件查询，每个属性才有 “=”
     * @param clazz
     * @param fieldNameValueMap
     * @param <T>
     * @return
     */
    <T extends BaseDomain> List<T> getEntityByFieldList(Class<T> clazz, Map<String, Object> fieldNameValueMap);

    /**
     * 根据主键修改多个属性值
     * @param clazz
     * @param id
     * @param fieldNameValueMap
     * @param <T>
     */
     <T extends BaseDomain> void update(Class<T> clazz,Object id,Map<String,Object> fieldNameValueMap);

    /**
     * 根据查询参数修改属性
     * @param clazz
     * @param queryFieldNameValueMap
     * @param persistFieldNameValueMap
     * @param <T>
     */
     <T extends BaseDomain> void update(Class<T> clazz,Map<String,Object> queryFieldNameValueMap,Map<String,Object> persistFieldNameValueMap);

    /**
     * 根据主键删除对象
     * @param clazz
     * @param id
     * @param <T>
     */
     <T extends BaseDomain> void remove(Class<T> clazz,Object id);

    /**
     * 根据参数删除对象
     * @param clazz
     * @param fieldNameValueMap
     * @param <T>
     */
     <T extends BaseDomain> void remove(Class<T> clazz,Map<String,Object> fieldNameValueMap);

}
