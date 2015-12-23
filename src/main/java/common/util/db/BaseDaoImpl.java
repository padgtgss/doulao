package common.util.db;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import common.util.enums.AvailableEnum;
import common.util.pojo.BaseDomain;
import common.util.pojo.DomainPage;
import common.var.constants.SystemConstant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: BaseDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-12-04
 */

public class BaseDaoImpl implements BaseDao {

    protected Datastore ds = DBCollection.datastore;


    @Override
    public <T extends BaseDomain> void save(T t) {
        t.setAvailableEnum(AvailableEnum.AVAILABLE);
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
        t.setUpdateTime(sdf.format(new Date()));
        t.setCreateTime(sdf.format(new Date()));
        ds.save(t);
    }

    @Override
    public <T extends BaseDomain> void merge(T t) {
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
        t.setUpdateTime(sdf.format(new Date()));
        ds.merge(t);
    }

    @Override
    public <T extends BaseDomain> T getEntityById(Class<T> clazz, Object id)  {
        try {
            T t = clazz.newInstance();
            t.setId(new ObjectId(id.toString()));
            return ds.get(t);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T extends BaseDomain> List<T> getAllEntities(Class<T> clazz) {
          return  ds.createQuery(clazz).filter("available",AvailableEnum.AVAILABLE).asList();
    }

    @Override
    public <T extends BaseDomain> DomainPage<T> getEntitiesByPage(Class<T> clazz, String fileName, Object fileValue, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        Query<T> query = ds.createQuery(clazz).field(fileName).equal(fileValue);
        query.filter("available",AvailableEnum.AVAILABLE);
        long totalCount = ds.getCount(query);
        query.offset((int) ((pageIndex - 1) * pageSize));
        query.limit((int)pageSize);
        List<T> resultList = query.asList();
        DomainPage<T> domainPage = new DomainPage<T>(pageSize,pageIndex,totalCount);
        domainPage.setDomains(resultList);
        return domainPage;
    }

    @Override
    public <T extends BaseDomain> DomainPage<T> getEntitiesPagesByFieldList(Class<T> clazz, Map<String, Object> fieldNameValueMap,long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        Query<T> query = ds.createQuery(clazz);
        query.filter("available",AvailableEnum.AVAILABLE);
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for(int i =0;i<fieldNames.size();i++){
            String fieldName = iterator.next();
            query.field(fieldName).equal(fieldNameValueMap.get(fieldName));
        }

        long totalCount =  query.asList().size();
        query.offset((int) ((pageIndex - 1) * pageSize));
        query.limit((int)pageSize);
        List<T> resultList = query.asList();
        DomainPage<T> domainPage = new DomainPage<T>(pageSize,pageIndex,totalCount);
        domainPage.setDomains(resultList);
        return domainPage;

    }

    @Override
    public <T extends BaseDomain> List<T> getEntityByFieldList(Class<T> clazz, Map<String, Object> fieldNameValueMap) {
        Query<T> query = ds.createQuery(clazz);
        query.filter("available",AvailableEnum.AVAILABLE);
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for(int i =0;i<fieldNames.size();i++){
            String fieldName = iterator.next();
            query.field(fieldName).equal(fieldNameValueMap.get(fieldName));
        }
        return query.asList();
    }

    @Override
    public <T extends BaseDomain> void update(Class<T> clazz, Object id, Map<String, Object> fieldNameValueMap) {

        Query<T> query = ds.createQuery(clazz);
        query.filter("_id",new ObjectId(id.toString()));
        UpdateOperations<T> updateOperations = ds.createUpdateOperations(clazz);
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for(int i =0;i<fieldNames.size();i++){
            String fieldName = iterator.next();
            updateOperations.set(fieldName, fieldNameValueMap.get(fieldName));
        }
        updateOperations.set("updateTime",new Date());
        ds.update(query,updateOperations);

    }

    @Override
    public <T extends BaseDomain> void update(Class<T> clazz, Map<String, Object> queryFieldNameValueMap, Map<String, Object> persistFieldNameValueMap) {
        Query<T> query = ds.createQuery(clazz);

        Set<String> queryFieldNames = queryFieldNameValueMap.keySet();
        Iterator<String> iterator1 = queryFieldNames.iterator();
        for(int i =0;i<queryFieldNames.size();i++) {
            String fieldName = iterator1.next();
            query.filter(fieldName,queryFieldNameValueMap.get(fieldName));
        }



        UpdateOperations<T> updateOperations = ds.createUpdateOperations(clazz);
        Set<String> fieldNames = persistFieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for(int i =0;i<fieldNames.size();i++){
            String fieldName = iterator.next();
            updateOperations.set(fieldName, persistFieldNameValueMap.get(fieldName));
        }

        updateOperations.set("updateTime",new Date());
        ds.update(query,updateOperations);

    }

    @Override
    public <T extends BaseDomain> void remove(Class<T> clazz, Object id) {
        Query<T> query = ds.createQuery(clazz);
        query.filter("_id",new ObjectId(id.toString()));
        ds.delete(query);
    }

    @Override
    public <T extends BaseDomain> void remove(Class<T> clazz, Map<String, Object> fieldNameValueMap) {
        Query<T> query = ds.createQuery(clazz);
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for(int i =0;i<fieldNames.size();i++){
            String fieldName = iterator.next();
            query.filter(fieldName, fieldNameValueMap.get(fieldName));
        }
        ds.delete(query);
    }


}
