package com.blu.imdg;

import com.blu.imdg.model.Department;
import com.blu.imdg.model.Employee;
import com.blu.imdg.model.EmployeeKey;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by isatimur on 8/9/16.
 */
public class SqlQueryEmployees {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static Logger logger = LoggerFactory.getLogger(SqlQueryEmployees.class);

    /*
      Replicated cache name to store departments.
      */
    private static final String DEPARTMENT_CACHE_NAME = SqlQueryEmployees.class.getSimpleName() + "-departments";

    /*
      Partitioned cache name to store employees.
      */
    private static final String EMPLOYEE_CACHE_NAME = SqlQueryEmployees.class.getSimpleName() + "-employees";

    /**
     * This is an entry point of SqlQueryEmployees, the ignite configuration lies upon resources directory as
     * example-ignite.xml.
     *
     * @param args Command line arguments, none required.
     */
    public static void main(String[] args) throws Exception {

        //The ignite configuration lies below resources directory as example-ignite.xml.
        Ignite ignite = Ignition.start("example-ignite.xml");

        logger.info("Start. Sql query example.");

        CacheConfiguration<Integer, Department> deptCacheCfg = new CacheConfiguration<>(DEPARTMENT_CACHE_NAME);

        deptCacheCfg.setCacheMode(CacheMode.REPLICATED);
        deptCacheCfg.setIndexedTypes(Integer.class, Department.class);

        CacheConfiguration<EmployeeKey, Employee> employeeCacheCfg = new CacheConfiguration<>(EMPLOYEE_CACHE_NAME);

        employeeCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        employeeCacheCfg.setIndexedTypes(EmployeeKey.class, Employee.class);

        try (
            IgniteCache<Integer, Department> deptCache = ignite.createCache(deptCacheCfg);
            IgniteCache<EmployeeKey, Employee> employeeCache = ignite.createCache(employeeCacheCfg)
        ) {
            // Populate cache.
            initialize();

            // Example for SQL-based querying employees based on salary ranges.
            sqlQuery();

            // Example for SQL-based querying employees for a given department (includes SQL join).
            sqlQueryWithJoin();

            // Output all employees whos get salary bigger than their direct manager (includes SQL join).
            sqlQueryEmployeesWithSalHigherManager();

            // Example for SQL-based fields queries that return only required
            // fields instead of whole key-value pairs.
            sqlFieldsQuery();

            // Example for SQL-based fields queries that uses joins.
            sqlFieldsQueryWithJoin();

            // Example for query that uses aggregation.
            aggregateQuery();

            // Example for query that uses grouping.
            groupByQuery();

            // Full text query example.
            textQuery();

            log("Sql query example finished.");
        }
    }

