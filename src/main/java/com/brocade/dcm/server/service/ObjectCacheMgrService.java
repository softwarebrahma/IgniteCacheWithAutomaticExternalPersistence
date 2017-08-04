package com.brocade.dcm.server.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.cache.CacheMetrics;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgniteUuid;
import org.apache.ignite.mxbean.CacheMetricsMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.brocade.dcm.domain.model.Dept;
import com.brocade.dcm.domain.model.Emp;

@Service
public class ObjectCacheMgrService {
	
	@Autowired
	private IgniteSpringBean igniteSpringBean;
	
	private IgniteCache<String, Dept> deptCache;
	
	private IgniteCache<String, Emp> empCache;
	
	private IgniteAtomicSequence igniteAtomicSequence ;
	
	@PostConstruct
	public void setupViewCache() {
		System.out.println("*******************TESTING******************* igniteSpringBean name : " + igniteSpringBean.name());
		
		igniteAtomicSequence = igniteSpringBean.atomicSequence("seqName", System.currentTimeMillis(), true);
		
		System.out.println("*******************TESTING******************* Loading cache: DeptCache");
		deptCache = igniteSpringBean.cache("DeptCache");
		deptCache.loadCache(null);
		
		System.out.println("*******************TESTING******************* Loading cache: EmpCache");
		empCache = igniteSpringBean.cache("EmpCache");
		empCache.loadCache(null);
		
		/*SqlFieldsQuery query = new SqlFieldsQuery("CREATE INDEX idx_dept_id ON Dept (id)");
		deptCache.query(query).getAll();*/
		
		CacheMetricsMXBean deptCacheMetricsMXBean = deptCache.localMxBean();
		CacheMetrics deptCacheMetrics = deptCache.localMetrics();
		System.out.println("*******************TESTING******************* DEPT CACHE KEY TYPE : " + deptCacheMetricsMXBean.getKeyType());
		System.out.println("*******************TESTING******************* DEPT CACHE VALUE TYPE : " + deptCacheMetricsMXBean.getValueType());
		System.out.println("*******************TESTING******************* DEPT CACHE KEY TYPE : " + deptCacheMetrics.getKeyType());
		System.out.println("*******************TESTING******************* DEPT CACHE VALUE TYPE : " + deptCacheMetrics.getValueType());
		
		CacheMetricsMXBean empCacheMetricsMXBean = empCache.localMxBean();
		CacheMetrics empCacheMetrics = empCache.localMetrics();
		System.out.println("*******************TESTING******************* EMP CACHE KEY TYPE : " + empCacheMetricsMXBean.getKeyType());
		System.out.println("*******************TESTING******************* EMP CACHE VALUE TYPE : " + empCacheMetricsMXBean.getValueType());
		System.out.println("*******************TESTING******************* EMP CACHE KEY TYPE : " + empCacheMetrics.getKeyType());
		System.out.println("*******************TESTING******************* EMP CACHE VALUE TYPE : " + empCacheMetrics.getValueType());
		
		ContinuousQuery<String, Dept> qry = new ContinuousQuery<>();
		qry.setInitialQuery(new ScanQuery<String, Dept>(new IgniteBiPredicate<String, Dept>() {
            @Override public boolean apply(String key, Dept val) {
                return true;
            }
        }));
			
		qry.setLocalListener(new CacheEntryUpdatedListener<String, Dept>() {
            @Override public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends Dept>> evts) {
                for (CacheEntryEvent<? extends String, ? extends Dept> e : evts) {
                	try {
                		System.out.println("*******************TESTING******************* INSIDE LOCAL LISTENER : Event Type: " + e.getEventType() + 
                				"Event [key=" + e.getKey() + ", val=" + e.getValue() + ']' + ", old val=" + e.getOldValue());
                	} catch (Exception ex) {
                		System.out.println("*******************TESTING******************* EXCEPTION INSIDE LOCAL LISTENER : " + ex.getClass().getName() + " : " + ex.getMessage());
                	}
                }
            }
        });
		deptCache.query(qry);
		junkMethod();
	}
	
	private void junkMethod() {
		long t1 = System.currentTimeMillis();
		IgniteUuid uUID = new IgniteUuid(UUID.randomUUID(), igniteAtomicSequence.incrementAndGet());
		long t2 = System.currentTimeMillis();
		System.out.println("Time for IgniteUuid generation (millis) : " + (t2 - t1));
		
		System.out.println("IgniteUuid string is\n" + uUID);
		String compressedUUID = UUIDUtils.compressIgniteUUID(uUID);
		System.out.println("Compressed IgniteUuid string is\n" + compressedUUID);
		
		IgniteUuid uuid2 = UUIDUtils.decompressIgniteUUID(compressedUUID);
		System.out.println("Decompressed IgniteUuid string is\n" + uuid2);
		System.out.println("Check if both IgniteUuids are same True/False : " + uUID.equals(uuid2));
	}
	
	@SpectreLocalReadWriteTransaction
	public void insertDepartmentWithEmployees(Dept dept, List<Emp> empList) {
		try {
			System.out.println("==== in ObjectCacheMgrService.insertDepartmentWithEmployees ==== for dept : " + dept);
			long t1 = System.currentTimeMillis();
			String uUID = new IgniteUuid(UUID.randomUUID(), igniteAtomicSequence.incrementAndGet()).toString();
			long t2 = System.currentTimeMillis();
			System.out.println("Time for UUID generation (millis) : " + (t2 - t1));
			dept.setDeptid(uUID);
			deptCache.getAndPut(uUID, dept);
			System.out.println("==== in ObjectCacheMgrService.insertDepartmentWithEmployees : department ==== inserted succesfully : " + dept);
			for (Emp emp : empList) {
				t1 = System.currentTimeMillis();
				uUID = new IgniteUuid(UUID.randomUUID(), igniteAtomicSequence.incrementAndGet()).toString();
				t2 = System.currentTimeMillis();
				System.out.println("Time for UUID generation (millis) : " + (t2 - t1));
				emp.setEmpid(uUID);
				emp.setDeptid(dept.getDeptid());
				empCache.getAndPut(uUID, emp);
				System.out.println("==== in ObjectCacheMgrService.insertDepartmentWithEmployees : employee ==== inserted succesfully : " + emp);
			}
		} catch (Exception e) {
			System.out.println("==== EXCEPTION in ObjectCacheMgrService.insertDepartmentWithEmployees ==== message is : " + e.getMessage());
			System.out.println("==== EXCEPTION in ObjectCacheMgrService.insertDepartmentWithEmployees ==== class is : " + e.getClass().getName());
			e.printStackTrace();
		}
	}
	
	@SpectreLocalReadOnlyTransaction
	public List<Dept> getDepts(String uUID) {
		List<Dept> depts = null;
		if (StringUtils.isEmpty(uUID)) {
			System.out.println("==== in objectCacheMgrService.getDepts Getting all Depts ==== ");
			SqlQuery<String, Dept> sqlQuery = new SqlQuery<>(Dept.class, "");
			 depts = deptCache.query(sqlQuery).getAll().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
		 } else {
			 System.out.println("==== in objectCacheMgrService.getDepts ==== for deptUUID : " + uUID);
			 depts = Arrays.asList(deptCache.get(uUID));
		 }
		System.out.println("==== in objectCacheMgrService.getDepts ==== returning : " + Arrays.deepToString(depts.toArray()));
		return depts;
	}
	
	@SpectreLocalReadOnlyTransaction
	public List<Emp> getEmps(String uUID) {
		List<Emp> emps = null;
		if (StringUtils.isEmpty(uUID)) {
			System.out.println("==== in objectCacheMgrService.getEmps Getting all Emps ==== ");
			SqlQuery<String, Emp> sqlQuery = new SqlQuery<>(Emp.class, "");
			 emps = empCache.query(sqlQuery).getAll().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
		 } else {
			 System.out.println("==== in objectCacheMgrService.getEmps ==== for empuUID : " + uUID);
				emps = Arrays.asList(empCache.get(uUID)); 
		 }
		System.out.println("==== in objectCacheMgrService.getEmps ==== returning : " + Arrays.deepToString(emps.toArray()));
		return emps;
	}
	
}
