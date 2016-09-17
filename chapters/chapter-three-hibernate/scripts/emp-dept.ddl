create table dept(
  deptno integer,
  dname  text,
  loc    text,
  constraint pk_dept primary key (deptno)
);

create table emp(
  empno    integer,
  ename    text,
  job      text,
  mgr      integer,
  hiredate date,
  sal      integer,
  comm     integer,
  deptno   integer,
  constraint pk_emp primary key (empno),
  constraint fk_deptno foreign key (deptno) references dept (deptno)
);

CREATE UNIQUE INDEX ename_idx ON emp (ename);