    /**
     * Let's fill ignite cache with test data. Data are taken from oracle's study test scheme EMP. The structure of
     * those scheme you can see below resources folder of this module.
     *
     * @throws InterruptedException In case of error.
     */
    private static void initialize() throws InterruptedException {
        IgniteCache<Integer, Department> deptCache = Ignition.ignite().cache(DEPARTMENT_CACHE_NAME);
        IgniteCache<EmployeeKey, Employee> employeeCache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Clear caches before start.
        deptCache.clear();
        employeeCache.clear();

        // Departments
        Department dept1 = new Department("Accounting", "New York");
        Department dept2 = new Department("Research", "Dallas");
        Department dept3 = new Department("Sales", "Chicago");
        Department dept4 = new Department("Operations", "Boston");

        // Employees
        Employee emp1 = new Employee("King", dept1, "President", null, localDateOf("17-11-1981"), 5000);
        Employee emp2 = new Employee("Blake", dept3, "Manager", emp1.getEmpno(), localDateOf("01-05-1981"), 2850);
        Employee emp3 = new Employee("Clark", dept1, "Manager", emp1.getEmpno(), localDateOf("09-06-1981"), 2450);
        Employee emp4 = new Employee("Jones", dept2, "Manager", emp1.getEmpno(), localDateOf("02-04-1981"), 2975);
        Employee emp5 = new Employee("Scott", dept2, "Analyst", emp4.getEmpno(), localDateOf("13-07-1987").minusDays(85), 3000);
        Employee emp6 = new Employee("Ford", dept2, "Analyst", emp4.getEmpno(), localDateOf("03-12-1981"), 3000);
        Employee emp7 = new Employee("Smith", dept2, "Clerk", emp6.getEmpno(), localDateOf("17-12-1980"), 800);
        Employee emp8 = new Employee("Allen", dept3, "Salesman", emp2.getEmpno(), localDateOf("20-02-1981"), 1600);
        Employee emp9 = new Employee("Ward", dept3, "Salesman", emp2.getEmpno(), localDateOf("22-02-1981"), 1250);
        Employee emp10 = new Employee("Martin", dept3, "Salesman", emp2.getEmpno(), localDateOf("28-09-1981"), 1250);
        Employee emp11 = new Employee("Turner", dept3, "Salesman", emp2.getEmpno(), localDateOf("08-09-1981"), 1500);
        Employee emp12 = new Employee("Adams", dept2, "Clerk", emp5.getEmpno(), localDateOf("13-07-1987").minusDays(51), 1100);
        Employee emp13 = new Employee("James", dept3, "Clerk", emp2.getEmpno(), localDateOf("03-12-1981"), 950);
        Employee emp14 = new Employee("Miller", dept1, "Clerk", emp3.getEmpno(), localDateOf("23-01-1982"), 1300);

        deptCache.put(dept1.getDeptno(), dept1);
        deptCache.put(dept2.getDeptno(), dept2);
        deptCache.put(dept3.getDeptno(), dept3);
        deptCache.put(dept4.getDeptno(), dept4);

        // Note that in this example we use custom affinity key for Employee objects
        // to ensure that all persons are collocated with their departments.
        employeeCache.put(emp1.getKey(), emp1);
        employeeCache.put(emp2.getKey(), emp2);
        employeeCache.put(emp3.getKey(), emp3);
        employeeCache.put(emp4.getKey(), emp4);
        employeeCache.put(emp5.getKey(), emp5);
        employeeCache.put(emp6.getKey(), emp6);
        employeeCache.put(emp7.getKey(), emp7);
        employeeCache.put(emp8.getKey(), emp8);
        employeeCache.put(emp9.getKey(), emp9);
        employeeCache.put(emp10.getKey(), emp10);
        employeeCache.put(emp11.getKey(), emp11);
        employeeCache.put(emp12.getKey(), emp12);
        employeeCache.put(emp13.getKey(), emp13);
        employeeCache.put(emp14.getKey(), emp14);

        // Wait 1 second to be sure that all nodes processed put requests.
        Thread.sleep(1000);
    }

    /**
     * The method represents set of conditions on salary ranges.
     */
    private static void sqlQuery() {
        IgniteCache<EmployeeKey, Employee> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Create query which selects salaries based on range.
        SqlQuery<EmployeeKey, Employee> qry = new SqlQuery<>(Employee.class, "sal > ? and sal <= ?");

        // Execute queries for salary ranges.
        logDecorated("==Employee with salaries between 0 and 1000==", cache.query(qry.setArgs(0, 1000)).getAll());

        logDecorated("==Employee with salaries between 1000 and 2000==", cache.query(qry.setArgs(1000, 2000)).getAll());

        logDecorated("==Employee with salaries greater than 2000==", cache.query(qry.setArgs(2000, Integer.MAX_VALUE)).getAll());
    }

    /**
     * Example for SQL queries based on all employees working for a specific department.
     */
    private static void sqlQueryWithJoin() {
        IgniteCache<EmployeeKey, Employee> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Create query which joins on 2 types to select people for a specific department.
        SqlQuery<EmployeeKey, Employee> qry = new SqlQuery<>(Employee.class,
            "from Employee, \"" + DEPARTMENT_CACHE_NAME + "\".Department " +
                "where Employee.deptno = Department.deptno " +
                "and lower(Department.dname) = lower(?)");

        // Execute queries for find employees for different departments.
        logDecorated("==Following department 'Accounting' have employees (SQL join)==", cache.query(qry.setArgs("Accounting")).getAll());
        logDecorated("==Following department 'Sales' have employees (SQL join)==", cache.query(qry.setArgs("Sales")).getAll());
    }

    /**
     * Output all employees those salary bigger than their direct manager (SQL-based fields queries and inner select).
     */
    private static void sqlQueryEmployeesWithSalHigherManager() {
        IgniteCache<EmployeeKey, Employee> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // all employees those salary bigger than their direct manager.
        SqlFieldsQuery qry = new SqlFieldsQuery("select e.ename ,e.sal,m.ename,m.sal from Employee e, (select ename,empno,sal from Employee) m " +
            "where e.mgr = m.empno and e.sal > m.sal");
        log("==All employees those salary bigger than their direct manager==");
        logDecorated("||Employee||Emp.Salary||Manager||Mgr.Salary||", cache.query(qry).getAll());
    }

