Employees
---------
EmployeeID	FirstName	LastName	ManagerID	Salary	JobPosition		Active
------------------------------------------------------------------------------
123			John		Smith		147			2000	QA Engineer		0
456			Victoria	Mush		147			3000	Senior Engineer	1
789			Harry		Thomason	258			1500	QA Engineer		1

Managers
--------
ManagerID	FirstName	LastName
---------------------------------
147			Maria		Ferrero
258			Jack		Henderson
369			Bob			Park

--1.	Посчитать количество всех сотрудников, которые до сих пор работают в компании (флаг Active).
SELECT count(*)
FROM Employees e
WHERE e.Active == 1

--2.	Посчитать количество всех сотрудников на каждой должности.
SELECT e.JobPosition, COUNT(e.JobPosition) AS "CountEmployees"
FROM Employees e 
GROUP BY e.JobPosition 

--3.	Вывести тех сотрудников (FirstName, LastName), у которых заработная плата больше либо равна 2000$.
SELECT e.FirstName, e.LastName 
FROM Employees e 
WHERE e.Salary >= 2000

--4.	Вывести только те должности (JobPosition) сотрудников, где средняя заработная плата превышает 2000$.
SELECT e.JobPosition 
FROM Employees e 
GROUP BY e.JobPosition 
HAVING AVG(e.Salary) > 2000 

--5.	Вывести FirstName, LastName только тех сотрудников,имя менеджера которых начинается на букву M.
SELECT e.FirstName, e.LastName 
FROM Employees e 
WHERE e.ManagerID = (
	SELECT m.ManagerID 
	FROM Managers m 
	WHERE m.FirstName LIKE 'M%'
)

--6.	Предположим, что это все записи, которые имеются в таблицах Employees и Managers. 
--Какой будет итоговый результат следующего запроса?
--
--select e.FirstName, e.LastName, e.ManagerID, m.ManagerID from Employees e
--right join Managers m
--on e.ManagerID = m.ManagerID

FirstName	LastName	ManagerID	ManagerID
---------------------------------------------
John		Smith		123			147
Victoria	Mush		456			147
Harry		Thomason	789			258
NULL		NULL 		NULL		369



