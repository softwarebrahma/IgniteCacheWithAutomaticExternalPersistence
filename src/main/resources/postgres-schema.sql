-- Table: dcm.dept

-- DROP TABLE dcm.dept;

CREATE TABLE dcm.dept
(
  deptid text NOT NULL,
  dname text,
  loc text,
  CONSTRAINT pk_dept PRIMARY KEY (deptid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dcm.dept
  OWNER TO postgres;


-- Table: dcm.emp

-- DROP TABLE dcm.emp;

CREATE TABLE dcm.emp
(
  empid text NOT NULL,
  ename text,
  job text,
  mgr text,
  hiredate date,
  sal integer,
  comm integer,
  deptid text,
  CONSTRAINT pk_emp PRIMARY KEY (empid),
  CONSTRAINT fk_deptid FOREIGN KEY (deptid)
      REFERENCES dcm.dept (deptid) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dcm.emp
  OWNER TO postgres;

-- Index: dcm.dcm_emp_ename

-- DROP INDEX dcm.dcm_emp_ename;

CREATE UNIQUE INDEX dcm_emp_ename
  ON dcm.emp
  USING btree
  (ename COLLATE pg_catalog."default");