    /**
     * Example for SQL-based fields queries that return only required fields instead of whole key-value pairs.
     */
    private static void sqlFieldsQuery() {
        IgniteCache<?, ?> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Create query to get names of all employees.
        SqlFieldsQuery qry = new SqlFieldsQuery("select ename from Employee");

        // Execute query to get collection of rows. In this particular
        // case each row will have one element with full name of an employees.
        Collection<List<?>> res = cache.query(qry).getAll();

        // Print names.
        log("==Names of all employees==", res);
    }

    /**
     * Example for SQL-based fields queries that return only required fields instead of whole key-value pairs.
     */
    private static void sqlFieldsQueryWithJoin() {
        IgniteCache<?, ?> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Create query to get names of all employees.
        SqlFieldsQuery qry = new SqlFieldsQuery(
            "select e.ename, d.dname " +
                "from Employee e, \"" + DEPARTMENT_CACHE_NAME + "\".Department d " +
                "where e.deptno = d.deptno");

        // Execute query to get collection of rows. In this particular
        // case each row will have one element with full name of an employees.
        Collection<List<?>> res = cache.query(qry).getAll();

        // Print persons' names and departments' names.
        logDecorated("==Names of all employees and departments they belong to (SQL join)==", res);
    }

    /**
     * Example for SQL-based fields queries that return only required fields instead of whole key-value pairs.
     */
    private static void aggregateQuery() {
        IgniteCache<?, ?> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Create query to get sum of salaries and number of summed rows.
        SqlFieldsQuery qry = new SqlFieldsQuery("select sum(sal), count(sal) from Employee");

        // Execute query to get collection of rows.
        Collection<List<?>> res = cache.query(qry).getAll();

        double sum = 0;
        long cnt = 0;

        for (List<?> row : res) {
            // Skip results from nodes without data.
            if (row.get(0) != null) {
                sum += ((BigDecimal)row.get(0)).doubleValue();
                cnt += (Long)row.get(1);
            }
        }

        // Average employee salary
        log("==Average employee salary (aggregation query)==");
        log("\t" + (cnt > 0 ? (sum / cnt) : "n/a"));
    }

    /**
     * Example for SQL-based fields queries that return only required fields instead of whole key-value pairs.
     */
    private static void groupByQuery() {
        IgniteCache<?, ?> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Create query to get salary averages grouped by department name.
        // We don't need to perform any extra manual steps here, because
        // Employee data is colocated based on department IDs.
        SqlFieldsQuery qry = new SqlFieldsQuery(
            "select avg(e.sal), d.dname " +
                "from Employee e, \"" + DEPARTMENT_CACHE_NAME + "\".Department d " +
                "where e.deptno = d.deptno " +
                "group by d.dname " +
                "having avg(e.sal) > ?");

        // Execute query to get collection of rows.
        logDecorated("==Average salaries per Department (group-by query)==", cache.query(qry.setArgs(500)).getAll());
    }

    /**
     * Example for TEXT queries using LUCENE-based indexing of people's job name.
     */
    private static void textQuery() {
        IgniteCache<Integer, Employee> cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        //  Query for all people are working as a "Salesman".
        TextQuery<Integer, Employee> salesman = new TextQuery<>(Employee.class, "Salesman");

        // Query for all people are working as a "Manager".
        TextQuery<Integer, Employee> manager = new TextQuery<>(Employee.class, "Manager");

        log("==Following people are working as a 'Salesman'==", cache.query(salesman).getAll());
        log("==Following people are working as a 'Manager'==", cache.query(manager).getAll());
    }

    /**
     * @param parseDateText
     * @return
     */
    private static LocalDate localDateOf(String parseDateText) {
        return LocalDate.parse(parseDateText, formatter);
    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg) {
        logger.info("\t" + msg);
    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg, Iterable<?> col) {
        logger.info("\t" + msg);
        col.forEach(c -> logger.info("\t\t" + c));
    }

    /**
     * Prints message and resultset to logger.
     *
     * @param msg String.
     * @param col Iterable
     */
    private static void logDecorated(String msg, Iterable<?> col) {
        logger.info("\t" + msg);
        col.forEach(c -> logger.info("\t\t" + c));
    }

}
