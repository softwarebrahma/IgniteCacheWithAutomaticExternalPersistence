package com.brocade.dcm.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brocade.dcm.domain.model.Dept;
import com.brocade.dcm.domain.model.Emp;
import com.brocade.dcm.server.service.ObjectCacheMgrService;

@RestController
public class ObjectCacheMgrServiceController {
	
	@Autowired
	private ObjectCacheMgrService objectCacheMgrService;
	
	@PostMapping("/dcm/deptwithemps")
	public ResponseEntity<Boolean> putPost(@RequestBody DeptWithEmpsContext deptWithEmpsContext) {
		try {
			objectCacheMgrService.insertDepartmentWithEmployees(deptWithEmpsContext.getDept(), deptWithEmpsContext.getEmpList());
			return new ResponseEntity<>(true, HttpStatus.CREATED);
		} catch(Exception e) {
			System.out.println("Caught : " + e.getMessage());
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}	
	}
	
	private static final class DeptWithEmpsContext {
		private Dept dept;
		private List<Emp> empList;
		public Dept getDept() {
			return dept;
		}
		public void setDept(Dept dept) {
			this.dept = dept;
		}
		public List<Emp> getEmpList() {
			return empList;
		}
		public void setEmpList(List<Emp> empList) {
			this.empList = empList;
		}
	}
	
	@GetMapping("/dcm/depts")
	public ResponseEntity<List<Dept>> getDepts(@RequestParam(required=false, defaultValue="") String deptuUID) {
		return new ResponseEntity<>(objectCacheMgrService.getDepts(deptuUID), HttpStatus.OK);
	}
	
	@GetMapping("/dcm/emps")
	public ResponseEntity<List<Emp>> getEmps(@RequestParam(required=false, defaultValue="") String empuUID) {
		return new ResponseEntity<>(objectCacheMgrService.getEmps(empuUID), HttpStatus.OK);
	}
	
}
